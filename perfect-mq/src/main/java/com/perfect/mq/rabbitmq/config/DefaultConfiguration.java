//package com.perfect.mq.rabbitmq.config;
//
//import com.perfect.mq.rabbitmq.callback.config.DefaultConfirmCallback;
//import com.perfect.mq.rabbitmq.callback.config.DefaultReutrnedCallback;
//import com.perfect.mq.rabbitmq.callback.config.IConfirmCallback;
//import com.perfect.mq.rabbitmq.callback.config.IReutrnedCallback;
//import com.perfect.mq.rabbitmq.listener.DefaultConnectionListener;
//import com.perfect.mq.rabbitmq.listener.DefaultConsumerListener;
//import com.perfect.mq.rabbitmq.listener.IChannelAwareMessageListener;
//import com.perfect.mq.rabbitmq.listener.IConnectionListener;
//import com.perfect.mq.rabbitmq.properties.MQProperties;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 默认基础配置
// *
// */
//@Configuration
//@EnableConfigurationProperties(MQProperties.class)
//public class DefaultConfiguration {
//
//    /**
//     * 默认确认回调接口实现
//     *
//     * @return DefaultConfirmCallback
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public IConfirmCallback iConfirmCallback() {
//        return new DefaultConfirmCallback();
//    }
//
//    /**
//     * 默认失败回调接口实现
//     *
//     * @return DefaultReutrnedCallback
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public IReutrnedCallback iReutrnedCallback() {
//        return new DefaultReutrnedCallback();
//    }
//
//    /**
//     * 默认MQ连接监听实现
//     *
//     * @return DefaultConnectionListener
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public IConnectionListener iConnectionListener() {
//        return new DefaultConnectionListener();
//    }
//
//    /**
//     * 默认回调接口监听实现
//     *
//     * @return DefaultConsumerListener
//     */
////    @Bean
////    @ConditionalOnMissingBean
////    public IChannelAwareMessageListener iChannelAwareMessageListener() {
////        return new DefaultConsumerListener();
////    }
//
////    /**
////     * 默认转发接口实现
////     *
////     * @return DefaultForwardService
////     */
////    @Bean
////    @ConditionalOnMissingBean
////    public IForwardService iForwardService() {
////        return new DefaultForwardService();
////    }
//}
