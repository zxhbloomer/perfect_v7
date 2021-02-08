package com.perfect.bean.entity.master.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户默认菜单表
 * </p>
 *
 * @author zxh
 * @since 2021-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_user_default_menu")
public class MUserDefaultMenuEntity implements Serializable {

    private static final long serialVersionUID = 7007703204943829558L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户主表id
     */
    @TableField("user_id")
    private Long user_id;

    /**
     * 菜单id，基本为m_permission_menu.permission_id，需要考虑自定义菜单情况
     */
    @TableField("menu_id")
    private Long menu_id;


}
