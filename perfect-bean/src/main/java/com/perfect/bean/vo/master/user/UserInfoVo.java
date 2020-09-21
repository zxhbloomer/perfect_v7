package com.perfect.bean.vo.master.user;

import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.config.base.v1.BaseVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ApiModel(value = "用户基本信息", description = "用户基本信息vo_bean")
@EqualsAndHashCode(callSuper=false)
public class UserInfoVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 574627344179000681L;

    private String token;

    private String[] roles;

    private String introduction;

    private String avatar;

    private String name;

    private UserSessionBo user_session_bean;
}
