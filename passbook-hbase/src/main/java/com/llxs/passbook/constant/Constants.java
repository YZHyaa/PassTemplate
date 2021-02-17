package com.llxs.passbook.constant;

public class Constants {

    /** 用户数的 redis key */
    public static final String USE_COUNT_REDIS_KEY = "imooc-user-count";

    /** 商户优惠券 Kafka Topic */
    public static final String TEMPLATE_TOPIC = "merchants-templates";

    /** token 文件存储目录 */
    public static final String TOKEN_DIR = "/tmp/token/";

    /** 已使用的 token 文件名后缀 */
    public static final String USED_TOKEN_SUFFIX = "_";

    /** token 的 redis key 前缀 */
    public static final String TOKEN_PREFIX = "token:";

    /** 商户信息的 redis key 前缀 */
    public static final String MERCHANTS_PREFIX = "merchants:";

}
