package com.perfect.core.service.sys.config.dict;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.config.dict.SDictDataEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.config.dict.SDictDataVo;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
public interface ISDictDataService extends IService<SDictDataEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<SDictDataVo> selectPage(SDictDataVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<SDictDataVo> select(SDictDataVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<SDictDataVo> selectIdsIn(List<SDictDataVo> searchCondition) ;

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    SDictDataVo selectByid(Long id);

    /**
     * 批量导入
     */
    boolean saveBatches(List<SDictDataEntity> entityList);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<SDictDataVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(SDictDataEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(SDictDataEntity entity);

    /**
     * sort保存
     *
     */
    UpdateResult<List<SDictDataVo>> saveList(List<SDictDataVo> data);

    /**
     *
     * @param searchCondition
     * @return
     */
    List<SDictDataVo> selectColumnComment(SDictDataVo searchCondition);
}
