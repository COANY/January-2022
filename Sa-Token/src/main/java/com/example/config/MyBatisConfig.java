//package com.example.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//
//@SpringBootConfiguration
//@EnableTransactionManagement //开启事务
//@MapperScan("com.example.dao")
//public class MyBatisConfig {
//
//    /**
//     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
//     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        //向Mybatis过滤器链中添加分页拦截器
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        //还可以添加i他的拦截器
//        return interceptor;
////        ----------------------------------------------------------------
////        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
////            @Override
////            public Expression getTenantId() {
////                return new StringValue(TenantRequestContext.getTenantLocal());
////            }
////
////            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
////            @Override
////            public boolean ignoreTable(String tableName) {
////                return !"la_zf_org".equalsIgnoreCase(tableName);
////            }
////
////            // 数据表中对应的租户字段，这里是默认字段
////            @Override
////            public String getTenantIdColumn() {
////                return "user_id";
////            }
////        }));
////        //还可以添加i他的拦截器
////        return interceptor;
//    }
//}