package com.perfect.core.service.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.org.MCompanyEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.org.MCompanyVo;
import com.perfect.bean.vo.master.org.MGroupVo;

import java.util.List;

/**
 * <p>
 * 公司主表 服务类 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
public interface IMCompanyService extends IService<MCompanyEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<MCompanyVo> selectPage(MCompanyVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<MCompanyVo> select(MCompanyVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MCompanyVo> selectIdsInForExport(List<MCompanyVo> searchCondition) ;

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<MCompanyVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(MCompanyEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(MCompanyEntity entity);

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    MCompanyVo selectByid(Long id);
}
