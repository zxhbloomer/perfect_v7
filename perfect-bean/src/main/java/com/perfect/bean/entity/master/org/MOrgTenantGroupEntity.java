package com.perfect.bean.entity.master.org;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 集团与集团关系表，多集团嵌套关系表
 * </p>
 *
 * @author zxh
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_org_tenant_group")
public class MOrgTenantGroupEntity implements Serializable {

    private static final long serialVersionUID = -745386752871791908L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织id
     */
    @TableField("org_id")
    private Long org_id;

    /**
     * 组织parent id
     */
    @TableField("org_parent_id")
    private Long org_parent_id;

    /**
     * 集团id
     */
    @TableField("current_id")
    private Long current_id;

    /**
     * 所属集团id
     */
    @TableField("parent_id")
    private Long parent_id;

    /**
     * 所属父结点类型
     */
    @TableField("parent_type")
    private String parent_type;

    /**
     * 根集团id
     */
    @TableField("root_id")
    private Long root_id;

    /**
     * 嵌套时会>1
     */
    @TableField("counts")
    private Integer counts;

    /**
     * 从根集团到当前集团的排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenant_id;

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
