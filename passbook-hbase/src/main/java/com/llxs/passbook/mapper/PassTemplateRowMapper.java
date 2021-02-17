package com.llxs.passbook.mapper;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.vo.PassTemplate;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 将 HBase 查询结果封装成 PassTempate
 */
public class PassTemplateRowMapper implements RowMapper<PassTemplate> {

    private static byte[] FAMILY_B = HBaseTable.PassTemplateTable.FAMILY_B.getBytes();
    private static byte[] ID = HBaseTable.PassTemplateTable.ID.getBytes();
    private static byte[] TITLE = HBaseTable.PassTemplateTable.TITLE.getBytes();
    private static byte[] SUMMARY = HBaseTable.PassTemplateTable.SUMMARY.getBytes();
    private static byte[] DESC = HBaseTable.PassTemplateTable.DESC.getBytes();
    private static byte[] HAS_TOKEN = HBaseTable.PassTemplateTable.HAS_TOKEN.getBytes();
    private static byte[] BACKGROUND = HBaseTable.PassTemplateTable.BACKGROUND.getBytes();

    private static byte[] FAMILY_C = HBaseTable.PassTemplateTable.FAMILY_C.getBytes();
    private static byte[] LIMIT = HBaseTable.PassTemplateTable.LIMIT.getBytes();
    private static byte[] START = HBaseTable.PassTemplateTable.START.getBytes();
    private static byte[] END = HBaseTable.PassTemplateTable.END.getBytes();

    @Override
    public PassTemplate mapRow(Result result, int rowNum) throws Exception {

        PassTemplate passTemplate = new PassTemplate();

        passTemplate.setId(Bytes.toInt(result.getValue(FAMILY_B, ID)));
        passTemplate.setTitle(Bytes.toString(result.getValue(FAMILY_B, TITLE)));
        passTemplate.setSummary(Bytes.toString(result.getValue(FAMILY_B, SUMMARY)));
        passTemplate.setDesc(Bytes.toString(result.getValue(FAMILY_B, DESC)));
        passTemplate.setHasToken(Bytes.toBoolean(result.getValue(FAMILY_B, HAS_TOKEN)));
        passTemplate.setBackground(Bytes.toInt(result.getValue(FAMILY_B, BACKGROUND)));

        // 下面类型时间类型转化会用到
        String[] patterns = new String[] {"yyyy-MM-dd"};

        passTemplate.setLimit(Bytes.toLong(result.getValue(FAMILY_C, LIMIT)));
        passTemplate.setStart(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, START)), patterns));
        passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, END)), patterns));

        return passTemplate;
    }
}
