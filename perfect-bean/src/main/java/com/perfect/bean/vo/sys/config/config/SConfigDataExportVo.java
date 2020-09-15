package com.perfect.bean.vo.sys.config.config;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.common.annotations.ExcelAnnotion;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zxh
 * @date 2019/9/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "参数配置表导出Bean", description = "参数配置表导出Bean")
public class SConfigDataExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -21039960705170821L;

    private Long id;

    /**
     * 参数名称
     */
    @ExcelAnnotion(name = "参数名称")
    private String name;

    /**
     * 参数键名
     */
    @ExcelAnnotion(name = "参数键名")
    private String config_key;

    /**
     * 参数键值
     */
    @ExcelAnnotion(name = "参数键值")
    private String value;

    /**
     * 是否启用(1:true-已启用,0:false-已禁用)
     */
    @ExcelAnnotion(name = "是否禁用")
    private Boolean is_enable;

    /**
     * 说明
     */
    @ExcelAnnotion(name = "说明")
    private String descr;

    private Long c_id;

    private LocalDateTime c_time;

    private Long u_id;

    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    private Integer dbversion;
}
