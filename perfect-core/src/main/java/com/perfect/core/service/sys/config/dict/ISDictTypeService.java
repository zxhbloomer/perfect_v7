package com.perfect.core.service.sys.config.dict;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.config.dict.SDictTypeEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.config.dict.SDictTypeVo;

import java.util.List;

/**
 * <p>
 * 字典类型表、字典主表 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
public interface ISDictTypeService extends IService<SDictTypeEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<SDictTypeEntity> selectPage(SDictTypeVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<SDictTypeEntity> select(SDictTypeVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<SDictTypeEntity> selectIdsIn(List<SDictTypeVo> searchCondition) ;

    /**
     * 批量导入
     */
    boolean saveBatches(List<SDictTypeEntity> entityList);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<SDictTypeVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(SDictTypeEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(SDictTypeEntity entity);

    /**
     * 通过code查询
     *
     */
    List<SDictTypeEntity> selectByCode(String code);
}
