package com.llxs.passbook.merchants.constant;

/**
 * 通用常量
 * */
public class Constants {

    /** 商户优惠券投放的 Kafka Topic */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /** token String */
    public static final String TOKEN_STRING = "token";

    /** token info */
    public static final String TOKEN = "llxs-passbook-merchants";

    /** 优惠券 token 文件存储目录 */
    public static final String TOKEN_DIR = "/tmp/token/";

    /** Redis 中保存 token 的 key 前缀 */
    public static final String TOKEN_PREFIX = "token:";

    /** Redis 中保存商户信息的 key 前缀 */
    public static final String MERCHANTS_PREFIX = "merchants:";

}
