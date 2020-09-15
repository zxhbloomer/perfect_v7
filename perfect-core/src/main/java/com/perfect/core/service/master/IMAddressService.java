package com.perfect.core.service.master;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.MAddressEntity;
import com.perfect.bean.pojo.result.DeleteResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.MAddressVo;
import com.perfect.bean.vo.sys.config.module.SModuleButtonVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 集团主表 服务类 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
public interface IMAddressService extends IService<MAddressEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<MAddressVo> selectPage(MAddressVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<MAddressVo> select(MAddressVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MAddressEntity> selectIdsIn(List<MAddressEntity> searchCondition) ;

    /**
     * 批量物理删除
     * @param searchCondition
     * @return
     */
    DeleteResult<Integer> realDeleteByIdsIn(List<MAddressVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(MAddressEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(MAddressEntity entity);

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    MAddressVo selectByid(Long id);
}
