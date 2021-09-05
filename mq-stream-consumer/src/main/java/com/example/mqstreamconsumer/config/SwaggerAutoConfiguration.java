package com.example.mqstreamconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

@Slf4j
@Configuration
@Import({Swagger2DocumentationConfiguration.class})
public class SwaggerAutoConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example")).paths(PathSelectors.any())
                .build();
    }
    // 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("MQ")
                // 创建人信息
                .contact(new Contact("HuangJF",  "",  ""))
                // 版本号
                .version("1.0")
                // 描述
                .description("API 描述")
                .build();
    }

}
