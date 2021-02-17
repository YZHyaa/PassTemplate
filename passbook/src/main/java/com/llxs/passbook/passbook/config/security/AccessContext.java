package com.llxs.passbook.passbook.config.security;

import com.llxs.passbook.passbook.constant.Constants;
import com.llxs.passbook.passbook.vo.TokenInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessContext {

    private static final ThreadLocal<TokenInfo> token = new ThreadLocal<>();

    public static void setTokenInfo(TokenInfo tokenInfo) {
        token.set(tokenInfo);
    }

    public static long getUserId() {

        if (token.get().getType() == Constants.USER) {
            return token.get().getId();
        } else {
            log.error("token error");
            return -1;
        }
    }

    public static int getMerchantsId() {
        if (token.get().getType() == Constants.MERCHATNS) {
            return (int) token.get().getId();
        } else {
            log.error("token error");
            return -1;
        }
    }

    public static void clearAccessKey() {
        token.remove();
    }
}
