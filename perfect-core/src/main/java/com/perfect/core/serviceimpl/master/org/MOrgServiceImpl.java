package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.perfect.bean.bo.log.operate.CustomOperateBo;
import com.perfect.bean.bo.log.operate.CustomOperateDetailBo;
import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.entity.master.org.*;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.utils.common.tree.TreeUtil;
import com.perfect.bean.vo.common.component.NameAndValueVo;
import com.perfect.bean.vo.common.component.TreeNode;
import com.perfect.bean.vo.master.org.*;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.common.annotations.LogByIdAnnotion;
import com.perfect.common.annotations.LogByIdsAnnotion;
import com.perfect.common.annotations.OperationLogAnnotion;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.common.enums.OperationEnum;
import com.perfect.common.enums.ParameterEnum;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.ArrayPfUtil;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.common.utils.servlet.ServletUtil;
import com.perfect.core.mapper.master.org.*;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.common.ICommonComponentService;
import com.perfect.core.service.master.org.IMOrgService;
import com.perfect.core.serviceimpl.log.operate.SLogOperServiceImpl;
import com.perfect.core.utils.mybatis.PageUtil;
import com.perfect.core.utils.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  ???????????? ???????????????
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class MOrgServiceImpl extends BaseServiceImpl<MOrgMapper, MOrgEntity> implements IMOrgService {

    @Autowired
    private MOrgTenantGroupMapper oTGMapper;

    @Autowired
    private MOrgGroupCompanyMapper oGCMapper;

    @Autowired
    private MOrgDeptPositionMapper oDPMapper;

    @Autowired
    private MOrgCompanyDeptMapper oCDMapper;

    @Autowired
    private MOrgMapper mapper;

    @Autowired
    private ICommonComponentService iCommonComponentService;

    @Autowired
    private MOrgServiceImpl self;

    @Autowired
    private MStaffOrgServiceImpl mStaffOrgService;

    @Autowired
    private SLogOperServiceImpl sLogOperService;

    /** ??????entity?????? */
//    List<MOrgEntity> entities;

    /**
     * ????????????????????????????????????
     */
    @Override
    public List<MOrgTreeVo> getTreeList(MOrgTreeVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        switch (searchCondition.getType()) {
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT:
                // ????????????
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY:
                // ??????
                String[] company_codes = {
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE,
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE,
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE};
                searchCondition.setCodes(company_codes);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT:
                // ??????
                String[] dept_codes = {
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE,
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE,
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE,
                    PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE
                };
                searchCondition.setCodes(dept_codes);
                // ??????code
                MOrgEntity mOrgEntity = mapper.selectOne(new QueryWrapper<MOrgEntity>()
                    .eq("serial_id",searchCondition.getSerial_id())
                    .eq("serial_type", searchCondition.getSerial_type())
                );
                String code = mOrgEntity.getCode().substring(0,8);
                searchCondition.setCode(code);
                searchCondition.setCurrent_code(mOrgEntity.getCode());
                break;
        };

        // ?????? ??????
        List<MOrgTreeVo> list = mapper.getTreeList(searchCondition);
        List<MOrgTreeVo> rtnList = TreeUtil.getTreeList(list);
        return rtnList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MOrgTreeVo> select(MOrgVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // ?????? ??????
        List<MOrgTreeVo> list = mapper.select(searchCondition);
        List<MOrgTreeVo> rtnList = TreeUtil.getTreeList(list);
        return rtnList;
    }

    /**
     * ???????????????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public MOrgCountsVo getAllOrgDataCount(MOrgVo searchCondition) {
        MOrgCountsVo mOrgCountsVo = mapper.getAllOrgDataCount(searchCondition);
        return mOrgCountsVo;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public List<MOrgTreeVo> getOrgs(MOrgVo searchCondition) {
        List<MOrgTreeVo> listOrg = select(searchCondition);
        return listOrg;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MGroupVo> getGroups(MOrgTreeVo searchCondition) {
        // ????????????
        Page<MGroupEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // ??????page????????????
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        IPage<MGroupVo> listGroup = mapper.getGroupList(pageCondition, searchCondition);
        return listGroup;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MCompanyVo> getCompanies(MOrgTreeVo searchCondition) {
        // ????????????
        Page<MCompanyEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // ??????page????????????
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        IPage<MCompanyVo> listcompany = mapper.getCompanyList(pageCondition, searchCondition);
        return listcompany;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MDeptVo> getDepts(MOrgTreeVo searchCondition) {
        // ????????????
        Page<MDeptVo> pageCondition =
                new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // ??????page????????????
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        IPage<MDeptVo> listDept =  mapper.getDeptList(pageCondition, searchCondition);
        return listDept;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MPositionVo> getPositions(MOrgTreeVo searchCondition) {
        // ????????????
        Page<MDeptVo> pageCondition =
                new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // ??????page????????????
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        IPage<MPositionVo> listPosition =  mapper.getPositionList(pageCondition, searchCondition);
        return listPosition;
    }

    /**
     * ??????????????????
     * @param searchCondition
     * @return
     */
    @Override
    public List<MStaffVo> getStaffs(MOrgVo searchCondition) {
        return null;
    }

    /**
     * ???????????????????????????????????????????????????
     * @param entity ????????????
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_ORG.OPER_INSERT,
        type = OperationEnum.ADD,
        logById = @LogByIdAnnotion(
            name = PerfectConstant.OPERATION.M_ORG.OPER_INSERT,
            type = OperationEnum.ADD,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_ORG.TABLE_NAME,
            id = "#{entity.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(MOrgEntity entity) {
        // ??????entity
        entity.setTenant_id(getUserSessionTenantId());
        switch (entity.getType()) {
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF_SERIAL_TYPE);
                break;
        }

        // ?????????check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);

        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // ???????????????entity
        MOrgEntity parentEntity = getById(entity.getParent_id());
        Integer son_count = parentEntity.getSon_count();
        son_count = (son_count == null ? 0 : son_count)  + 1;
        parentEntity.setSon_count(son_count);
        // ??????????????????????????????
        parentEntity.setC_id(null);
        parentEntity.setC_time(null);
        mapper.updateById(parentEntity);

        // ???????????????code
        String parentCode = parentEntity.getCode();
        // ??????????????????
        // ????????????son_count
        // 0 ??????????????????0
        // 4 ???????????????4
        // d ????????????????????????
        String str = String.format("%04d", son_count);
        entity.setCode(parentCode + str);
        entity.setSon_count(0);

        // ??????????????????
        int insert_result = mapper.insert(entity);

        // ???????????????????????????
        setOrgRelationData(entity,parentEntity);

        return InsertResultUtil.OK(insert_result);
    }

    /**
     * ???????????????????????????
     */
    private void setOrgRelationData(MOrgEntity entity, MOrgEntity parentEntity) {
        // ??????????????????
        switch (entity.getType()) {
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT:
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP:
                updateOTGRelation(entity,parentEntity);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY:
                updateOGCRelation(entity,parentEntity);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT:
                updateOCDRelation(entity,parentEntity);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION:
                updateODPRelation(entity,parentEntity);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF:
                break;
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private void updateOTGRelation(MOrgEntity currentEntity, MOrgEntity parentEntity){
        MOrgTenantGroupEntity oTGEntity = new MOrgTenantGroupEntity();
        oTGEntity.setCurrent_id(currentEntity.getSerial_id());
        oTGEntity.setTenant_id(getUserSessionTenantId());
        oTGEntity.setOrg_id(currentEntity.getId());
        oTGEntity.setOrg_parent_id(currentEntity.getParent_id());
        if(PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE.equals(parentEntity.getSerial_type())) {
            /** ??????????????????????????????????????????????????????????????????m_org_tenant_group */
            oTGEntity.setParent_id(parentEntity.getSerial_id());
            oTGEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE);
            // ???????????????????????????root??????
            MOrgTenantGroupEntity parentOTGEntity = oTGMapper
                .getOTGEntityByCurrentId(parentEntity.getSerial_id(), parentEntity.getTenant_id());
            oTGEntity.setRoot_id(parentOTGEntity.getRoot_id());
        } else {
            /** ?????????????????????????????????????????????????????? */
            oTGEntity.setParent_id(parentEntity.getSerial_id());
            oTGEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE);
            oTGEntity.setRoot_id(currentEntity.getSerial_id());
        }
        /** ??????sort */
        int count = oTGMapper.getOTGRelationCount(currentEntity);
        count = count + 1;
        oTGEntity.setSort(count);
        /** ???????????? */
        oTGMapper.insert(oTGEntity);
        /** ??????counts??????sorts */
        oTGMapper.updateOTGCountAndSort(oTGEntity.getId());
    }

    /**
     * ????????????->????????????
     */
    private void updateOGCRelation(MOrgEntity currentEntity, MOrgEntity parentEntity){
        MOrgGroupCompanyEntity oGCEntity = new MOrgGroupCompanyEntity();
        oGCEntity.setOrg_id(currentEntity.getId());
        oGCEntity.setOrg_parent_id(currentEntity.getParent_id());
        oGCEntity.setCurrent_id(currentEntity.getSerial_id());
        oGCEntity.setTenant_id(getUserSessionTenantId());
        oGCEntity.setParent_id(parentEntity.getSerial_id());
        oGCEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE);
        oGCEntity.setRoot_id(currentEntity.getSerial_id());
        oGCEntity.setRoot_group_id(parentEntity.getSerial_id());
        oGCEntity.setCounts(1);
        oGCEntity.setSort(1);
        oGCMapper.insert(oGCEntity);
    }

    /**
     * ????????????->????????????
     */
    private void updateOCDRelation(MOrgEntity currentEntity, MOrgEntity parentEntity){
        MOrgCompanyDeptEntity oCDEntity = new MOrgCompanyDeptEntity();
        oCDEntity.setOrg_id(currentEntity.getId());
        oCDEntity.setOrg_parent_id(currentEntity.getParent_id());
        oCDEntity.setCurrent_id(currentEntity.getSerial_id());
        oCDEntity.setTenant_id(getUserSessionTenantId());
        if(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE.equals(parentEntity.getSerial_type())) {
            /** ?????????????????????????????????????????????????????????????????? */
            oCDEntity.setParent_id(parentEntity.getSerial_id());
            oCDEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE);
            // ???????????????????????????root??????
            MOrgCompanyDeptEntity parentOCDEntity = oCDMapper
                .getOCDEntityByCurrentId(parentEntity.getSerial_id(), parentEntity.getTenant_id());
            oCDEntity.setRoot_id(parentOCDEntity.getRoot_id());
        } else {
            /** ?????????????????????????????????????????????????????? */
            oCDEntity.setParent_id(parentEntity.getSerial_id());
            oCDEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE);
            oCDEntity.setRoot_id(currentEntity.getSerial_id());
        }
        /** ??????sort */
        int count = oCDMapper.getOCDRelationCount(currentEntity);
        count = count + 1;
        oCDEntity.setSort(count);
        /** ???????????? */
        oCDMapper.insert(oCDEntity);
        /** ??????counts??????sorts */
        oCDMapper.updateOCDCountAndSort(oCDEntity.getId());
        oCDMapper.updateOCDParentData();
    }

    /**
     * ????????????->??????????????????????????????
     */
    private void updateODPRelation(MOrgEntity currentEntity, MOrgEntity parentEntity){
        MOrgDeptPositionEntity oDPEntity = new MOrgDeptPositionEntity();
        oDPEntity.setOrg_id(currentEntity.getId());
        oDPEntity.setOrg_parent_id(currentEntity.getParent_id());
        oDPEntity.setCurrent_id(currentEntity.getSerial_id());
        oDPEntity.setTenant_id(getUserSessionTenantId());
        oDPEntity.setParent_id(parentEntity.getSerial_id());
        oDPEntity.setParent_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE);
        oDPEntity.setRoot_id(currentEntity.getSerial_id());
        oDPEntity.setCounts(1);
        oDPEntity.setSort(1);
        oDPMapper.insert(oDPEntity);
    }

    /**
     * ???????????????????????????????????????????????????
     * @param entity ????????????
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_ORG.OPER_UPDATE,
        type = OperationEnum.UPDATE,
        logById = @LogByIdAnnotion(
            name = PerfectConstant.OPERATION.M_ORG.OPER_UPDATE,
            type = OperationEnum.UPDATE,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_ORG.TABLE_NAME,
            id = "#{entity.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(MOrgEntity entity) {
        // ??????entity
        entity.setTenant_id(((UserSessionBo)ServletUtil.getUserSession()).getTenant_Id());
        switch (entity.getType()) {
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF_SERIAL_TYPE);
                break;
        }

        // ?????????check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // ??????????????????
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * ????????????byid
     * @param id
     * @return
     */
    @Override
    public MOrgVo selectByid(Long id){
        return mapper.selectByid(id, getUserSessionTenantId());
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    public Integer selectNodeInsertStatus(String code, String type) {
        // ?????? ??????
        Integer count = mapper.selectNodeInsertStatus(code, type, getUserSessionTenantId());
        return count;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     *
     * @return
     */
    public Integer getCountBySerial(MOrgEntity entity, Long equal_id, Long not_equal_id) {
        // ?????? ??????
        Integer count = mapper.getCountBySerial(entity, equal_id, not_equal_id);
        return count;
    }

    /**
     * check??????
     * @return
     */
    public CheckResult checkLogic(MOrgEntity entity, String moduleType){
        Integer count = 0;
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // ????????????????????????????????????->??????->??????->??????->??????->??????
                Integer countInsert = this.selectNodeInsertStatus(entity.getCode(),entity.getType());
                if(countInsert > 0){
                    String nodeTypeName = iCommonComponentService.getDictName(PerfectDictConstant.DICT_ORG_SETTING_TYPE, entity.getType());
                    return CheckResultUtil.NG("??????????????????????????????????????????????????????" + "???" + nodeTypeName + "???", countInsert);
                }
                // ?????????????????????????????????????????????
                count = getCountBySerial(entity, null, null);
                if(count > 0){
                    return CheckResultUtil.NG("?????????????????????????????????????????????????????????????????????????????????????????????????????????", count);
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // ????????????????????????????????????->??????->??????->??????->??????->??????
                Integer countUpdate = this.selectNodeInsertStatus(entity.getCode(),entity.getType());
                if(countUpdate > 0){
                    String nodeTypeName = iCommonComponentService.getDictName(PerfectDictConstant.DICT_ORG_SETTING_TYPE, entity.getType());
                    return CheckResultUtil.NG("?????????????????????????????????????????????????????????" + "???" + nodeTypeName + "???", countUpdate);
                }
                // ?????????????????????????????????????????????
                count = getCountBySerial(entity, null, entity.getId());
                if(count > 0){
                    return CheckResultUtil.NG("?????????????????????????????????????????????????????????????????????????????????????????????????????????", count);
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }

    /**
     * ?????????????????????????????????????????????
     * @return
     */
    @Override
    public List<NameAndValueVo> getCorrectTypeByInsertStatus(MOrgVo vo) {
        vo.setTenant_id(getUserSessionTenantId());
        // ?????? ??????
        List<NameAndValueVo> rtn = mapper.getCorrectTypeByInsertStatus(vo);
        return rtn;
    }

    /**
     * ??????
     * @param entity
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_ORG.OPER_DELETE,
        type = OperationEnum.DELETE,
        logById = @LogByIdAnnotion(
            name = PerfectConstant.OPERATION.M_ORG.OPER_DELETE,
            type = OperationEnum.DELETE,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_ORG.TABLE_NAME,
            id = "#{entity.id}"
        )
    )

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteById(MOrgEntity entity) {
        // ?????????????????????
        List<MOrgEntity> rtnList = getDataByCode(entity);
        rtnList.forEach(bean -> {
            // ?????????????????????
            deleteOrgRelation(bean);
            // ?????? ??????
            mapper.deleteById(bean.getId());
        });

        return true;
    }

    /**
     * ???????????????
     * @param entity
     */
    private void deleteOrgRelation(MOrgEntity entity) {
        switch (entity.getType()) {
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT:
                entity.setSerial_type(PerfectDictConstant.DICT_ORG_SETTING_TYPE_TENANT_SERIAL_TYPE);
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_GROUP:
                oTGMapper.delOTGRelation(entity.getSerial_id());
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_COMPANY:
                oGCMapper.delOGCRelation(entity.getSerial_id());
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT:
                oCDMapper.delOCDRelation(entity.getSerial_id());
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION:
                oDPMapper.delODPRelation(entity.getSerial_id());
                break;
            case PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF:
                break;
        }
    }

    /**
     * ??????code????????? like 'code%'????????????????????????????????????
     * @param entity
     * @return
     */
    @Override
    public List<MOrgEntity> getDataByCode(MOrgEntity entity) {
        entity.setTenant_id(getUserSessionTenantId());
        List<MOrgEntity> rtnList = mapper.getDataByCode(entity);
        return rtnList;
    }

    /**
     * ????????????
     * ?????????????????????????????????
     * @param beans
     * @return
     */
    @Override
    public Boolean dragsave(List<MOrgTreeVo> beans) {
        List<MOrgEntity> entities = new ArrayList<>();
        int code = 0;
        List<MOrgEntity> beanList = dragData2List(beans, null ,entities, code);
        /**
         * ??????????????????????????????????????????????????????aop???????????????????????????aop?????????
         */
        return self.dragsave2Db(beanList);
    }

    /**
     * ????????????
     * @param list
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_ORG.OPER_DRAG_DROP,
        type = OperationEnum.DRAG_DROP,
        logByIds = @LogByIdsAnnotion(
            name = PerfectConstant.OPERATION.M_ORG.OPER_DRAG_DROP,
            type = OperationEnum.DRAG_DROP,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_ORG.TABLE_NAME,
            id_position = ParameterEnum.FIRST,
            ids = "#{beans.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    public Boolean dragsave2Db(List<MOrgEntity> list){
        // ????????????
        for (MOrgEntity entity : list) {
            if(entity.getParent_id() != null){
                setParentSonCount(list, entity.getParent_id());
            }
        }
        // ????????????
        for (MOrgEntity entity : list) {
            entity.setSon_count(entity.getSon_count() == null ? 0 : entity.getSon_count());
            entity.setU_id(SecurityUtil.getLoginUser_id());
            entity.setU_time(LocalDateTime.now());
            mapper.updateDragSave(entity);
        }

        Long tenant_id = getUserSessionTenantId();
        oTGMapper.delAll(tenant_id);
        oGCMapper.delAll(tenant_id);
        oDPMapper.delAll(tenant_id);
        oCDMapper.delAll(tenant_id);

        // ???????????????????????????
        for (MOrgEntity entity : list) {
            /** ???????????????entity */
            MOrgEntity currentEntity = getById(entity.getId());
            MOrgEntity parentEntity = getById(entity.getParent_id());
            /** ??????????????????????????? */
            setOrgRelationData(currentEntity,parentEntity);
        }

        return true;
    }

    /**
     * ??????????????????
     * @return
     */
    private List<MOrgEntity> setParentSonCount(List<MOrgEntity> entities, Long parent_id) {
        for(MOrgEntity entity : entities){
            if(entity.getId().equals(parent_id)){
                entity.setSon_count(entity.getSon_count() == null ? 1 : entity.getSon_count() + 1);
            }
        }
        return entities;
    }

    /**
     * ??????????????????
     * @param beans         ????????????beans
     * @param parent_bean   ?????????bean
     * @param entities      ??????????????????list bean
     * @param code          ???
     * @return
     */
    private List<MOrgEntity> dragData2List(List<? extends TreeNode> beans, MOrgEntity parent_bean, List<MOrgEntity> entities, int code) {
        for (TreeNode bean : beans) {
            code = code + 1;
            MOrgEntity entity = new MOrgEntity();
            entity.setId(bean.getId());
            entity.setParent_id(bean.getParent_id());
            if(parent_bean == null) {
                entity.setCode(String.format("%04d", code));
            } else {
                entity.setCode(parent_bean.getCode() + String.format("%04d", code));
            }
            entities.add(entity);
            if(bean.getChildren().size() !=0){
                dragData2List(bean.getChildren(), entity, entities, 0);
            }
        }
        return entities;
    }

    /**
     * ???????????????????????????????????????
     * @return
     */
    @Override
    public MStaffPositionTransferVo getStaffTransferList(MStaffTransferVo condition) {

        MStaffPositionTransferVo rtn = new MStaffPositionTransferVo();
        // ??????????????????
        rtn.setStaff_all(mapper.getAllStaffTransferList(condition));
        // ???????????????????????????????????????
        List<Long> rtnList = mapper.getUsedStaffTransferList(condition);
        rtn.setStaff_positions(rtnList.toArray(new Long[rtnList.size()]));
        return rtn;
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * @param bean ??????id list
     * @return
     */
    @Override
    public MStaffPositionTransferVo setStaffTransfer(MStaffTransferVo bean) {
        // ????????????bean?????????
        CustomOperateBo cobo = new CustomOperateBo();
        cobo.setName(PerfectConstant.OPERATION.M_STAFF_ORG.OPER_POSITION_STAFF);
        cobo.setPlatform(PerfectConstant.PLATFORM.PC);
        cobo.setType(OperationEnum.BATCH_UPDATE_INSERT_DELETE);


        // ??????????????????????????????list
        List<MStaffPositionOperationVo> deleteMemgerList = mapper.selete_delete_member(bean);
        // ??????????????????????????????list
        List<MStaffPositionOperationVo> insertMemgerList = mapper.selete_insert_member(bean);

        // ??????????????????????????????????????????
        return self.saveMemberList(deleteMemgerList, insertMemgerList, cobo, bean);
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * @param deleteMemberList
     * @param insertMemgerList
     * @param cobo
     * @param bean
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MStaffPositionTransferVo saveMemberList(List<MStaffPositionOperationVo> deleteMemberList,
        List<MStaffPositionOperationVo> insertMemgerList,
        CustomOperateBo cobo,
        MStaffTransferVo bean) {

        List<CustomOperateDetailBo> detail = new ArrayList<>();

        // ---------------------------------???????????? ?????? start-----------------------------------------------------
        // ????????????????????????????????????
        for(MStaffPositionOperationVo vo : deleteMemberList) {
            CustomOperateDetailBo<MStaffPositionOperationVo> bo = new CustomOperateDetailBo<>();
            bo.setName(cobo.getName());
            bo.setType(OperationEnum.DELETE);
            bo.setTable_name(PerfectConstant.OPERATION.M_STAFF_ORG.TABLE_NAME);
            bo.setNewData(null);
            bo.setOldData(vo);
            setColumnsMap(bo);
            detail.add(bo);
        }
        // ---------------------------------???????????? ?????? end-----------------------------------------------------

        // ?????????????????????
        List<MStaffOrgEntity> delete_list =
            BeanUtilsSupport.copyProperties(deleteMemberList, MStaffOrgEntity.class, new String[] {"c_time", "u_time"});
        List<Long> ids = Lists.newArrayList();
        delete_list.forEach(beans -> {
            ids.add(beans.getId());
        });
        if (ArrayPfUtil.isNotEmpty(ids)) {
            mStaffOrgService.removeByIds(ids);
        }

        // ?????????????????????
        Long[] staff_positions = new Long[insertMemgerList.size()];
        int i = 0;
        List<MStaffOrgEntity> mStaffOrgEntities = new ArrayList<>();
        for( MStaffPositionOperationVo vo : insertMemgerList ) {
            MStaffOrgEntity mStaffOrgEntity = new MStaffOrgEntity();
            mStaffOrgEntity.setStaff_id(vo.getId());
            mStaffOrgEntity.setSerial_id(bean.getPosition_id());
            mStaffOrgEntity.setSerial_type(PerfectDictConstant.DICT_SYS_CODE_TYPE_M_POSITION);
            mStaffOrgEntity.setTenant_id(bean.getTenant_id());
            mStaffOrgEntities.add(mStaffOrgEntity);

            staff_positions[i] = vo.getId();
            i = i + 1;
        }

        mStaffOrgService.saveBatch(mStaffOrgEntities);

        // ---------------------------------???????????? ?????? start-----------------------------------------------------
        // ?????????????????????
        MStaffTransferVo condition = new MStaffTransferVo();
        condition.setTenant_id(bean.getTenant_id());
        condition.setPosition_id(bean.getPosition_id());
        condition.setStaff_positions(staff_positions);
        List<MStaffPositionOperationVo> seleteMemgerList = mapper.selete_member(bean);
        for(MStaffPositionOperationVo vo: seleteMemgerList) {
            // ?????????????????????????????????
            CustomOperateDetailBo<MStaffPositionOperationVo> bo = new CustomOperateDetailBo<>();
            bo.setName(cobo.getName());
            bo.setType(OperationEnum.ADD);
            bo.setTable_name(PerfectConstant.OPERATION.M_STAFF_ORG.TABLE_NAME);
            bo.setNewData(vo);
            bo.setOldData(null);
            setColumnsMap(bo);
            detail.add(bo);
        }
        cobo.setDetail(detail);
        // ---------------------------------???????????? ?????? end-----------------------------------------------------

        // ??????????????????
        sLogOperService.save(cobo);

        // ???????????????????????????
        // ???????????????????????????????????????
        List<Long> rtnList = mapper.getUsedStaffTransferList(condition);
        MStaffPositionTransferVo mStaffPositionTransferVo = new MStaffPositionTransferVo();
        mStaffPositionTransferVo.setStaff_positions_count(rtnList.size());
        return mStaffPositionTransferVo;
    }

    /**
     * ??????????????????????????????
     */
    private void setColumnsMap(CustomOperateDetailBo<MStaffPositionOperationVo> bean){
        Map<String, String> columns = new ConcurrentHashMap<>();
        columns.put("staff_name", "????????????");
        columns.put("position_name", "????????????");
        columns.put("c_id", "?????????id");
        columns.put("c_time", "????????????");
        columns.put("u_id", "?????????id");
        columns.put("u_time", "????????????");
        bean.setColumns(columns);
    }

    /**
     * ???????????????????????????
     *
     * @param searchCondition
     * @return
     */
    @Override
    public MStaffTabVo selectStaff(MStaffTabDataVo searchCondition) {
        MStaffTabVo mStaffTabVo = new MStaffTabVo();
        searchCondition.setTenant_id(getUserSessionTenantId());
        // ????????????????????????
        mStaffTabVo.setCurrentOrgStaffCount(getCurrentOrgStaffCount(searchCondition));

        /**
         * ?????????
         * ??????????????????????????????????????????
         * 0:????????????
         * 1???????????????
         * */
        if (searchCondition.getActive_tabs_index() == 1){
            // 1???????????????
            mStaffTabVo.setList(mapper.getAllOrgStaff(searchCondition));
        } else {
            // 0:????????????
            mStaffTabVo.setList(mapper.selectStaff(searchCondition));
        }

        // count ??????
        mStaffTabVo.setAllOrgStaffCount(getAllOrgStaffCount(searchCondition));
        return mStaffTabVo;
    }

    /**
     * ????????????????????????count
     */
    @Override
    public Integer getCurrentOrgStaffCount(MStaffTabDataVo searchCondition) {
        return mapper.getCurrentOrgStaffCount(searchCondition);
    }

    /**
     * ??????????????????count
     */
    @Override
    public Integer getAllOrgStaffCount(MStaffTabDataVo searchCondition) {
        /**
         * ???????????????????????????
         * 1:??????code??????????????????0001xxxx|xxxx|??????4??????????????????????????????????????????4???
         * 2???????????????code??????
         *
         * 2020.04.26 updated????????????????????????????????????????????????????????????=???????????????
         */
//        String _code = searchCondition.getCode();
//        _code = StrUtil.sub(_code, 0, 4);
//        searchCondition.setCode(_code);
        return mapper.getAllOrgStaffCount(searchCondition);
    }
}
