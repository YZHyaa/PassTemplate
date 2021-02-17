package com.llxs.passbook.service.impl;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.service.HBasePassService;
import com.llxs.passbook.utils.RowKeyGenUtil;
import com.llxs.passbook.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class HBasePassServiceImpl implements HBasePassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public boolean dropPassTemplateToHBase(PassTemplate passTemplate) {

        if (passTemplate == null) {
            return false;
        }

        String rowKey = RowKeyGenUtil.genPassTemplateRowKey(passTemplate);

        try {
            if (hbaseTemplate.getConnection().getTable(TableName.valueOf(HBaseTable.PassTemplateTable.TABLE_NAME))
                    .exists(new Get(Bytes.toBytes(rowKey)))) {
                log.warn("RowKey {} is already exist!", rowKey);
                return false;
            }
        } catch (IOException e) {
            log.error("DropPassTemplate Error: {}", e.getMessage());
            return false;
        }

        Put put = new Put(Bytes.toBytes(rowKey));

        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.ID),
                Bytes.toBytes(passTemplate.getId())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.TITLE),
                Bytes.toBytes(passTemplate.getTitle())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.SUMMARY),
                Bytes.toBytes(passTemplate.getSummary())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.DESC),
                Bytes.toBytes(passTemplate.getDesc())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.HAS_TOKEN),
                Bytes.toBytes(passTemplate.getHasToken())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(HBaseTable.PassTemplateTable.BACKGROUND),
                Bytes.toBytes(passTemplate.getBackground())
        );

        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(HBaseTable.PassTemplateTable.LIMIT),
                Bytes.toBytes(passTemplate.getLimit())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(HBaseTable.PassTemplateTable.START),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplate.getStart()))
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(HBaseTable.PassTemplateTable.END),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplate.getEnd()))
        );

        hbaseTemplate.saveOrUpdate(HBaseTable.UserTable.TABLE_NAME, put);

        return true;
    }
}
