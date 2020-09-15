package com.perfect.core.serviceimpl.sys.rbac.role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.sys.rbac.role.SRoleEntity;
import com.perfect.bean.vo.sys.rbac.role.SRoleVo;
import com.perfect.core.mapper.sys.rbac.role.SRoleMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.rbac.role.ISRoleService;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-07-11
 */
@Service
public class SRoleServiceImpl extends BaseServiceImpl<SRoleMapper, SRoleEntity> implements ISRoleService {

    @Autowired
    private SRoleMapper sRoleMapper;

    /**
     * 获取列表，页面查询
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<SRoleEntity> selectPage(SRoleVo searchCondition) {
        // 分页条件
        Page<SRoleEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        return sRoleMapper.selectPage(pageCondition, searchCondition);
    }

    /**
     * 获取列表，查询所有数据
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public List<SRoleEntity> select(SRoleVo searchCondition){
        // 查询 数据
        List<SRoleEntity> list = sRoleMapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public List<SRoleEntity> selectIdsIn(List<SRoleVo> searchCondition) {
        // 查询 数据
        List<SRoleEntity> list = sRoleMapper.selectIdsIn(searchCondition);
        return list;
    }

    /**
     * 批量导入逻辑
     * 
     * @param entityList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatches(List<SRoleEntity> entityList) {
        return super.saveBatch(entityList, 500);
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdsIn(List<SRoleVo> searchCondition) {
        List<SRoleEntity> list = sRoleMapper.selectIdsIn(searchCondition);
        list.forEach(
            bean -> {
                bean.setIsdel(!bean.getIsdel());
            }
        );
        saveOrUpdateBatch(list, 500);
    }

}
