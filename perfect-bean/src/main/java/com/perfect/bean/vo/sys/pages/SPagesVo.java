package com.perfect.bean.vo.sys.pages;

import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 页面表
 * </p>
 *
 * @author zxh
 * @since 2020-06-04
 */
@Data
@NoArgsConstructor
@ApiModel(value = "页面表vo", description = "页面表vo")
@EqualsAndHashCode(callSuper=false)
public class SPagesVo implements Serializable {

    private static final long serialVersionUID = 4833527967811065817L;

    private Long id;

    /**
     * 配置vue export default  name时所使用的type：constants_program.P_VUE_SETTING
     */
    private String code;

    /**
     * 页面名称
     */
    private String name;

    /**
     * 模块地址：@/views/10_system/vuesetting/vue
     */
    private String component;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 页面的名称
     */
    private String meta_title;

    /**
     * 菜单中显示的icon
     */
    private String meta_icon;

    /**
     * 说明
     */
    private String descr;

    private Long c_id;
    private String c_name;

    private LocalDateTime c_time;

    private Long u_id;
    private String u_name;

    private LocalDateTime u_time;

    /**
     * 数据版本，乐观锁使用
     */
    private Integer dbversion;

    /**
     * 换页条件
     */
    private PageCondition pageCondition;

}
