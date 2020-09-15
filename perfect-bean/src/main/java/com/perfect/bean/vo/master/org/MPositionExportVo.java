package com.perfect.bean.vo.master.org;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.common.annotations.ExcelAnnotion;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 岗位主表
 * </p>
 *
 * @author zxh
 * @since 2019-11-12
 */
@Data
@NoArgsConstructor
@ApiModel(value = "岗位主表", description = "岗位主表")
@EqualsAndHashCode(callSuper=false)
public class MPositionExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 8990646953974899179L;

    private Long id;

    /**
     * 编码
     */
    @ExcelAnnotion(name = "编码")
    private String code;

    /**
     * 全称
     */
    @ExcelAnnotion(name = "名称")
    private String name;

    /**
     * 简称
     */
    @ExcelAnnotion(name = "简称")
    private String simple_name;

    /**
     * 说明
     */
    @ExcelAnnotion(name = "说明")
    private String descr;

    /**
     * 是否删除
     */
    private Boolean is_del;
    @ExcelAnnotion(name = "是否删除")
    private String is_del_name;

    /**
     * 租户id
     */
    private Long tenant_id;

    private Long c_id;
    @ExcelAnnotion(name = "新增人")
    private String c_name;
    @ExcelAnnotion(name = "新增时间")
    private LocalDateTime c_time;
    @ExcelAnnotion(name = "更新人")
    private Long u_id;
    @ExcelAnnotion(name = "更新时间")
    private String u_name;

    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    private Integer dbversion;

    /**
     * 关联单号
     */
    private Long parent_serial_id;

    /**
     * 关联单号类型
     */
    private String parent_serial_type;
    private String parent_name;
    private String parent_simple_name;
    private String parent_type_text;

    /**
     * 该岗位向下，员工数量
     */
    private int staff_count;

}
