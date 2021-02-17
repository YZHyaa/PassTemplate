package com.llxs.passbook.passbook.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtils {

    private static String domain = "localhost";

    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param cookieName cookie名
     * @param cookieValue cookie 要保存的值
     * @param cookieMaxAge cookie 的存活时间
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge){
        setCookie(request,response,cookieName,cookieValue,cookieMaxAge,domain,null,null);
    }

    private static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge, String domain, String encodeString, Boolean httpOnly) {
        try {
            if(StringUtils.isBlank(encodeString)) {
                encodeString = "utf-8";
            }

            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge != null && cookieMaxAge > 0)
                cookie.setMaxAge(cookieMaxAge);
            if (null != request) // 设置域名的cookie
                cookie.setDomain(domain);
            cookie.setPath("/");

            if(httpOnly != null) {
                cookie.setHttpOnly(httpOnly);
            }
            // 发送 cookie 到客户端
            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error("Cookie Encode Error.", e);
        }
    }

    /**
     * 得到Cookie的值, 不编码
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null){
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Cookie Decode Error.", e);
        }
        return retValue;
    }
}
