package com.example.chatadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {
    /**
     * 创建Docket类型的对象。并使用spring容器管理。
     * DOcket是swagger中的全局配置对象。
     *
     * @return
     */

    @Bean
    public Docket docket() {
        // 设置要显示swagger的环境
        // 通过 enable() 接收此参数判断是否要显示
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 配置是否启用Swagger，如果是false，在浏览器将无法访问
                //.enable(false)
                // 通过.select()方法，去配置扫描接口
                .select()
                // RequestHandlerSelectors配置如何扫描接口
                .apis(RequestHandlerSelectors.
                        // basePackage 指定要扫描的包
                        // any():扫描全部
                        // none():不扫描
                        // withClassAnnotation:扫描类上的注解，多数是一个注解的反射对象
                        // withMethodAnnotation:扫描方法上的注解
                                basePackage("com.example.chatuser.controller"))
                // 配置 你想在哪个controller层生产接口文档
                //.paths(PathSelectors.ant("/search/**"))
                // 配置如何通过path过滤,即这里只扫描请求以/kuang开头的接口
                .build();
    }

    // 配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("wuzhenyu", "https://www.baidu.com", "zhenYu-wu@163.com");
        return new ApiInfo(
                // 标题
                "chat项目",
                // 描述
                "测试SwaggerAPI可用性",
                // 版本
                "v1.0",
                // 组织链接
                "https://www.baidu.com",
                // 联系人信息
                contact,
                // 许可
                "Apache 2.0 许可",
                // 许可连接
                "许可链接",
                // 扩展
                new ArrayList<>()
        );
    }
}
