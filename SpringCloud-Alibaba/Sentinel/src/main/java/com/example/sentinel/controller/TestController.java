package com.example.sentinel.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RefreshScope//配置自动更新
public class TestController {



    @GetMapping(value = "/test1")
    public String echo1() {
        return "123";
    }
    @GetMapping(value = "/test2")
    public String echo2() {
        return "123";
    }


}
