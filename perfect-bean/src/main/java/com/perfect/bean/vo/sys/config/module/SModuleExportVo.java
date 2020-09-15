package com.perfect.bean.vo.sys.config.module;

import com.baomidou.mybatisplus.annotation.TableName;
import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.bean.vo.common.condition.PageCondition;
import com.perfect.common.annotations.ExcelAnnotion;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 模块：页面、task等
 * </p>
 *
 * @author zxh
 * @since 2019-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_module")
public class SModuleExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -4793768968717402701L;

    private Long id;

    /**
     * 模块编号
     */
    @ExcelAnnotion(name = "模块编号")
    private String code;

    /**
     * 类型
     */
    @ExcelAnnotion(name = "模块类型")
    private String type;

    /**
     * 名称
     */
    @ExcelAnnotion(name = "模块名称")
    private String name;

    /**
     * 说明
     */
    @ExcelAnnotion(name = "模块类型")
    private String descr;

    /**
     * 是否删除
     */
    @ExcelAnnotion(name = "是否删除")
    private Boolean isdel;

    /**
     * 换页条件
     */
    private PageCondition pageCondition;

    private Long c_id;

    private LocalDateTime c_time;

    private Long u_id;

    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    private Integer dbversion;


}
