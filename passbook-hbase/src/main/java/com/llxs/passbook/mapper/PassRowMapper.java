package com.llxs.passbook.mapper;

import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.vo.Pass;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 将 HBase 查询结果封装成 Pass
 */
public class PassRowMapper implements RowMapper<Pass> {

    private static byte[] FAMILY_I = HBaseTable.PassTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = HBaseTable.PassTable.USER_ID.getBytes();
    private static byte[] TEMPLATE_ID = HBaseTable.PassTable.TEMPLATE_ID.getBytes();
    private static byte[] TOKEN = HBaseTable.PassTable.TOKEN.getBytes();
    private static byte[] ASSIGNED_DATE = HBaseTable.PassTable.ASSIGNED_DATE.getBytes();
    private static byte[] CON_DATE = HBaseTable.PassTable.CON_DATE.getBytes();

    @Override
    public Pass mapRow(Result result, int rowNum) throws Exception {

        Pass pass = new Pass();

        pass.setUserId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)));
        pass.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        pass.setToken(Bytes.toString(result.getValue(FAMILY_I, TOKEN)));

        // 字符串格式转换（日期）
        String[] patterns = new String[] {"yyyy-DD-dd"};
        pass.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_I, ASSIGNED_DATE)), patterns));

        String conDateStr = Bytes.toString(result.getValue(FAMILY_I, CON_DATE));
        // 当 conDateStr（消费日期）为 -1 表示还未赋值，即还未消费
        if (conDateStr.equals("-1")) {
            pass.setConDate(null);
        } else {
            pass.setConDate(DateUtils.parseDate(conDateStr, patterns));
        }

        pass.setRowKey(Bytes.toString(result.getRow()));

        return pass;
    }
}
