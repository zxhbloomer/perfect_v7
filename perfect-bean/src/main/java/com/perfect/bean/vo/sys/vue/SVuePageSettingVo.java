package com.perfect.bean.vo.sys.vue;

import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zxh
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vue页面配置表", description = "资源表导出Bean")
public class SVuePageSettingVo implements Serializable {

    private static final long serialVersionUID = -7712359095709901953L;

    private Long id;

    /**
     * vue export default 的 name
     */
    private String name;

    /**
     * 配置vue export default  name时所使用的type
     */
    private String code;


    /**
     * 模块地址：@/views/10_system/vuesetting/vue
     */
    private String component;

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
