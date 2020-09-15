package com.perfect.bean.entity.sys.config.resource;

import com.baomidou.mybatisplus.annotation.*;
import com.perfect.bean.entity.base.entity.v1.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author zxh
 * @since 2019-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_resource")
public class SResourceEntity extends BaseEntity<SResourceEntity> implements Serializable {

    private static final long serialVersionUID = 6090334147779080116L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * excel导入模板文件：10，静态配置文件：20，静态图片文件：30，json
     */
    @TableField("type")
    private String type;

    /**
     * 资源名称
     */
    @TableField("name")
    private String name;

    /**
     * 相对路径
     */
    @TableField("uri")
    private String uri;

    /**
     * 文件系统的baseurl
     */
    @TableField("base")
    private String base;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long file_size;

    /**
     * 文件扩展名
     */
    @TableField("extension")
    private String extension;

    /**
     * 说明
     */
    @TableField("descr")
    private String descr;

    /**
     * json配置文件
     */
    @TableField("context")
    private String context;

    /**
     * 是否删除
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean is_del;

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
