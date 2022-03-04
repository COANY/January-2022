package com.example.sentinel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;


@Data
@SpringBootConfiguration
public class SwaggerProperty {
    /**
     * 需要生成api文档的 类
     */
    @Value("${swagger.basePackage}")
    private String basePackage;
    /**
     * api文档 标题
     */
    @Value("${swagger.title}")
    private String title;
    /**
     * api文档 描述
     */
    @Value("${swagger.description}")
    private String description;
    /**
     * api文档 版本
     */
    @Value("${swagger.version}")
    private String version;
    /**
     * api  文档作者
     */
    @Value("${swagger.author}")
    private String author;
    /**
     * api 文档作者网站
     */
    @Value("${swagger.url}")
    private String url;
    /**
     * api文档作者邮箱
     */
    @Value("${swagger.email}")
    private String email;
    /**
     * api 文档 认证协议
     */
//    @Value("${swagger.license}")
//    private String license;
    /**
     * api 文档 认证 地址
     */
//    @Value("${swagger.licenseUrl}")
//    private String licenseUrl;
}
