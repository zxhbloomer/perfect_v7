package com.perfect.core.service.sys.config.tenant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.config.tenant.STenantTreeVo;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
public interface ITenantService extends IService<STenantEntity> {

    /**
     * 获取数据，树结构
     * 
     * @param id
     * @return
     */
    List<STenantTreeVo> getTreeList(Long id, String nam);

    /**
     * 获取数据，级联结构
     *
     * @param id
     * @return
     */
    List<STenantTreeVo> getCascaderList(Long id, String nam);

    /**
     * 获取列表，页面查询
     */
    IPage<STenantVo> selectPage(STenantVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<STenantEntity> selectIdsIn(List<STenantVo> searchCondition) ;

    /**
     * 插入一条记录（选择字段，策略插入）
     * 
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(STenantEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * 
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(STenantEntity entity);

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    STenantVo selectByid(Long id);

    /**
     * 通过code查询
     *
     */
    List<STenantEntity> selectByCode(String code);

    /**
     * 通过名称查询
     *
     */
    List<STenantEntity> selectByName(String name);

    /**
     * 根据ID获取子结点数组
     * 
     * @param id
     * @return
     */
     List<STenantTreeVo> getChildren(Long id, String name);

    /**
     * 启用
     * @param entity
     * @return
     */
     UpdateResult<Integer> enableUpdate(STenantEntity entity);

    /**
     * 禁用
     * @param entity
     * @return
     */
    UpdateResult<Integer> disableUpdate(STenantEntity entity);

    /**
     * 启用
     * @param entity
     * @return
     */
    boolean enableProcess(STenantEntity entity);

    /**
     * 禁用
     * @param entity
     * @return
     */
    boolean disableProcess(STenantEntity entity);
}
