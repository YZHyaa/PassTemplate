package com.llxs.passbook.passbook.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/passbook/passtemplateList/**");
    }
}
