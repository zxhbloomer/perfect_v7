package com.perfect.core.serviceimpl.sys.config.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.sys.config.resource.SResourceEntity;
import com.perfect.bean.vo.sys.config.resource.SResourceVo;
import com.perfect.core.mapper.sys.config.resource.SResourceMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.config.resource.ISResourceService;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
@Service
public class SResourceServiceImpl extends BaseServiceImpl<SResourceMapper, SResourceEntity>
    implements ISResourceService {

    @Autowired
    private SResourceMapper sResourceMapper;

    /**
     * 获取列表，页面查询
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<SResourceEntity> selectPage(SResourceVo searchCondition) {
        // 分页条件
        Page<SResourceEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        return sResourceMapper.selectPage(pageCondition, searchCondition);
    }

    /**
     * 获取列表，查询所有数据
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public List<SResourceEntity> select(SResourceVo searchCondition) {
        // 查询 数据
        List<SResourceEntity> list = sResourceMapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public List<SResourceEntity> selectIdsIn(List<SResourceVo> searchCondition) {
        // 查询 数据
        List<SResourceEntity> list = sResourceMapper.selectIdsIn(searchCondition);
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
    public boolean saveBatches(List<SResourceEntity> entityList) {
        return super.saveBatch(entityList, 500);
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdsIn(List<SResourceVo> searchCondition) {
        List<SResourceEntity> list = sResourceMapper.selectIdsIn(searchCondition);
        list.forEach(
            bean -> {
                bean.setIs_del(!bean.getIs_del());
            }
        );
        saveOrUpdateBatch(list, 500);
    }

}
