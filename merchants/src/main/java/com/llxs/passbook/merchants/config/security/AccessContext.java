package com.llxs.passbook.merchants.config.security;

/**
 * 用 ThreadLocal 存储每一个线程携带的 Token
 */
public class AccessContext {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String tokenStr) {
        token.set(tokenStr);
    }

    public static void clearAccessKey() {
        token.remove();
    }
}
