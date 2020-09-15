package com.perfect.core.serviceimpl.master.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.org.MStaffOrgEntity;
import com.perfect.bean.entity.master.user.MStaffEntity;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.DeleteResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.DeleteResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.org.MPositionVo;
import com.perfect.bean.vo.master.org.MStaffPositionCountsVo;
import com.perfect.bean.vo.master.org.MStaffPositionVo;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.mapper.client.user.MUserMapper;
import com.perfect.core.mapper.master.org.MStaffOrgMapper;
import com.perfect.core.mapper.master.user.MStaffMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.master.user.IMStaffService;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 员工 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Service
public class MStaffServiceImpl extends BaseServiceImpl<MStaffMapper, MStaffEntity> implements IMStaffService {

    @Autowired
    private MStaffMapper mapper;

    @Autowired
    private MUserMapper mUserMapper;

    @Autowired
    private MStaffOrgMapper mStaffOrgMapper;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MStaffVo> selectPage(MStaffVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 分页条件
        Page<MStaffEntity> pageCondition =
                new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        return mapper.selectPage(pageCondition, searchCondition);
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MStaffVo> select(MStaffVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 查询 数据
        List<MStaffVo> list = mapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MStaffVo> selectIdsIn(List<MStaffVo> searchCondition) {
        // 查询 数据
        List<MStaffVo> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取所选id的数据
     */
    @Override
    public List<MStaffVo> exportBySelectIdsIn(List<MStaffVo> searchCondition) {
        // 查询 数据
        List<MStaffVo> list = mapper.exportSelectIdsIn(searchCondition, getUserSessionTenantId());
        return list;
    }

    /**
     * 批量删除复原
     *
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeleteResult<Integer> realDeleteByIdsIn(List<MStaffVo> searchCondition) {
        List<Long> idList = new ArrayList<>();
        searchCondition.forEach(bean -> {
            idList.add(bean.getId());
        });
        int result=mapper.deleteBatchIds(idList);
        return DeleteResultUtil.OK(result);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param vo 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(MStaffVo vo, HttpSession session) {
        // 分拆entity
        MStaffEntity mStaffEntity = (MStaffEntity) BeanUtilsSupport.copyProperties(vo, MStaffEntity.class);
        MUserEntity mUserEntity = (MUserEntity) BeanUtilsSupport.copyProperties(vo.getRealUser(), MUserEntity.class);

        // 插入前check，员工表check
        CheckResult cr1 = checkStaffEntity(mStaffEntity, CheckResult.INSERT_CHECK_TYPE);
        if (cr1.isSuccess() == false) {
            throw new BusinessException(cr1.getMessage());
        }
        // 插入前check，账号表check
        CheckResult cr2 = checkUserEntity(mUserEntity, CheckResult.INSERT_CHECK_TYPE);
        if (cr2.isSuccess() == false) {
            throw new BusinessException(cr2.getMessage());
        }

        // 部分默认值设置
        mStaffEntity.setIs_del(mStaffEntity.getIs_del() == null ? false : mStaffEntity.getIs_del());

        mUserEntity.setIs_del(mUserEntity.getIs_del() == null ? false : mUserEntity.getIs_del());
        mUserEntity.setIs_lock(mUserEntity.getIs_lock() == null ? false : mUserEntity.getIs_lock());
        mUserEntity.setIs_enable(mUserEntity.getIs_enable() == null ? true : mUserEntity.getIs_enable());
        mUserEntity.setIs_biz_admin(mUserEntity.getIs_biz_admin() == null ? false : mUserEntity.getIs_biz_admin());
        mUserEntity.setIs_changed_pwd(mUserEntity.getIs_changed_pwd() == null ? false : mUserEntity.getIs_changed_pwd());
        mUserEntity.setTenant_id(getUserSessionTenantId());

        // 插入逻辑保存
        mUserMapper.insert(mUserEntity);
        mStaffEntity.setTenant_id(getUserSessionTenantId());
        mapper.insert(mStaffEntity);

        // 增添关系
        mUserEntity.setStaff_id(mStaffEntity.getId());
        mStaffEntity.setUser_id(mUserEntity.getId());
        mStaffEntity.setIs_del(false);
        mUserEntity.setIs_del(false);

        // 判断session中的密码是否有设置，如果有设置则获取并保存
        Object sessionObj = session.getAttribute(PerfectConstant.SESSION_KEY_USER_PASSWORD);
        if(sessionObj != null) {
            String encodePsd = (String) sessionObj;
            mUserEntity.setPwd(encodePsd);
            // 删除
            session.removeAttribute(PerfectConstant.SESSION_KEY_USER_PASSWORD);
        }

        // 更新保存
        mStaffEntity.setC_id(null);
        mStaffEntity.setC_time(null);
        mStaffEntity.setTenant_id(getUserSessionTenantId());
        mapper.updateById(mStaffEntity);

        mUserEntity.setC_id(null);
        mUserEntity.setC_time(null);
        mUserEntity.setTenant_id(getUserSessionTenantId());
        mUserMapper.updateById(mUserEntity);


        // 判断企业、部门字段，对用户组织关系表进行更新
        updateStaffOrg(mStaffEntity);

        // 返回值确定
        vo.setId(mStaffEntity.getId());
        return InsertResultUtil.OK(1);
    }

    /**
     * 更新用户组织机构关系表
     */
    private void updateStaffOrg(MStaffEntity entity) {
        // 删除关系表：企业
        mStaffOrgMapper.delete(new QueryWrapper<MStaffOrgEntity>()
            .eq("staff_id",entity.getId())
            .eq("serial_type", PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE)
        );
        // 删除关系表：部门
        mStaffOrgMapper.delete(new QueryWrapper<MStaffOrgEntity>()
            .eq("staff_id",entity.getId())
            .eq("serial_type", PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE)
        );
        // 插入关系表：企业
        if(entity.getCompany_id() != null){
            MStaffOrgEntity companyStaffEntity = new MStaffOrgEntity();
            companyStaffEntity.setStaff_id(entity.getId());
            companyStaffEntity.setSerial_id(entity.getCompany_id());
            companyStaffEntity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE);
            companyStaffEntity.setTenant_id(getUserSessionTenantId());
            mStaffOrgMapper.insert(companyStaffEntity);
        }
        // 插入关系表：部门
        if(entity.getDept_id() != null){
            MStaffOrgEntity deptStaffEntity = new MStaffOrgEntity();
            deptStaffEntity.setStaff_id(entity.getId());
            deptStaffEntity.setSerial_id(entity.getDept_id());
            deptStaffEntity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE);
            deptStaffEntity.setTenant_id(getUserSessionTenantId());
            mStaffOrgMapper.insert(deptStaffEntity);
        }
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param vo 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(MStaffVo vo, HttpSession session) {
        // 分拆entity
        MStaffEntity mStaffEntity = (MStaffEntity) BeanUtilsSupport.copyProperties(vo, MStaffEntity.class);
        MUserEntity mUserEntity = (MUserEntity) BeanUtilsSupport.copyProperties(vo.getRealUser(), MUserEntity.class);

        // 插入前check
        CheckResult cr1 = checkStaffEntity(mStaffEntity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr1.isSuccess() == false) {
            throw new BusinessException(cr1.getMessage());
        }
        CheckResult cr2 = checkUserEntity(mUserEntity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr2.isSuccess() == false) {
            throw new BusinessException(cr2.getMessage());
        }

        // 判断session中的密码是否有设置，如果有设置则获取并保存
        Object sessionObj = session.getAttribute(PerfectConstant.SESSION_KEY_USER_PASSWORD);
        if(sessionObj != null) {
            String encodePsd = (String) sessionObj;
            // 备份旧的密码
            mUserEntity.setPwd_his_pwd(mUserEntity.getPwd());
            // 保存新的密码
            mUserEntity.setPwd(encodePsd);
            // 删除
            session.removeAttribute(PerfectConstant.SESSION_KEY_USER_PASSWORD);
        }

        mUserEntity.setStaff_id(mStaffEntity.getId());
        mUserEntity.setTenant_id(getUserSessionTenantId());
        if(mStaffEntity.getUser_id() == null){
            mUserMapper.insert(mUserEntity);
        } else {
            mUserEntity.setC_id(null);
            mUserEntity.setC_time(null);
            mUserMapper.updateById(mUserEntity);
        }
        mStaffEntity.setUser_id(mUserEntity.getId());
        mStaffEntity.setC_id(null);
        mStaffEntity.setC_time(null);
        mapper.updateById(mStaffEntity);

        // 判断企业、部门字段，对用户组织关系表进行更新
        updateStaffOrg(mStaffEntity);


        // 设置返回值
        vo.setId(mStaffEntity.getId());

        // 更新逻辑保存
        return UpdateResultUtil.OK(1);
    }

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    @Override
    public MStaffVo selectByid(Long id){
        MStaffVo searchCondition = new MStaffVo();
        searchCondition.setId(id);
        searchCondition.setTenant_id(getUserSessionTenantId());
        return mapper.selectByid(searchCondition);
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Override
    public void deleteByIdsIn(List<MStaffVo> searchCondition){
        List<MStaffVo> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        list.forEach(bean -> {
            bean.setIs_del(!bean.getIs_del());
        });
        List<MStaffEntity> entityList = BeanUtilsSupport.copyProperties(list, MStaffEntity.class);
        super.saveOrUpdateBatch(entityList, 500);
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MStaffEntity> selectBySimpleName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MStaffEntity> list = mapper.selectBySimpleName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 查询by name，返回结果
     *
     * @return
     */
    public List<MStaffEntity> selectByName(String val, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MStaffEntity> list = mapper.selectByName(val, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkStaffEntity(MStaffEntity entity, String moduleType){
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 员工姓名重复性check
                List<MStaffEntity> nameList_insertCheck = selectByName(entity.getName(), null, null);
                if (nameList_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：员工姓名【"+ entity.getName() +"】出现重复", nameList_insertCheck);
                }

                // 员工简称重复性check
                List<MStaffEntity> _insertCheck = selectBySimpleName(entity.getSimple_name(), null, null);
                if (_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：员工姓名简称【"+ entity.getSimple_name() +"】出现重复", _insertCheck);
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 员工姓名重复性check
                List<MStaffEntity> nameList_updCheck = selectByName(entity.getName(), null, entity.getId());
                if (nameList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：员工姓名【"+ entity.getName() +"】出现重复", nameList_updCheck);
                }

                // 员工简称重复性check
                List<MStaffEntity> simpleNameList_updCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());
                if (simpleNameList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：员工姓名简称【"+ entity.getSimple_name() +"】出现重复", simpleNameList_updCheck);
                }
                break;
            case CheckResult.DELETE_CHECK_TYPE:
                /** 如果逻辑删除为false，表示为：页面点击了删除操作 */
                if(entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                // 是否被使用的check，如果被使用则不能删除
                int count = mapper.isExistsInOrg(entity);
                if(count > 0){
                    return CheckResultUtil.NG("删除出错：该员工【"+ entity.getSimple_name() +"】在组织机构中正在使用！", count);
                }
                break;
            case CheckResult.UNDELETE_CHECK_TYPE:
                // 员工姓名重复性check
                List<MStaffEntity> nameList_undelete_Check = selectByName(entity.getName(), null, entity.getId());
                if (nameList_undelete_Check.size() >= 1) {
                    CheckResultUtil.NG("复原出错：该员工【"+ entity.getName() +"】在组织机构数据中正在被使用，复原这条数据会造成数据重复！", entity.getName());
                }

                // 员工简称重复性check
                List<MStaffEntity> simpleNameList_undelete_Check = selectBySimpleName(entity.getName(), null, null);
                if (simpleNameList_undelete_Check.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：该员工【"+ entity.getSimple_name() +"】在组织机构数据中正在被使用，复原这条数据会造成数据重复！", entity.getName());
                }
                break;
        }
        return CheckResultUtil.OK();
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkUserEntity(MUserEntity entity, String moduleType){
        // 登录人名称不能重复
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                if(entity.getIs_enable()){
                    List<MUserEntity> listValue_insertCheck = selectLoginName(entity.getLogin_name(), null, null);
                    // 新增场合，不能重复
                    if (listValue_insertCheck.size() >= 1) {
                        // 模块编号不能重复
                        return CheckResultUtil.NG("新增保存出错：登录用户名【"+ entity.getLogin_name() +"】出现重复！", listValue_insertCheck);
                    }
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                if(entity.getIs_enable() == null || entity.getIs_enable()){
                    List<MUserEntity> listValue_updCheck = selectLoginName(entity.getLogin_name(), null, entity.getId());
                    // 更新场合，不能重复设置
                    if (listValue_updCheck.size() >= 1) {
                        // 模块编号不能重复
                        return CheckResultUtil.NG("更新保存出错：登录用户名【"+ entity.getLogin_name() +"】出现重复！", listValue_updCheck);
                    }
                }
                break;
            case CheckResult.DELETE_CHECK_TYPE:
                break;
            case CheckResult.UNDELETE_CHECK_TYPE:
                /** 如果逻辑删除为true，表示为：页面点击了删除操作 */
                if(!entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                if(entity.getIs_enable()){
                    List<MUserEntity> listValue_updCheck = selectLoginName(entity.getLogin_name(), null, entity.getId());
                    // 更新场合，不能重复设置
                    if (listValue_updCheck.size() >= 1) {
                        // 模块编号不能重复
                        return CheckResultUtil.NG("复原出错：登录用户名【"+ entity.getLogin_name() +"】出现重复！", listValue_updCheck);
                    }
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }

    /**
     * 查询 by login_name
     * @param login_name
     * @param equal_id
     * @param not_equal_id
     * @return
     */
    public List<MUserEntity> selectLoginName(String login_name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MUserEntity> list = mUserMapper.selectLoginName(login_name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 查询岗位员工
     * @param searchCondition
     * @return
     */
    @Override
    public MStaffPositionVo getPositionStaffData(MStaffPositionVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 分页条件
        Page<MStaffEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());

        MStaffPositionVo mStaffPositionVo = new MStaffPositionVo();
        mStaffPositionVo.setId(searchCondition.getId());
        IPage<MPositionVo> list = mapper.getPositionStaffData(pageCondition, searchCondition);
        List<MStaffPositionCountsVo> counts = mapper.getPositionStaffDataCount(searchCondition);
        mStaffPositionVo.setAll(counts.get(0).getCount());
        mStaffPositionVo.setSettled(counts.get(1).getCount());
        mStaffPositionVo.setUnsettled(counts.get(2).getCount());
        mStaffPositionVo.setList(list);
        return  mStaffPositionVo;
    }

    /**
     * 查询岗位员工
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setPositionStaff(MStaffPositionVo searchCondition) {
        /** 用户组织机构关系表:m_staff_org 插入数据 */
        if(searchCondition.getPosition_settled()){
            // 设置了岗位
            MStaffOrgEntity entity = new MStaffOrgEntity();
            entity.setStaff_id(searchCondition.getId());
            entity.setSerial_id(searchCondition.getPosition_id());
            entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE);
            entity.setTenant_id(getUserSessionTenantId());
            mStaffOrgMapper.insert(entity);
        } else {
            // 取消了岗位
            mStaffOrgMapper.delete(new QueryWrapper<MStaffOrgEntity>()
                .eq("staff_id",searchCondition.getId())
                .eq("serial_id",searchCondition.getPosition_id())
                .eq("serial_type", PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE)
            );
        }
    }
}
