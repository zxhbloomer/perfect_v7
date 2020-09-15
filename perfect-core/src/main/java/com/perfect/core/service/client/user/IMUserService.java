package com.perfect.core.service.client.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.master.user.MUserVo;
import com.perfect.bean.vo.master.user.UserInfoVo;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-06-24
 */
public interface IMUserService extends IService<MUserEntity> , UserDetailsService {

    /**
     * 获取use的基本信息
     * @param userName
     * @return
     */
    UserInfoVo getUserInfo(String userName);


    /**
     * 获取userbean
     * @return
     */
    UserSessionBo getUserBean(Long id, String loginOrStaffId);


    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    InsertResult<Integer> insert(MUserEntity entity);

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    UpdateResult<Integer> update(MUserEntity entity);

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    MUserVo selectByid(Long id);

    /**
     * 根据登录的username获取entity
     *
     * @param username
     * @return
     */
    MUserEntity getDataByName(String username);

 }
