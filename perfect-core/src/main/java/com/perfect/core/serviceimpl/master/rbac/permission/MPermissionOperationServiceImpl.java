package com.perfect.core.serviceimpl.master.rbac.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.rbac.permission.MPermissionOperationEntity;
import com.perfect.core.mapper.master.rbac.permission.MPermissionOperationMapper;
import com.perfect.core.service.master.rbac.permission.IMPermissionOperationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限页面操作表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2020-08-07
 */
@Service
public class MPermissionOperationServiceImpl extends ServiceImpl<MPermissionOperationMapper, MPermissionOperationEntity> implements
    IMPermissionOperationService {

}
