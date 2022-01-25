package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vicnent Lee
 * @create 2021-05-14 15:47
 * @desc Swagger配置
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Resource
    private SwaggerProperty swaggerProperty;


    /**
     * 构造api 文档
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperty))  //文档信息
                .select()
                //文档扫描
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))  //有ApiOperation注解的controller都加入api文档
                .apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage())) //包模式
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(parameterList());
    }

    private ApiInfo apiInfo(SwaggerProperty swagger) {
        return new ApiInfoBuilder()
                //标题
                .title(swagger.getTitle())
                //描述
                .description(swagger.getDescription())
                //创建联系人信息 （作者，邮箱，网站）
                .contact(new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()))
                //版本
                .version(swagger.getVersion())
//                //认证
//                .license(swagger.getLicense())
//                //认证协议
//                .licenseUrl(swagger.getLicenseUrl())
                .build();
    }

    /**
     * 全局response 返回信息
     * @return
     */
/*
    private List<Response> responseList() {
        List<Response> responseList = new ArrayList<>();
        Arrays.stream(ResultCodeEnum.values()).forEach(errorEnum -> {
            responseList.add(
                    new ResponseBuilder().code(errorEnum.getCode().toString()).description(errorEnum.getMessage()).build()
            );
        });
        return responseList;
    }
*/

    private List<Parameter> parameterList(){
        //添加全局请求头
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
                new ParameterBuilder().name("Access-Token")
                        .description("token")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(true)
                        .build()
        );

        return parameters;
    }
}
