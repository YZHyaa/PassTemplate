package com.llxs.passbook.service.impl;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.constant.PassStatus;
import com.llxs.passbook.dao.MerchantsRedisDao;
import com.llxs.passbook.entity.Merchants;
import com.llxs.passbook.mapper.PassRowMapper;
import com.llxs.passbook.service.UserPassService;
import com.llxs.passbook.vo.Pass;
import com.llxs.passbook.vo.PassInfo;
import com.llxs.passbook.vo.PassTemplate;
import com.llxs.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPassServiceImpl implements UserPassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsRedisDao merchantsDao;

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.ALL);
    }

    /**
     * 根据优惠券状态获取优惠券信息
     */
    private Response getPassInfoByStatus(Long userId, PassStatus status) throws Exception {

        Scan scan = new Scan();
        FilterList filterList = new FilterList();

        // 根据 userId 构造行键前缀过滤器
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());
        filterList.addFilter(new PrefixFilter(rowPrefix));

        // 根据优惠券状态构建列值过滤器
        if (status != PassStatus.ALL) {
            CompareFilter.CompareOp compareOp =
                    status == PassStatus.UNUSED ?
                            CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

            filterList.addFilter(new SingleColumnValueFilter(
                    HBaseTable.PassTable.FAMILY_I.getBytes(),
                    HBaseTable.PassTable.CON_DATE.getBytes(), compareOp,
                    Bytes.toBytes("-1")
            ));
        }

        scan.setFilter(filterList);

        List<Pass> passes = hbaseTemplate.find(HBaseTable.PassTable.TABLE_NAME, scan, new PassRowMapper());

        // 根据 Pass 获取所有 PassTemplate
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        // 根据 PassTemplate 获取所有商户 Merchant
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(new ArrayList<>(passTemplateMap.values()));


        List<PassInfo> result = new ArrayList<>();
        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);
            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchants = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getId());
                continue;
            }

            // 将查询结果封装成 PassInfo
            result.add(new PassInfo(pass, passTemplate, merchants));
        }

        return new Response(result);
    }

    /**
     * 通过获取的 Passes 对象构造 Map，即根据每条 Pass 信息中包含的 PassTemplateId 去获取 PassTemplate
     * @param passes {@link Pass}
     * @return Map <RowKey, PassTemplate> {@link PassTemplate}
     * */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws Exception {

        String[] patterns = new String[] {"yyyy-MM-dd"};

        byte[] FAMILY_B = Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(HBaseTable.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(HBaseTable.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(HBaseTable.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(HBaseTable.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(HBaseTable.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(HBaseTable.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(HBaseTable.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(HBaseTable.PassTemplateTable.END);


        List<String> templateIds = passes.stream().map(
                p -> p.getRowKey()
        ).collect(Collectors.toList());

        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));

        // 根据所有的 Get 去查询指定的 PassTemplate
        Result[] templateResults = hbaseTemplate.getConnection()
                .getTable(TableName.valueOf(HBaseTable.PassTemplateTable.TABLE_NAME))
                .get(templateGets);


        Map<String, PassTemplate> templateMap = new HashMap<>();
        for (Result item : templateResults) {
            PassTemplate passTemplate = new PassTemplate();

            passTemplate.setId(Bytes.toInt(item.getValue(FAMILY_B, ID)));
            passTemplate.setTitle(Bytes.toString(item.getValue(FAMILY_B, TITLE)));
            passTemplate.setSummary(Bytes.toString(item.getValue(FAMILY_B, SUMMARY)));
            passTemplate.setDesc(Bytes.toString(item.getValue(FAMILY_B, DESC)));
            passTemplate.setHasToken(Bytes.toBoolean(item.getValue(FAMILY_B, HAS_TOKEN)));
            passTemplate.setBackground(Bytes.toInt(item.getValue(FAMILY_B, BACKGROUND)));

            passTemplate.setLimit(Bytes.toLong(item.getValue(FAMILY_C, LIMIT)));
            passTemplate.setStart(DateUtils.parseDate(
                    Bytes.toString(item.getValue(FAMILY_C, START)), patterns));
            passTemplate.setEnd(DateUtils.parseDate(
                    Bytes.toString(item.getValue(FAMILY_C, END)), patterns
            ));

            // <RowKey, PassTemplate>
            templateMap.put(Bytes.toString(item.getRow()), passTemplate);
        }

        return templateMap;
    }

    /**
     * 通过获取的 PassTemplate 对象构造 Merchants Map
     * @param passTemplates {@link PassTemplate}
     * @return <id, Merchants> {@link Merchants}
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplate> passTemplates) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = passTemplates.stream().map(
                PassTemplate::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findMerchantsByIdIn(merchantsIds);

        // <id, Merchants>
        merchants.forEach(m -> merchantsMap.put(m.getCid(), m));

        return merchantsMap;
    }

}
