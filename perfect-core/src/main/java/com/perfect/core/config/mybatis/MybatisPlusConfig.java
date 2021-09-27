package com.perfect.core.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.perfect.core.config.mybatis.plugin.datascope.DataScopeInterceptor;
import com.perfect.core.config.mybatis.plugin.autofill.MyBatisAutoFillHandel;
import com.perfect.core.config.mybatis.sqlinjector.PerfectSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zxh
 */
@Component
@Slf4j
@Configuration
@MapperScan("com.perfect.core.mapper")
public class MybatisPlusConfig  {

    /**
     * 乐观锁
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

    /**
     * 通用字段补全
     */
    @Bean
    public MetaObjectHandler commonFieldFillHandler() {
        return new MyBatisAutoFillHandel();
    }

    /**
     * 数据范围mybatis插件,数据权限
     */
    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public PerfectSqlInjector perfectSqlInjector() {
        return new PerfectSqlInjector();
    }
}
