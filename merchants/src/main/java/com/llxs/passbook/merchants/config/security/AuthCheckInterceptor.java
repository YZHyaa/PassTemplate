package com.llxs.passbook.merchants.config.security;

import com.llxs.passbook.merchants.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 */
@Component
public class AuthCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token =request.getHeader(Constants.TOKEN_STRING);

        if (StringUtils.isEmpty(token)) {
            throw new Exception("Header 中缺少 " + Constants.TOKEN_STRING + "!");
        }

        if (!token.equals(Constants.TOKEN)) {
            throw new Exception("Header 中 " + Constants.TOKEN_STRING + "错误!");
        }

        AccessContext.setToken(token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessContext.clearAccessKey();
    }
}
