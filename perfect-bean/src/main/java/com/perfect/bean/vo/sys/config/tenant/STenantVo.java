package com.perfect.bean.vo.sys.config.tenant;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@ApiModel(value = "租户信息", description = "租户信息")
@EqualsAndHashCode(callSuper=false)
public class STenantVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 6408599988501466911L;

    private Long id;

    /**
     * 父结点
     */
    private Long parent_id;
    private String parent_name;

    /**
     * 系统编码
     */
    private String serial_no;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String simple_name;

    /**
     * 是否启用(1:true-已启用,0:false-已禁用)
     */
    private Boolean is_enable;

    /**
     * 生效日期
     */
    private LocalDateTime enable_time;

    /**
     * 失效日期
     */
    private LocalDateTime disable_time;

    private LocalDateTime[] enable_time_range;

    /**
     * 是否冻结
     */
    private Boolean is_freeze;

    /**
     * 是否叶子结点
     */
    private Boolean is_leaf;

    /**
     * 级次
     */
    private Integer level;

    /**
     * 说明
     */
    private String descr;

    /**
     * 是否删除
     */
    private Boolean is_del;

    private Long c_id;

    private LocalDateTime c_time;

    private Long u_id;

    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    private Integer dbversion;

    /**
     * 换页条件
     */
    private PageCondition pageCondition;
}
