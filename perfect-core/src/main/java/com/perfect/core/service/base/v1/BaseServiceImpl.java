package com.perfect.core.service.base.v1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.utils.servlet.ServletUtil;

/**
 * 扩展Mybatis-Plus接口
 *
 * @author
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    /**
     * 获取当前登录用户的session数据
     * @return
     */
    public UserSessionBo getUserSession(){
        UserSessionBo bo = ServletUtil.getUserSession();
        return bo;
    }

    /**
     * 获取当前登录用户的session数据:租户数据
     * @return
     */
    public Long getUserSessionTenantId(){
        if(getUserSession() == null) {
            return null;
        }
        Long tenant_Id = getUserSession().getTenant_Id();
        return tenant_Id;
    }

}
