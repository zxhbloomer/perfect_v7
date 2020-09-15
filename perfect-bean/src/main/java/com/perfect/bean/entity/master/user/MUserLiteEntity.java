package com.perfect.bean.entity.master.user;

import com.baomidou.mybatisplus.annotation.*;
import com.perfect.bean.entity.base.entity.v1.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 简单
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_user_lite")
public class MUserLiteEntity extends BaseEntity<MUserLiteEntity> implements Serializable {

    private static final long serialVersionUID = -3245496064225940524L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("login_name")
    private String loginName;

    @TableField("name")
    private String name;

    @TableField("simple_name")
    private String simple_name;

    /**
     * 系统用户=10,职员=20,客户=30,供应商=40,其他=50,认证管理员=60,审计管理员=70
     */
    @TableField("type")
    private String type;

    /**
     * 说明
     */
    @TableField("descr")
    private String descr;

    /**
     * 密码

     */
    @TableField("pwd")
    private String pwd;

    /**
     * 租户代码
     */
    @TableField("corp_code")
    private String corp_code;

    /**
     * 租户名称
     */
    @TableField("corp_name")
    private String corp_name;

    @TableField("avatar")
    private String avatar;

    @TableField(value="c_id", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long c_id;

    @TableField(value="c_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime c_time;

    @TableField(value="u_id", fill = FieldFill.INSERT_UPDATE)
    private Long u_id;

    @TableField(value="u_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime u_time;

}
