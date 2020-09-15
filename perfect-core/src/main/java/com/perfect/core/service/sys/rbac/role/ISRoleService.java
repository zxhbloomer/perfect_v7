package com.perfect.core.service.sys.rbac.role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.rbac.role.SRoleEntity;
import com.perfect.bean.vo.sys.rbac.role.SRoleVo;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-07-11
 */
public interface ISRoleService extends IService<SRoleEntity> {

    /**
     * 获取列表，页面查询
     */
    IPage<SRoleEntity> selectPage(SRoleVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<SRoleEntity> select(SRoleVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<SRoleEntity> selectIdsIn(List<SRoleVo> searchCondition) ;

    /**
     * 批量导入
     */
    boolean saveBatches(List<SRoleEntity> entityList);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<SRoleVo> searchCondition);

}
