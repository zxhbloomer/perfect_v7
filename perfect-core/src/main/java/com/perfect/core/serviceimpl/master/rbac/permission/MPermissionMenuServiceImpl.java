package com.perfect.core.serviceimpl.master.rbac.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.rbac.permission.MPermissionMenuEntity;
import com.perfect.core.mapper.master.rbac.permission.MPermissionMenuMapper;
import com.perfect.core.service.master.rbac.permission.IMPermissionMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限菜单信息 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2020-08-07
 */
@Service
public class MPermissionMenuServiceImpl extends ServiceImpl<MPermissionMenuMapper, MPermissionMenuEntity> implements
    IMPermissionMenuService {

}
