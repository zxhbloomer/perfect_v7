package com.perfect.bean.entity.sys.areas;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区
 * </p>
 *
 * @author zxh
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_areas")
public class SAreasEntity implements Serializable {

    private static final long serialVersionUID = -2321947233766835205L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区编号
     */
    @TableField("code")
    private Integer code;

    /**
     * 地区名称
     */
    @TableField("name")
    private String name;

    /**
     * 市级编号
     */
    @TableField("city_code")
    private Integer city_code;

    /**
     * 省级编号
     */
    @TableField("province_code")
    private Integer province_code;


}
