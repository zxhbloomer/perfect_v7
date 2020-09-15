package com.perfect.core.serviceimpl.sys.platform;

import com.perfect.bean.entity.master.user.MStaffEntity;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.bean.vo.sys.platform.SignUpVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.core.mapper.client.user.MUserMapper;
import com.perfect.core.mapper.master.user.MStaffMapper;
import com.perfect.core.mapper.sys.config.tenant.STenantMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.platform.ISignUpService;
import com.perfect.core.serviceimpl.common.autocode.TenantAutoCodeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-07-04
 */
@Service
public class SignUpServiceImpl extends BaseServiceImpl<MStaffMapper, MStaffEntity> implements ISignUpService {

    @Autowired
    private MStaffMapper mStaffMapper;

    @Autowired
    private MUserMapper mUserMapper;

    @Autowired
    private STenantMapper sTenantMapper;

    @Autowired
    private TenantAutoCodeImpl tenantAutoCode;

    /**
     * 注册
     * @param bean
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean signUp(SignUpVo bean) {
        // 1:调用check
        check(bean);
        // 2:执行注册
        // 2.1:s_tenant插入数据
        STenantEntity sTenantEntity = new STenantEntity();
        sTenantEntity.setName(bean.getTenant());
        sTenantEntity.setSimple_name(bean.getTenant());
        sTenantEntity.setEnable_time(LocalDateTime.now());
        // 有效时间默认为一个月
        sTenantEntity.setDisable_time(LocalDateTime.now().plusMonths(1));
        sTenantEntity.setIs_freeze(false);
        sTenantEntity.setIs_enable(true);
        sTenantEntity.setIs_del(false);
        sTenantEntity.setSerial_no(PerfectConstant.SIGN_UP.TENANT_SERIAL_NO);
        sTenantEntity.setCode(tenantAutoCode.autoCode().getCode());
        // 执行插入
        sTenantMapper.insert(sTenantEntity);

        // 2.2:m_staff插入数据
        MStaffEntity mStaffEntity = new MStaffEntity();
        mStaffEntity.setTenant_id(sTenantEntity.getId());
        mStaffEntity.setName(bean.getAdmin());
        mStaffEntity.setSimple_name(bean.getAdmin());
        mStaffMapper.insert(mStaffEntity);

        // 2.3:m_user插入数据
        MUserEntity mUserEntity = new MUserEntity();
        mUserEntity.setStaff_id(mStaffEntity.getId());
        mUserEntity.setLogin_name(bean.getMobile());
        mUserEntity.setPwd(bean.getEncodePassword());
        mUserEntity.setIs_biz_admin(true);
        mUserEntity.setLogin_author_way("0");
        mUserEntity.setTenant_id(sTenantEntity.getId());
        mUserEntity.setIs_del(false);
        mUserEntity.setType(PerfectDictConstant.DICT_USR_LOGIN_TYPE_ADMIN);
        mUserEntity.setLogin_type(PerfectDictConstant.DICT_SYS_LOGIN_TYPE_MOBILE);
        mUserMapper.insert(mUserEntity);

        // 最后获取到user的id，更新会staff中去
        mStaffMapper.updateById(mStaffEntity);

        bean.setTenant_id(sTenantEntity.getId());

        return true;
    }

    /**
     * 第一步：检查用户名，手机号码是否重复check
     * @param bean
     * @return
     */
    @Override
    public Boolean checkMobile(SignUpVo bean) {
        // 1：m_staff.name是否有重复
        List<MStaffEntity> staff_list = mStaffMapper.selectByName(bean.getMobile(), null, null, null);
        if (staff_list.size() > 0) {
            throw new BusinessException("该手机号码在系统中已经被注册使用，请更换手机号！");
        }
        // 2:m_user.login_name是否有重复
        MUserEntity mUserEntity = mUserMapper.getDataByName(bean.getMobile());
        if(mUserEntity != null){
            throw new BusinessException("该手机号码在系统中已经被注册使用，请更换手机号！");
        }
        return true;
    }

    /**
     * 第二步：检查租户名称、管理员名称，手机号码是否重复check
     * @param bean
     * @return
     */
    @Override
    public Boolean check(SignUpVo bean) {
        // check mobile
        checkMobile(bean);
        // 租户名称
        List<STenantEntity> tenant_list = sTenantMapper.selectByName(bean.getTenant());
        if (tenant_list.size() > 0) {
            throw new BusinessException("该租户名称系统中已经被注册使用，请更换租户名称！");
        }
        // 管理员名称
        List<MStaffEntity> staff_list = mStaffMapper.selectByName(bean.getAdmin(), null, null, null);
        if (staff_list.size() > 0) {
            throw new BusinessException("该手机号码在系统中已经被注册使用，请更换手机号！");
        }
        return true;
    }
}
