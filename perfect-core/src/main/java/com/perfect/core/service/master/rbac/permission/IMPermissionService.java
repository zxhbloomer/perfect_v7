package com.perfect.core.service.master.rbac.permission;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.rbac.permission.MPermissionEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.rbac.permission.MMenuRootNodeListVo;
import com.perfect.bean.vo.master.rbac.permission.MPermissionVo;
import com.perfect.bean.vo.master.rbac.permission.operation.OperationMenuDataVo;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zxh
 * @since 2020-07-27
 */
public interface IMPermissionService extends IService<MPermissionEntity> {

    /**
     * 获取列表，页面查询
     */
    IPage<MPermissionVo> selectPage(MPermissionVo searchCondition) ;

//    /**
//     * 获取所有数据
//     */
//    List<MPermissionVo> select(MPermissionVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MPermissionVo> selectIdsIn(List<MPermissionVo> searchCondition) ;

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void enableById(MPermissionVo searchCondition);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<MPermissionVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @return
     */
    InsertResult<MPermissionVo> insert(MPermissionVo mPermissionVo, OperationMenuDataVo operationMenuDataVo);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @return
     */
    UpdateResult<MPermissionVo> update(MPermissionVo vo);

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    MPermissionVo selectByid(Long id);

    /**
     * 部门权限表数据获取系统菜单根节点
     * @param vo
     * @return
     */
    MMenuRootNodeListVo getSystemMenuRootList(MMenuRootNodeListVo vo);

//    /**
//     * 判断是否已经选择了菜单
//     * @param searchCondition
//     * @return
//     */
//    Boolean isAlreadySetMenuId(MPermissionVo searchCondition);
}
