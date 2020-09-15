package com.perfect.bean.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 短信验证码
 * </p>
 *
 * @author zxh
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_sms_code")
public class SSmsCodeEntity implements Serializable {

    private static final long serialVersionUID = -9076562134275007478L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 验证码
     */
    @TableField("code")
    private String code;

    /**
     * 0:未知/1:注册/2:忘记密码/3:修改绑定手机
     */
    @TableField("type")
    private String type;

    @TableField("c_id")
    private Long c_id;

    @TableField("c_time")
    private LocalDateTime c_time;

    @TableField("u_id")
    private Long u_id;

    @TableField("u_time")
    private LocalDateTime u_time;


}
