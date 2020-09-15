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
 * 省份
 * </p>
 *
 * @author zxh
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_area_provinces")
public class SAreaProvincesEntity implements Serializable {

    private static final long serialVersionUID = 5724484419786651666L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 省份编号
     */
    @TableField("code")
    private Integer code;

    /**
     * 省份名称
     */
    @TableField("name")
    private String name;


}
