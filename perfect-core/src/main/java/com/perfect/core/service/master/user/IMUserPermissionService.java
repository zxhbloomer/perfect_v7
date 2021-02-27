package com.perfect.core.service.master.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.user.MUserPermissionEntity;
import com.perfect.bean.vo.master.org.MStaffPositionVo;

import java.util.List;

/**
 * <p>
 * 用户权限关联表 服务类
 * </p>
 *
 * @author zxh
 * @since 2021-02-09
 */
public interface IMUserPermissionService extends IService<MUserPermissionEntity> {


    /**
     * 查询岗位员工
     * @param user_id
     * @return
     */
    List<MUserPermissionEntity> resetUserPermission(Long user_id);
}
