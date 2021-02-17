package com.llxs.passbook.passbook.config.security;

import com.llxs.passbook.passbook.config.prop.JwtProperties;
import com.llxs.passbook.passbook.constant.Constants;
import com.llxs.passbook.passbook.utils.CookieUtils;
import com.llxs.passbook.passbook.utils.JwtUtils;
import com.llxs.passbook.passbook.vo.TokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JwtProperties jwtProperties; // 获取 cookie 信息

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // 获取 cookie 中的 token
        String cookieValue = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        // 如果请求中没有 token
        if(StringUtils.isBlank(cookieValue)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 校验 token 合法性，是否包含了 user 信息
        TokenInfo info = JwtUtils.getInfoFromToken(cookieValue,jwtProperties.getPublicKey());
        if(info == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 将 token 中保存的当前 user 的 id 放入 ThreadLocal
        AccessContext.setTokenInfo(info);

        return true;
    }

    /**
     * 在请求处理完后删除 ThreadLocal 中保存的 id
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AccessContext.clearAccessKey();
    }
}
