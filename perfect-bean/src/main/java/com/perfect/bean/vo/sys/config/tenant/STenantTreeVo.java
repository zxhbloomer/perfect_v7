package com.perfect.bean.vo.sys.config.tenant;

import com.perfect.bean.vo.common.component.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 生成租户树数据的接收类
 *
 * @author zxh
 * @date 2019年 10月03日 11:55:24
 */
@Data
@ApiModel(value = "生成租户树数据的接收类", description = "生成租户树数据的接收类")
@EqualsAndHashCode(callSuper=false)
public class STenantTreeVo extends TreeNode implements Serializable {

    private static final long serialVersionUID = -7928187114619523854L;

    /**
     * 编码
     */
    private String code;

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

    /**
     * 是否冻结
     */
    private Boolean is_freeze;

    /**
     * 是否叶子结点
     */
    private Boolean is_leaf;

    /**
     * 说明
     */
    private String descr;

    /**
     * 树结点名称：按深度显示名称 xx>...>xx
     */
    private String depth_name;

    /**
     * 系统编码：如：第一层：0001
     *              第二层：00010001
     */
    private String serial_no;

    /**
     * 父亲结点编码
     */
    private String parent_code;

    /**
     * 父亲结点系统编码
     */
    private String parent_serial_no;

    /**
     * 级联控件使用，父结点id
     */
    private Long value;

}
