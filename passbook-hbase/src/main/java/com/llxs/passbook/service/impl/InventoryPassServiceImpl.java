package com.llxs.passbook.service.impl;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.dao.MerchantsRedisDao;
import com.llxs.passbook.entity.Merchants;
import com.llxs.passbook.mapper.PassTemplateRowMapper;
import com.llxs.passbook.service.InventoryPassService;
import com.llxs.passbook.service.UserPassService;
import com.llxs.passbook.utils.RowKeyGenUtil;
import com.llxs.passbook.vo.*;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryPassServiceImpl implements InventoryPassService {

    @Autowired
    private UserPassService userPassService;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsRedisDao merchantsDao;

    @Override
    public Response getInventoryInfo(Long userId) throws Exception {

        // 排除用户领取过的优惠券
        List<PassInfo> userAllPassInfo = (List<PassInfo>) userPassService.getUserAllPassInfo(userId).getData();
        List<String> excludePassTemplateRowKey = userAllPassInfo.stream().map(
                t -> {
                    PassTemplate passTemplate = t.getPassTemplate();
                    String rowKey = RowKeyGenUtil.genPassTemplateRowKey(passTemplate);
                    return rowKey;
                }
        ).collect(Collectors.toList());

        List<PassTemplate> availablePassTemplate = getAvailablePassTemplate(excludePassTemplateRowKey);
        List<PassTemplateInfo> passTemplateInfos = buildPassTemplateInfo(availablePassTemplate);
        return new Response(new InventoryResponse(userId, passTemplateInfos));
    }

    /**
     * 获取用户可见库存优惠券
     * 用户库存 = 全部 - 未发完 - 有效期内 - 用户已有
     */
    private List<PassTemplate> getAvailablePassTemplate(List<String> excludePassTemplateRowKey) {

        Scan scan = new Scan();
        // 根据发放量过滤优惠券
        // 只需满足一个过滤器即可
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        // 1.没分发完
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(0L)
                )
        );
        // 2.无限量
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.EQUAL,
                        Bytes.toBytes("-1")
                )
        );
        scan.setFilter(filterList);
        List<PassTemplate> validTemplate =
                hbaseTemplate.find(HBaseTable.PassTemplateTable.TABLE_NAME, scan, new PassTemplateRowMapper());

        List<PassTemplate> availablePassTemplates = new ArrayList<>();
        for (PassTemplate passTemplate : validTemplate) {

            // 根据 RowKey 过滤用户已有优惠券
            if (excludePassTemplateRowKey.contains(RowKeyGenUtil.genPassTemplateRowKey(passTemplate))) {
                continue;
            }

            // 根据有效期过滤优惠券
            if (new Date().getTime() >= passTemplate.getStart().getTime()
                        && new Date().getTime() <= passTemplate.getEnd().getTime()) {
                availablePassTemplates.add(passTemplate);
            }
        }

        return availablePassTemplates;
    }

    /**
     * 根据可用 PassTemplate 构造 PassTemplateInfo {@link PassTemplateInfo}
     */
    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> availablePassTemplates) {

        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = availablePassTemplates.stream().map(
                t -> t.getId()
        ).collect(Collectors.toList());

        List<Merchants> merchants = merchantsDao.findMerchantsByIdIn(merchantsIds);
        merchants.forEach( m -> merchantsMap.put(m.getCid(), m));

        List<PassTemplateInfo> result = new ArrayList<>(availablePassTemplates.size());
        for (PassTemplate passTemplate : availablePassTemplates) {

            Merchants mc = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (null == mc) {
                log.error("Merchants Error: {}", passTemplate.getId());
                continue;
            }

            result.add(new PassTemplateInfo(passTemplate, mc));
        }

        return result;
    }
}
