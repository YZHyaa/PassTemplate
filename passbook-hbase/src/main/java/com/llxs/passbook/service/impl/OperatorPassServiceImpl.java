package com.llxs.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.constant.Constants;
import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.mapper.PassRowMapper;
import com.llxs.passbook.mapper.PassTemplateRowMapper;
import com.llxs.passbook.service.OperatorPassService;
import com.llxs.passbook.utils.RowKeyGenUtil;
import com.llxs.passbook.vo.GainPassTemplateRequest;
import com.llxs.passbook.vo.Pass;
import com.llxs.passbook.vo.PassTemplate;
import com.llxs.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OperatorPassServiceImpl implements OperatorPassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Response gainPassTempate(GainPassTemplateRequest request) {

        String passTemplateId = RowKeyGenUtil.genPassTemplateRowKey(request.getPassTemplate());
        PassTemplate passTemplate;
        try {
             passTemplate = hbaseTemplate.get(
                    HBaseTable.PassTemplateTable.TABLE_NAME,
                    passTemplateId,
                    new PassTemplateRowMapper()
            );
        } catch (Exception ex) {
            log.error("Gain PassTemplate Error: {}", JSON.toJSONString(request.getPassTemplate()));
            return Response.failure("Gain PassTemplate Error!");
        }

        // 判断优惠券是否有剩余
        if (passTemplate.getLimit() <= 0 && passTemplate.getLimit() != -1) {
            log.error("PassTemplate Limit Max: {}", JSON.toJSONString(request.getPassTemplate()));
            return Response.failure("PassTemplate Limit Max!");
        }
        // 判断优惠券是否过期
        Date cur = new Date();
        if (!(cur.getTime() >= passTemplate.getStart().getTime()
                && cur.getTime() < passTemplate.getEnd().getTime())) {
            log.error("PassTemplate ValidTime Error: {}", JSON.toJSONString(request.getPassTemplate()));
            return Response.failure("PassTemplate ValidTime Error!");
        }

        // 优惠券limit减一
        Put put = new Put(Bytes.toBytes(passTemplateId));
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
                Bytes.toBytes(passTemplate.getLimit() - 1)
        );
        hbaseTemplate.saveOrUpdate(HBaseTable.PassTemplateTable.TABLE_NAME, put);

        if (!addPassForUser(request, passTemplate.getId(), passTemplateId)) {
            return Response.failure("GainPassTemplate Failure!");
        }

        return Response.success();
    }

    /**
     * 将领取优惠券的信息加入到 Pass 表
     */
    private boolean addPassForUser(GainPassTemplateRequest request, Integer merchantsId, String passTemplateId) {

        byte[] FAMILY_I = HBaseTable.PassTable.FAMILY_I.getBytes();
        byte[] USER_ID = HBaseTable.PassTable.USER_ID.getBytes();
        byte[] TEMPLATE_ID = HBaseTable.PassTable.TEMPLATE_ID.getBytes();
        byte[] TOKEN = HBaseTable.PassTable.TOKEN.getBytes();
        byte[] ASSIGNED_DATE = HBaseTable.PassTable.ASSIGNED_DATE.getBytes();
        byte[] CON_DATE = HBaseTable.PassTable.CON_DATE.getBytes();

        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(Bytes.toBytes(RowKeyGenUtil.genPassRowKey(request)));

        put.addColumn(FAMILY_I, USER_ID, Bytes.toBytes(request.getUserId()));
        put.addColumn(FAMILY_I, TEMPLATE_ID, Bytes.toBytes(passTemplateId));
        // 判断该 PassTemplate 是否有 token
        if (request.getPassTemplate().getHasToken()) {
            // 在 redis 中获取该 PassTemplate 的 token（key为PassTemplateId）
            String token = redisTemplate.opsForSet().pop(Constants.TOKEN_PREFIX + passTemplateId);
            if (null == token) {
                log.error("Token not exist: {}", passTemplateId);
                return false;
            }
            // 将使用的 token 记录到文件
            if (!recordTokenToFile(merchantsId, passTemplateId, token)) {
                return false;
            }
            put.addColumn(FAMILY_I, TOKEN, Bytes.toBytes(token));
        } else {
            // 如果该 PassTemplate 没 token 就设为 -1
            put.addColumn(FAMILY_I, TOKEN, Bytes.toBytes("-1"));
        }
        put.addColumn(FAMILY_I, ASSIGNED_DATE, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        // 刚领取了肯定还没消费，置为 -1
        put.addColumn(FAMILY_I, CON_DATE, Bytes.toBytes("-1"));

        datas.add(put);
        hbaseTemplate.saveOrUpdates(HBaseTable.PassTable.TABLE_NAME, datas);

        return true;
    }

    /**
     * 将使用过的 token 记录到文件
     */
    private boolean recordTokenToFile(Integer merchantsId, String passTemplateId, String token) {
        try {
            Files.write(
                    // /tmp/token/merchantsId/passTemplateId_
                    Paths.get(Constants.TOKEN_DIR,
                            String.valueOf(merchantsId),
                            passTemplateId  + Constants.USED_TOKEN_SUFFIX),
                    (token + '\n').getBytes(),
                    // 新建、追加
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            log.error("Write token to File error {}", e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Response usePassTemplate(Pass pass) throws Exception {

        Scan scan = new Scan();
        FilterList filterList = new FilterList();

        // 判断是否有当前用户的记录
        byte[] rowPrefiex = Bytes.toBytes(new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString());
        filterList.addFilter(new PrefixFilter(rowPrefiex));

        // 判断当前用户是否有当前要使用的优惠券
        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(HBaseTable.PassTable.FAMILY_I),
                Bytes.toBytes(HBaseTable.PassTable.TEMPLATE_ID),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(pass.getTemplateId())
        ));

        // 判断当前要消费的优惠券是否过期
        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(HBaseTable.PassTable.FAMILY_I),
                Bytes.toBytes(HBaseTable.PassTable.CON_DATE),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));

        scan.setFilter(filterList);

        List<Pass> passes = hbaseTemplate.find(HBaseTable.PassTable.TABLE_NAME, scan, new PassRowMapper());
        if (null == passes || passes.size() != 1) {
            log.error("UserUsePass Error: {}", JSON.toJSONString(pass));
            return Response.failure("UserUsePass Error");
        }

        String token = passes.get(0).getToken();
        // 校验 token 是否合法
        if (!StringUtils.equals(token, "-1")) {
            String passTempalteId = pass.getTemplateId();
            PassTemplate passTemplate = hbaseTemplate.get(HBaseTable.PassTemplateTable.TABLE_NAME, passTempalteId, new PassTemplateRowMapper());
            String merchantId = String.valueOf(passTemplate.getId());
            Path path = Paths.get(Constants.TOKEN_DIR, merchantId, passTempalteId + Constants.USED_TOKEN_SUFFIX);

            Stream<String> lines = Files.lines(path);
            Set<String> userdTokens = lines.collect(Collectors.toSet());
            if (!userdTokens.contains(token)) {
                return Response.failure("token is invalid");
            }
        }


        // 使用优惠券，即将相应记录的过期时间赋值
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTable.FAMILY_I),
                Bytes.toBytes(HBaseTable.PassTable.CON_DATE),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()))
        );

        hbaseTemplate.saveOrUpdate(HBaseTable.PassTable.TABLE_NAME, put);

        return Response.success();
    }
}
