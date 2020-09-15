package com.perfect.managerstarter.starter;

import com.perfect.common.properies.PerfectConfigProperies;
import com.perfect.security.properties.PerfectSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zxh
 */
@SpringBootApplication(
    exclude = { DataSourceAutoConfiguration.class },
    scanBasePackages = {
            "com.perfect.framework",
            "com.perfect.*",
            "com.perfect.common",
            "com.perfect.security",
            "com.perfect.redis",
            "com.perfect.manager.controller",
        })
@EnableTransactionManagement
@EntityScan(basePackages = {"com.perfect.*"})
@Slf4j
@EnableConfigurationProperties({PerfectSecurityProperties.class, PerfectConfigProperies.class})
@EnableCaching
@EnableRabbit
@ServletComponentScan
public class ManagerServerStart {

    public static ConfigurableApplicationContext config;

    public static void main(String[] args) {
        log.info("-----------------------启动开始-------------------------");
        SpringApplication.run(ManagerServerStart.class, args);
        log.info("-----------------------启动完毕-------------------------");
    }
}
