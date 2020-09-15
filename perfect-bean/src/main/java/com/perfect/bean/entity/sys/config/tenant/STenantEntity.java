package com.perfect.bean.entity.sys.config.tenant;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 租户主表
 * </p>
 *
 * @author zxh
 * @since 2019-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("s_tenant")
public class STenantEntity implements Serializable {

    private static final long serialVersionUID = -9178316502154676964L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("parent_id")
    private Long parent_id;

    /**
     * 系统编码
     */
    @TableField("serial_no")
    private String serial_no;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 简称
     */
    @TableField("simple_name")
    private String simple_name;

    /**
     * 是否启用(1:true-已启用,0:false-已禁用)
     */
    @TableField("is_enable")
    private Boolean is_enable;

    /**
     * 生效日期
     */
    @TableField("enable_time")
    private LocalDateTime enable_time;

    /**
     * 失效日期
     */
    @TableField("disable_time")
    private LocalDateTime disable_time;

    /**
     * 是否冻结
     */
    @TableField("is_freeze")
    private Boolean is_freeze;

    /**
     * 是否叶子结点
     */
    @TableField("is_leaf")
    private Boolean is_leaf;

    /**
     * 级次
     */
    @TableField("level")
    private Integer level;


    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;


    /**
     * 说明
     */
    @TableField("descr")
    private String descr;

    /**
     * 是否删除
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean is_del;

    @TableField(value="c_id", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long c_id;

    @TableField(value="c_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime c_time;

    @TableField(value="u_id", fill = FieldFill.INSERT_UPDATE)
    private Long u_id;

    @TableField(value="u_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    @Version
    @TableField(value="dbversion", fill = FieldFill.INSERT_UPDATE)
    private Integer dbversion;


}
