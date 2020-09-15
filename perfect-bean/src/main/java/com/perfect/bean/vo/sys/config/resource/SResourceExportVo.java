package com.perfect.bean.vo.sys.config.resource;

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
@ApiModel(value = "资源表导出Bean", description = "资源表导出Bean")
public class SResourceExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -3069693188733171667L;


    private Long id;

    /**
     * excel导入模板文件：10，静态配置文件：20，静态图片文件：30
     */
    @ExcelAnnotion(name = "资源类型")
    private String type;

    /**
     * 相对路径
     */
    @ExcelAnnotion(name = "相对路径")
    private String uri;

    /**
     * 文件系统的baseurl
     */
    @ExcelAnnotion(name = "baseurl")
    private String base;

    /**
     * 文件大小
     */
    @ExcelAnnotion(name = "文件大小")
    private Long file_size;

    /**
     * 文件扩展名
     */
    @ExcelAnnotion(name = "文件扩展名")
    private String extension;

    /**
     * 说明
     */
    @ExcelAnnotion(name = "说明")
    private String descr;

}
