package com.example.handel;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 */

//WebMvcConfigurer 提供很多自定义的拦截器，例如跨域设置、类型转化器等等
@Configuration
public class RegisterInterceptor implements WebMvcConfigurer {

    // 注册Sa-Token的注解拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，注册了才能进行鉴权
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
        //登录拦截器
        registry.addInterceptor(new SaRouteInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/user/doLogin")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-resources")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/webjars/**")
        ;
//        registry.addInterceptor(new SaRouteInterceptor((request, response, handerl) -> {
//            // 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
//        })).addPathPatterns("/**")
//                .excludePathPatterns("/user/doLogin")
//                .excludePathPatterns("/doc.html")
//                .excludePathPatterns("/swagger-resources")
//                .excludePathPatterns("/swagger-resources/**")
//                .excludePathPatterns("/v2/api-docs")
//                .excludePathPatterns("/favicon.ico")
//        ;
    }

    //springboot2.x 静态资源在自定义拦截器之后无法访问的解决方案
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}