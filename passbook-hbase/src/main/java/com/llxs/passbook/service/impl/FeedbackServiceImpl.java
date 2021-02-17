package com.llxs.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.llxs.passbook.constant.HBaseTable;
import com.llxs.passbook.mapper.FeedbackRowMapper;
import com.llxs.passbook.service.FeedbackService;
import com.llxs.passbook.utils.RowKeyGenUtil;
import com.llxs.passbook.vo.Feedback;
import com.llxs.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public Response createFeedback(Feedback feedback) {

        if (!feedback.validate()) {
            log.error("Feedback Error {}", JSON.toJSONString(feedback));
            return Response.failure("Feedback Error");
        }

        Put put = new Put(Bytes.toBytes(RowKeyGenUtil.genFeedbackRowKey(feedback)));

        put.addColumn(
                Bytes.toBytes(HBaseTable.Feedback.FAMILY_I),
                Bytes.toBytes(HBaseTable.Feedback.USER_ID),
                Bytes.toBytes(feedback.getUserId())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.Feedback.FAMILY_I),
                Bytes.toBytes(HBaseTable.Feedback.TYPE),
                Bytes.toBytes(feedback.getType())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.Feedback.FAMILY_I),
                Bytes.toBytes(HBaseTable.Feedback.TEMPLATE_ID),
                Bytes.toBytes(feedback.getTemplateId())
        );
        put.addColumn(
                Bytes.toBytes(HBaseTable.Feedback.FAMILY_I),
                Bytes.toBytes(HBaseTable.Feedback.COMMENT),
                Bytes.toBytes(feedback.getComment())
        );

        hbaseTemplate.saveOrUpdate(HBaseTable.Feedback.TABLE_NAME, put);

        return Response.success();
    }

    @Override
    public Response getFeedback(Long userId) {

        byte[] rowKeyPrefix = new StringBuilder(String.valueOf(userId)).reverse().toString().getBytes();
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(rowKeyPrefix));

        List<Feedback> feedbacks = hbaseTemplate.find(HBaseTable.Feedback.TABLE_NAME, scan, new FeedbackRowMapper());

        return new Response(feedbacks);
    }
}
