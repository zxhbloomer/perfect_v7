package com.perfect.core.service.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.org.MPositionEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.org.MPositionVo;

import java.util.List;

/**
 * <p>
 * 岗位主表 服务类 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
public interface IMPositionService extends IService<MPositionEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<MPositionVo> selectPage(MPositionVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<MPositionVo> select(MPositionVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MPositionEntity> selectIdsIn(List<MPositionVo> searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MPositionVo> selectIdsInForExport(List<MPositionVo> searchCondition) ;

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<MPositionVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(MPositionEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(MPositionEntity entity);

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    MPositionVo selectByid(Long id);
}
