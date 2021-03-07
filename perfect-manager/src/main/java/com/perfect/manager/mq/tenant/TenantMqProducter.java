package com.perfect.manager.mq.tenant;

import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.pojo.mqsender.MqSenderPojo;
import com.perfect.bean.pojo.mqsender.builder.MqSenderPojoBuilder;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import com.perfect.common.enums.MqSenderEnum;
import com.perfect.common.enums.quartz.QuartzEnum;
import com.perfect.core.service.quartz.ISJobService;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import com.perfect.manager.quartz.builder.sys.config.tenant.TenantTaskBuilder;
import com.perfect.mq.rabbitmq.enums.MQEnum;
import com.perfect.mq.rabbitmq.producer.PerfectMqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: TenantMq
 * @Description: 租户的mq
 * @Author: zxh
 * @date: 2019/12/16
 * @Version: 1.0
 */
@Component
public class TenantMqProducter {

    @Autowired
    private ISJobService jobService;

    @Autowired
    private ITenantService iTenantService;

    /**
     * 调用消息队列，消息队列调用定时任务
     */
    @Autowired
    private PerfectMqProducer mqproducer;

    /**
     * 保存完毕后，调用mq，触发定时任务
     *
     * 规则：获取完整的数据进行判断后触发
     *
     */
    public void mqSendAfterDataSave(Long tenant_id){
        STenantVo sTenantVo = iTenantService.selectByid(tenant_id);
        mqSendAfterDataSave(sTenantVo);
    }

    /**
     * 保存完毕后，调用mq，触发定时任务
     *
     * 规则：获取完整的数据进行判断后触发
     *
     */
    public void mqSendAfterDataSave(STenantVo data){
        // 启用的task
        SJobEntity enableJobEntity = jobService.selectJobBySerialId(data.getId(), QuartzEnum.TASK_TENANT_ENABLE.getJob_serial_type());
        SJobEntity enableBean = TenantTaskBuilder.builderEnableBean(data, enableJobEntity);
        // 初始化要发生mq的bean
        MqSenderPojo
            enableMqSenderPojo = MqSenderPojoBuilder.buildMqSenderPojo(enableBean, MqSenderEnum.NORMAL_MQ, QuartzEnum.TASK_TENANT_ENABLE.getJob_name());
        // 启动一个开始的任务
        mqproducer.send(enableMqSenderPojo, MQEnum.MQ_TASK_Tenant_ENABLE);

        // 禁用的task
        SJobEntity disableJobEntity = jobService.selectJobBySerialId(data.getId(), QuartzEnum.TASK_TENANT_DISABLE.getJob_serial_type());
        SJobEntity disableBean = TenantTaskBuilder.builderDisableBean(data, disableJobEntity);
        // 初始化要发生mq的bean
        MqSenderPojo disableMqSenderPojo = MqSenderPojoBuilder.buildMqSenderPojo(disableBean, MqSenderEnum.NORMAL_MQ, QuartzEnum.TASK_TENANT_DISABLE.getJob_name());
        // 启动一个结束的任务
        mqproducer.send(disableMqSenderPojo, MQEnum.MQ_TASK_Tenant_Disable);
    }

    private void resetAllMq(List<STenantVo> dataList){
        for (STenantVo vo : dataList){
            mqSendAfterDataSave(vo);
        }
    }
}
