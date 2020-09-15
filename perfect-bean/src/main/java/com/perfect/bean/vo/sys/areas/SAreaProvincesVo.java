package com.perfect.bean.vo.sys.areas;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.perfect.bean.config.base.v1.BaseVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 省份
 * </p>
 *
 * @author zxh
 * @since 2019-10-31
 */
@Data
@NoArgsConstructor
@ApiModel(value = "省份", description = "省份")
@EqualsAndHashCode(callSuper=false)
public class SAreaProvincesVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 8199680070615544291L;

    private Long id;

    /**
     * 省份编号
     */
    private Integer code;

    /**
     * 省份名称
     */
    private String name;


}
