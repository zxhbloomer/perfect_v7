package com.perfect.core.serviceimpl.client.user;

import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.bo.user.login.MUserBo;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.bean.vo.master.user.MUserVo;
import com.perfect.bean.vo.master.user.UserInfoVo;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.core.mapper.client.user.MUserMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.client.user.IMUserLiteService;
import com.perfect.core.service.client.user.IMUserService;
import com.perfect.core.service.master.user.IMStaffService;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-05-17
 */
@Service
@Slf4j
public class MUserServiceImpl extends BaseServiceImpl<MUserMapper, MUserEntity> implements IMUserService {

    @Autowired
    private MUserMapper mUserMapper;

    @Autowired
    private IMStaffService imStaffService;

    @Autowired
    private ITenantService iTenantService;

    @Autowired
    private IMUserLiteService imUserLiteService;

    /**
     * 登录入口
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUserEntity user = getDataByName(username);

        if (user == null) {
            log.error("您输入的用户名不存在！");
            throw new UsernameNotFoundException("您输入的用户名不存在！");
        }

        //        Clerk clerk = clerkMapper.selectByPrimaryKey(user.getId());
        //        if (clerk == null) {
        //            throw new ClerkNotFoundException("Couldn't found clerk in system");
        //        }
        //
        //        List<Role> roles = userMapper.selectRoles(user.getId());
        //
        List<String> permissions = new ArrayList<>();
//        permissions.addAll(CollectionUtils.arrayToList(new String[]{"ROLE_USER"}));
        permissions.addAll(Arrays.asList(new String[]{"ROLE_USER"}));
        //        for (Role role : roles) {
        //            permissions.addAll(CollectionUtils.arrayToList(role.getPermissions()));
        //        }

        return new MUserBo(
                user.getId(),
                username,
                user.getPwd(),
                AuthorityUtils.createAuthorityList(permissions.toArray(new String[]{})))   // 加载权限的关键部分
                .setUser(user);
    }

    /**
     * 获取use的基本信息
     * @param userName
     * @return
     */
    @Override
    public UserInfoVo getUserInfo(String userName){

        // TODO 测试bean
        UserInfoVo ui = new UserInfoVo();
        ui.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        ui.setIntroduction("我是超级管理员");
        ui.setName("超级管理员");
        ui.setRoles(new String[]{"admin"});
        return ui;
    }

    /**
     * 获取userbean
     *
     * 1：用户 信息
     * 2：员工 信息
     * 3：租户 信息
     *
     * @param loginOrStaffId
     * @return
     */
    @Override
    public UserSessionBo getUserBean(Long id, String loginOrStaffId){
        MUserEntity mUserEntity = null;
        MStaffVo mStaffVo = null;
        if(loginOrStaffId.equals(PerfectConstant.LOGINUSER_OR_STAFF_ID.LOGIN_USER_ID)){
            mUserEntity = mUserMapper.selectById(id);
            mStaffVo = imStaffService.selectByid(mUserEntity.getStaff_id());
        } else {
            mStaffVo = imStaffService.selectByid(id);
            mUserEntity = mUserMapper.selectById(mStaffVo.getUser_id());
        }
        STenantVo sTenantVo = iTenantService.selectByid(mStaffVo != null ? mStaffVo.getTenant_id() : null);
        UserSessionBo userSessionBo = new UserSessionBo();
        /** 设置1：用户信息 */
        userSessionBo.setUser_info(mUserEntity);
        /** 设置2：员工 信息 */
        userSessionBo.setStaff_info(mStaffVo);
        /** 设置3：租户 信息 */
        userSessionBo.setTenant_info(sTenantVo);

        // 设置basebean
        userSessionBo.setAccountId(mUserEntity.getId());
        userSessionBo.setStaff_Id(mStaffVo != null ? mStaffVo.getId() : null);
        userSessionBo.setTenant_Id(mStaffVo != null ? mStaffVo.getTenant_id() : null);

        return userSessionBo;
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(MUserEntity entity) {
        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 插入逻辑保存
        entity.setIs_del(false);

        // 执行插入
        int rtn = mUserMapper.insert(entity);

        // 用户简单重构
        imUserLiteService.reBulidUserLiteData(entity.getId());

        return InsertResultUtil.OK(rtn);
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(MUserEntity entity) {
        // 更新前check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);

        // 执行更新操作
        int rtn = mUserMapper.updateById(entity);

        // 用户简单重构
        imUserLiteService.reBulidUserLiteData(entity.getId());

        return UpdateResultUtil.OK(rtn);
    }

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    @Override
    public MUserVo selectByid(Long id){
        return mUserMapper.selectByid(id, getUserSessionTenantId());
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkLogic(MUserEntity entity, String moduleType){
        return CheckResultUtil.OK();
    }

    /**
     * 根据登录的username获取entity
     *
     * @param username
     * @return
     */
    @Override
    public MUserEntity getDataByName(String username) {
        MUserEntity entity = mUserMapper.getDataByName(username);
        return entity;
    }
}