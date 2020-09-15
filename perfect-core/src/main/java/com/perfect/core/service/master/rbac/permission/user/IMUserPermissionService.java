package com.perfect.core.service.master.rbac.permission.user;

import com.perfect.bean.bo.session.user.rbac.PermissionMenuBo;
import com.perfect.bean.bo.session.user.rbac.PermissionOperationBo;

import java.util.List;

/**
 * @ClassName:
 * @Description: 获取用户的权限
 * @Author: zxh
 * @date: 2020/8/26
 * @Version: 1.0
 */
public interface IMUserPermissionService  {

    /**
     * 菜单权限数据，顶部导航栏
     */
    List<PermissionMenuBo> getPermissionMenuTopNav(Long tenant_id);

    /**
     * 菜单权限数据
     */
    List<PermissionMenuBo> getPermissionMenu(Long staff_id, Long tenant_id);

    /**
     * 操作权限数据
     */
    List<PermissionOperationBo> getPermissionOperation(Long staff_id, Long tenant_id);

    /**
     * 获取默认页面
     * @param tenant_id
     * @return
     */
    String getPermissionMenuDefaultPage( Long tenant_id);

}
