package com.perfect.core.service.master.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.master.user.MStaffEntity;
import com.perfect.bean.pojo.result.DeleteResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.org.MStaffPositionVo;
import com.perfect.bean.vo.master.user.MStaffVo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 员工 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
public interface IMStaffService extends IService<MStaffEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<MStaffVo> selectPage(MStaffVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<MStaffVo> select(MStaffVo searchCondition);

    /**
     * 获取所选id的数据
     */
    List<MStaffVo> selectIdsIn(List<MStaffVo> searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<MStaffVo> exportBySelectIdsIn(List<MStaffVo> searchCondition);

    /**
     * 批量物理删除
     * @param searchCondition
     * @return
     */
    DeleteResult<Integer> realDeleteByIdsIn(List<MStaffVo> searchCondition);

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(MStaffVo entity, HttpSession session);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(MStaffVo entity, HttpSession session);

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    MStaffVo selectByid(Long id);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<MStaffVo> searchCondition);

    /**
     * 查询岗位员工
     * @param searchCondition
     * @return
     */
    MStaffPositionVo getPositionStaffData(MStaffPositionVo searchCondition);

    /**
     * 查询岗位员工
     * @param searchCondition
     * @return
     */
    void setPositionStaff(MStaffPositionVo searchCondition);
}
