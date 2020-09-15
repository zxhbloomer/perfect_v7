package com.perfect.core.service.sys.platform.syscode;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.platform.syscode.SCodeEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.platform.syscode.SCodeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxh
 * @since 2019-07-04
 */
public interface ISCodeService extends IService<SCodeEntity> {

    /**
     * 获取列表，页面查询
     */
    IPage<SCodeVo> selectPage(SCodeVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<SCodeVo> select(SCodeVo searchCondition) ;

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    SCodeVo selectByid(Long id);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(SCodeEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(SCodeEntity entity);

    /**
     * 通过type查询
     *
     */
    List<SCodeEntity> selectByType(String type, Long equal_id, Long not_equal_id);

    /**
     * 获取编号
     * @param type
     * @return
     */
    UpdateResult<SCodeEntity> createCode(String type);
}
