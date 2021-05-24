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
public class MUserLiteEntity implements Serializable {

    private static final long serialVersionUID = 1898364708866632626L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * m_user的主键
     */
    @TableField("user_id")
    private Long user_id;

    /**
     * m_staff的主键
     */
    @TableField("staff_id")
    private Long staff_id;

    /**
     * 登录模式：（10：手机号码；20：邮箱）
     */
    @TableField("login_type")
    private String login_type;

    /**
     * 登录用户名
     */
    @TableField("login_name")
    private String login_name;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 简称
     */
    @TableField("simple_name")
    private String simple_name;

    /**
     * 系统用户=10,职员=20,客户=30,供应商=40,其他=50,认证管理员=60,审计管理员=70
     */
    @TableField("type")
    private String type;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenant_id;

    /**
     * 所属公司
     */
    @TableField("company_id")
    private Long company_id;

    /**
     * 默认部门
     */
    @TableField("dept_id")
    private Long dept_id;

}
