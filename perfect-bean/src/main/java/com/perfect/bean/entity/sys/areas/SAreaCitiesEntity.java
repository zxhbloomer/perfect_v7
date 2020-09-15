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
 * 市
 * </p>
 *
 * @author zxh
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_area_cities")
public class SAreaCitiesEntity implements Serializable {

    private static final long serialVersionUID = 7102750642289585035L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 市级编号
     */
    @TableField("code")
    private Integer code;

    /**
     * 市名称
     */
    @TableField("name")
    private String name;


}
