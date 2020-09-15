package com.perfect.common.enums;

/**
 * @ClassName: mq发送枚举类
 * @Description: TODO
 * @Author: zxh
 * @date: 2019/10/16
 * @Version: 1.0
 */
public enum MqSenderEnum {
    CALL_BACK_MQ(0, "通用队列，有回调"),
    NORMAL_MQ(1, "通用队列，无回调");
    private Integer code;

    private String name;

    MqSenderEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName(){
        return name;
    }
}
