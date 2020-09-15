package com.perfect.bean.vo.sys.config.resource;

import com.perfect.bean.pojo.fs.UploadFileResultPojo;
import com.perfect.bean.vo.common.condition.PageCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangxh
 */
@Data
@NoArgsConstructor
@ApiModel(value = "资源返回信息", description = "资源返回vo_bean")
@EqualsAndHashCode(callSuper=false)
public class SResourceVo extends UploadFileResultPojo implements Serializable {

    private static final long serialVersionUID = 2443084812232177470L;

    private Long id;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 资源编码
     */
    private String [] types;

    /**
     * 名称
     */
    private String name;

    /**
     * 说明
     */
    private String descr;

    /**
     * 租户代码
     */
    private String corp_code;

    /**
     * 租户名称
     */
    private String corp_name;

    /**
     * 换页条件
     */
    private PageCondition pageCondition;

    /**
     * 是否删除
     */
    private Boolean is_del;
}
