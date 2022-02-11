package com.example.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RefreshScope//配置自动更新
public class TestController {

    @Value("${config}")
    private String URL;

    @GetMapping(value = "/test")
    public String echo() {
        return URL;
    }

}
