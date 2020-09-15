package com.perfect.bean.vo.sys.rbac.role;

import java.io.Serializable;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.common.annotations.ExcelAnnotion;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色导出Bean
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色导出Bean", description = "角色导出Bean")
public class SRoleExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -7449124258332853610L;

    private Long id;

    /**
     * 角色类型
     */
    @ExcelAnnotion(name = "角色类型")
    private String type;

    /**
     * 角色编码
     */
    @ExcelAnnotion(name = "角色编码")
    private String code;

    /**
     * 角色名称
     */
    @ExcelAnnotion(name = "角色名称")
    private String name;

    /**
     * 说明
     */
    @ExcelAnnotion(name = "说明")
    private String descr;

    /**
     * 简称
     */
    @ExcelAnnotion(name = "简称")
    private String simple_name;

}
