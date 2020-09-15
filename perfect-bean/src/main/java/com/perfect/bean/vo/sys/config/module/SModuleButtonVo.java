package com.perfect.bean.vo.sys.config.module;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模块按钮信息
 * </p>
 *
 * @author zxh
 * @since 2019-11-02
 */
@Data
@NoArgsConstructor
@ApiModel(value = "模块按钮信息", description = "模块按钮信息")
@EqualsAndHashCode(callSuper=false)
public class SModuleButtonVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 3951235625344140283L;

    private Long id;

    private String module_code;
    private String module_name;

    /**
     * 按钮编号
     */
    private String code;

    /**
     * 按钮名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 排序的最大最小值
     */
    private int max_sort;
    private int min_sort;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 页面id，
     */
    private Long parent_id;

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
