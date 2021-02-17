package com.llxs.passbook.utils;

import com.llxs.passbook.vo.Feedback;
import com.llxs.passbook.vo.GainPassTemplateRequest;
import com.llxs.passbook.vo.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

@Slf4j
public class RowKeyGenUtil {

    /**
     * 生成 userId（RowKey)
     * @param prefix 当前用户数
     * @return 用户 id
     */
    public static Long genUserId(Long prefix) {

        // 生成五位的随机序列
        String suffix = RandomStringUtils.randomNumeric(5);
        // RowKey = 当前用户数 + 随机序列
        // 将 RowKey 转为 long
        return Long.valueOf(prefix + suffix);
    }

    /**
     * 生成 PassTemplate 的 RowKey
     * RowKey = md5(userId_title)
     */
    public static String genPassTemplateRowKey(PassTemplate passTemplate) {

        String passInfo = String.valueOf(passTemplate.getId()) + "_" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("GenPassTemplateRowKey: {}, {}", passInfo, rowKey);

        return rowKey;
    }

    /**
     * 生成 Pass 的 RowKey
     * RowKey = reversed(userId) + inverse(timestamp) + PassTemplate RowKey
     */
    public static String genPassRowKey(GainPassTemplateRequest request) {

        return new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis())
                + genPassTemplateRowKey(request.getPassTemplate());
    }

    /**
     * 生成 Feedback 的 RowKey
     * Rowkey = reversed(userId) + inverse(timestamp)
     */
    public static String genFeedbackRowKey(Feedback feedback) {

        return new StringBuilder(String.valueOf(feedback.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis());
    }
}
