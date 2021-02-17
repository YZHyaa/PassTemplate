package com.llxs.passbook.merchants.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .useDefaultResponseMessages(false)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.llxs.passbook.merchants.controller"))
                    .paths(PathSelectors.any())
                    .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("商户投放子系统")
                .description("商户投放子系统接口文档")
                .version("v1.0")
                .termsOfServiceUrl("http://llxs.org")
                .contact(new Contact("yzh", "https://blog.csdn.net/weixin_43935927", "1327584236@qq.com"))
                .build();
    }
}
