package com.llxs.passbook.merchants.config;

import com.llxs.passbook.merchants.config.security.AuthCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthCheckInterceptor authCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        registry.addInterceptor(authCheckInterceptor).addPathPatterns("/merchants/**");
    }
}
