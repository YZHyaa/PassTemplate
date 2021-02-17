package com.llxs.passbook.passbook.constant;

public class Constants {

    /** 商户优惠券 Kafka Topic */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /** 商户信息的 redis key 前缀 */
    public static final String MERCHANTS_PREFIX = "merchants:";

    /** 优惠券 token 的 redis key 前缀 */
    public static final String TOKEN_PREFIX = "token:";

    /** token 文件存储目录 */
    public static final String TOKEN_DIR = "/tmp/token/";

    /** 已使用的 token 文件名后缀 */
    public static final String USED_TOKEN_SUFFIX = "_";

    /** 当前是 user */
    public static final int USER = 1;
    /** 当前是 merchants */
    public static final int MERCHATNS = 2;
}
