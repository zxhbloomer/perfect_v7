package com.perfect.bean.vo.sys.config.dict;

import java.io.Serializable;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.common.annotations.ExcelAnnotion;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源表导出Bean
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "字典类型导出Bean", description = "字典类型导出Bean")
public class SDictTypeExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -6942475112738825609L;
    private Long id;

    @ExcelAnnotion(name = "字典类型")
    private String code;

    @ExcelAnnotion(name = "字典名称")
    private String name;

    @ExcelAnnotion(name = "说明")
    private String descr;

    @ExcelAnnotion(name = "是否删除")
    private String is_del;

}
