package com.perfect.core.service.sys.pages.function;

import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.pages.function.SFunctionEntity;
import com.perfect.bean.pojo.result.DeleteResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.pages.function.SFunctionVo;

import java.util.List;

/**
 * <p>
 * 按钮表 服务类
 * </p>
 *
 * @author zxh
 * @since 2020-06-16
 */
public interface ISFunctionService extends IService<SFunctionEntity> {

    /**
     * 获取列表，页面查询
     * @param searchCondition
     * @return
     */
    List<SFunctionVo> selectPage(SFunctionVo searchCondition) ;

    /**
     * 获取所有数据
     * @param searchCondition
     * @return
     */
    List<SFunctionVo> select(SFunctionVo searchCondition) ;


    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    SFunctionVo selectByid(Long id);


    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(SFunctionVo entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(SFunctionVo entity);


    /**
     * 批量物理删除
     * @param searchCondition
     * @return
     */
    DeleteResult<Integer> realDeleteByIdsIn(List<SFunctionVo> searchCondition);

    /**
     * sort保存
     */
    UpdateResult<List<SFunctionVo>> saveSort(List<SFunctionVo> data);
}
