package com.perfect.mq.rabbitmq.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.perfect.bean.entity.log.mq.SLogMqEntity;
import com.perfect.bean.pojo.mqsender.MqSenderPojo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.utils.redis.RedisUtil;
import com.perfect.common.utils.string.convert.Convert;
import com.perfect.core.service.log.mq.ISLogMqService;
import com.perfect.mq.rabbitmq.enums.MQEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

/**
 * 生产者
 *
 * @author zxh
 * @date 2019年 10月14日 21:46:52
 */
@Component
@Slf4j
public class PerfectMqProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    ISLogMqService service;

    /**
     * 考虑数据放到redis中，然后需要回调则考虑回调，没有就没有
     *
     * CorrelationData的id，使用redis的key
     *
     */

    @Autowired
    @Qualifier("perfect_RabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 消息发送
     * @param mqSenderPojo
     * @param mqenum
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(MqSenderPojo mqSenderPojo, MQEnum mqenum) {
        String messageDataJson = JSON.toJSONString(mqSenderPojo);

        /**
         * 数据库保存
         */
        insertToDbService(mqSenderPojo, mqenum, messageDataJson);

        /**
         * 保存mqSenderPojo到redis，key为mqSenderPojo.getKey()
         */
        redisUtil.putToMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, mqSenderPojo.getKey(), messageDataJson);

        /**
         * 封装消息
         */
        Message message =
            MessageBuilder.withBody(messageDataJson.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(mqSenderPojo.getKey()).build();
        /**
         * 确认消息是否到达broker服务器
         */
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

        CorrelationData correlationData = new CorrelationData(mqSenderPojo.getKey());
        rabbitTemplate.setExchange(mqenum.getExchange());
        rabbitTemplate.convertAndSend(mqenum.getExchange(), mqenum.getRouting_key(), message, correlationData);
    }

    /**
     * 建立消息队列entity_bean
     * @param mqSenderPojo
     */
    private SLogMqEntity buildEntityBean(MqSenderPojo mqSenderPojo, MQEnum mqenum, String data){
        SLogMqEntity sLogMqEntity = new SLogMqEntity();
        sLogMqEntity.setCode(mqSenderPojo.getType());
        sLogMqEntity.setName(mqSenderPojo.getName());
        sLogMqEntity.setExchange(mqenum.getExchange());
        sLogMqEntity.setRouting_key(mqenum.getRouting_key());
        sLogMqEntity.setMq_data(data);
        sLogMqEntity.setConstruct_id(mqSenderPojo.getKey());
        sLogMqEntity.setProducer_status(true);
        sLogMqEntity.setConsumer_status(false);
        return sLogMqEntity;
    }

    /**
     * 执行保存操作
     * @param mqSenderPojo
     * @param mqenum
     * @param data
     */
    private void insertToDbService (MqSenderPojo mqSenderPojo, MQEnum mqenum, String data) {
        SLogMqEntity sLogMqEntity = buildEntityBean(mqSenderPojo, mqenum, data);
        service.insert(sLogMqEntity);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String jsonString = correlationData.getId();
        System.out.println("消息id:" + correlationData.getId());
        if(correlationData != null) {
            // 处理返回
            if (ack) {
                log.info("------使用MQ消息确认：消息发送成功----");
                //                Object redisRtn = redisUtil.getFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, correlationData.getId());
                // 删除redis
                redisUtil.removeFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, correlationData.getId());
            } else {
                log.error("------使用MQ消息确认：传送失败----");
                Object redisRtn = redisUtil.getFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, correlationData.getId());
                redisUtil.putToMap(PerfectConstant.REDIS_PREFIX.MQ_CONSUME_FAILT_PREFIX, correlationData.getId(), redisRtn);
                redisUtil.removeFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, correlationData.getId());
            }
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String messageData = Convert.str(message.getBody(), (Charset)null);
        MqSenderPojo mqSenderPojo = JSONObject.parseObject(messageData, MqSenderPojo.class);

        Object redisRtn = redisUtil.getFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, mqSenderPojo.getKey());
        redisUtil.putToMap(PerfectConstant.REDIS_PREFIX.MQ_CONSUME_RETURN_PREFIX, mqSenderPojo.getKey(), redisRtn);
        redisUtil.removeFromMap(PerfectConstant.REDIS_PREFIX.MQ_SEND_PREFIX, mqSenderPojo.getKey());
        System.out.println("消息被退回");
        System.out.println("被退回的消息是 :" + messageData);
        System.out.println("被退回的消息编码是 :" + replyCode);
    }
}
