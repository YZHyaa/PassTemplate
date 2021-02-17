package com.llxs.passbook.mapper;


import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.vo.Feedback;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 将查询结果封装为 FeedBack
 */
public class FeedbackRowMapper implements RowMapper<Feedback> {

    private static byte[] FAMILY_I = HBaseTable.Feedback.FAMILY_I.getBytes();
    private static byte[] USER_ID = HBaseTable.Feedback.USER_ID.getBytes();
    private static byte[] TYPE = HBaseTable.Feedback.TYPE.getBytes();
    private static byte[] TEMPLATE_ID = HBaseTable.Feedback.TEMPLATE_ID.getBytes();
    private static byte[] COMMENT = HBaseTable.Feedback.COMMENT.getBytes();

    @Override
    public Feedback mapRow(Result result, int rowNum) throws Exception {

        Feedback feedback = new Feedback();

        feedback.setUserId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)));
        feedback.setType(Bytes.toString(result.getValue(FAMILY_I, TYPE)));
        feedback.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        feedback.setComment(Bytes.toString(result.getValue(FAMILY_I, COMMENT)));

        return feedback;
    }
}
