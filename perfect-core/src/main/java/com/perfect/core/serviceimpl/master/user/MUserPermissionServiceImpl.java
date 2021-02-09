package com.perfect.core.serviceimpl.master.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.user.MUserPermissionEntity;
import com.perfect.core.mapper.master.user.MUserPermissionMapper;
import com.perfect.core.service.master.user.IMUserPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限关联表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2021-02-09
 */
@Service
public class MUserPermissionServiceImpl extends ServiceImpl<MUserPermissionMapper, MUserPermissionEntity> implements IMUserPermissionService {

}
