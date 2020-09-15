package com.perfect.bean.vo.master.rbac.permission;

import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zxh
 * @since 2020-07-27
 */
@Data
@NoArgsConstructor
@ApiModel(value = "权限表", description = "权限表")
@EqualsAndHashCode(callSuper=false)
public class MPermissionVo implements Serializable {

    private static final long serialVersionUID = 6160575309487167959L;

    private Long id;

    /**
     * 类型：10（部门权限）、20（岗位权限）、30（用户权限）、40（排除权限）
     */
    private String type;

    /**
     * 关联单号类型
     */
    private String serial_type;

    /**
     * 关联单号
     */
    private Long serial_id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 关联单号
     */
    private Long menu_id;

    /**
     * 0:未使用，1:已使用
     */
    private Boolean status;

    /**
     * 说明
     */
    private String descr;

    /**
     * 是否是已经删除(1:true-已删除,0:false-未删除)
     */
    private Boolean is_del;

    /**
     * 是否启用(1:true-已启用,0:false-已禁用)
     */
    private Boolean is_enable;

    /**
     * 租户id
     */
    private Long tenant_id;

    private Long c_id;
    private String c_name;

    private LocalDateTime c_time;

    private Long u_id;
    private String u_name;

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
