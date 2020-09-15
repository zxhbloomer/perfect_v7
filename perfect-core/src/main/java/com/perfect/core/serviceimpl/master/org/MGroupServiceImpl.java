package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.org.MGroupEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.org.MGroupVo;
import com.perfect.common.annotations.LogByIdAnnotion;
import com.perfect.common.annotations.LogByIdsAnnotion;
import com.perfect.common.annotations.OperationLogAnnotion;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.enums.OperationEnum;
import com.perfect.common.enums.ParameterEnum;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.string.StringUtil;
import com.perfect.core.mapper.master.org.MGroupMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.master.org.IMGroupService;
import com.perfect.core.serviceimpl.common.autocode.MGroupAutoCodeImpl;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  集团主表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class MGroupServiceImpl extends BaseServiceImpl<MGroupMapper, MGroupEntity> implements IMGroupService {

    @Autowired
    private MGroupMapper mapper;

    @Autowired
    private MGroupAutoCodeImpl autoCode;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MGroupVo> selectPage(MGroupVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 分页条件
        Page<MGroupEntity> pageCondition =
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
    public List<MGroupVo> select(MGroupVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 查询 数据
        List<MGroupVo> list = mapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MGroupVo> selectIdsInForExport(List<MGroupVo> searchCondition) {
        // 查询 数据
        List<MGroupVo> list = mapper.selectIdsInForExport(searchCondition, getUserSessionTenantId());
        return list;
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_GROUP.OPER_LOGIC_DELETE,
        type = OperationEnum.LOGIC_DELETE,
        logByIds = @LogByIdsAnnotion(
            name = PerfectConstant.OPERATION.M_GROUP.OPER_LOGIC_DELETE,
            type = OperationEnum.LOGIC_DELETE,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_GROUP.TABLE_NAME,
            id_position = ParameterEnum.FIRST,
            ids = "#{searchCondition.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdsIn(List<MGroupVo> searchCondition) {
        List<MGroupEntity> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        for(MGroupEntity entity : list) {
            CheckResult cr;
            if(entity.getIs_del()){
                /** 如果逻辑删除为true，表示为：页面点击了复原操作 */
                cr = checkLogic(entity, CheckResult.UNDELETE_CHECK_TYPE);
            } else {
                /** 如果逻辑删除为false，表示为：页面点击了删除操作 */
                cr = checkLogic(entity, CheckResult.DELETE_CHECK_TYPE);
            }
            if (cr.isSuccess() == false) {
                throw new BusinessException(cr.getMessage());
            }
            entity.setIs_del(!entity.getIs_del());
            entity.setTenant_id(getUserSessionTenantId());
        }
        saveOrUpdateBatch(list, 500);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_GROUP.OPER_INSERT,
        type = OperationEnum.ADD,
        logById = @LogByIdAnnotion(
            name = PerfectConstant.OPERATION.M_GROUP.OPER_INSERT,
            type = OperationEnum.ADD,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_GROUP.TABLE_NAME,
            id = "#{entity.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(MGroupEntity entity) {
        entity.setTenant_id(getUserSessionTenantId());
        // 编号为空则自动生成编号
        if(StringUtil.isEmpty(entity.getCode())){
            entity.setCode(autoCode.autoCode().getCode());
        }
        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 插入逻辑保存
        entity.setIs_del(false);
        return InsertResultUtil.OK(mapper.insert(entity));
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    @OperationLogAnnotion(
        name = PerfectConstant.OPERATION.M_GROUP.OPER_UPDATE,
        type = OperationEnum.UPDATE,
        logById = @LogByIdAnnotion(
            name = PerfectConstant.OPERATION.M_GROUP.OPER_UPDATE,
            type = OperationEnum.UPDATE,
            oper_info = "",
            table_name = PerfectConstant.OPERATION.M_GROUP.TABLE_NAME,
            id = "#{entity.id}"
        )
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(MGroupEntity entity) {
        entity.setTenant_id(getUserSessionTenantId());
        // 更新前check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param code
     * @return
     */
    public List<MGroupEntity> selectByCode(String code, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MGroupEntity> list = mapper.selectByCode(code, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MGroupEntity> selectByName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MGroupEntity> list = mapper.selectByName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MGroupEntity> selectBySimpleName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MGroupEntity> list = mapper.selectBySimpleName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkLogic(MGroupEntity entity, String moduleType){
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                List<MGroupEntity> codeList_insertCheck = selectByCode(entity.getCode(), null, null);
                List<MGroupEntity> nameList_insertCheck = selectByName(entity.getName(), null, null);
                List<MGroupEntity> simple_name_insertCheck = selectBySimpleName(entity.getSimple_name(), null, null);
                if (codeList_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：集团编号【"+ entity.getCode() +"】出现重复", entity.getCode());
                }
                if (nameList_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：集团全称【"+ entity.getName() +"】出现重复", entity.getName());
                }
                if (simple_name_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：集团简称【"+ entity.getSimple_name() +"】出现重复", entity.getSimple_name());
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合，不能重复设置
                List<MGroupEntity> codeList_updCheck = selectByCode(entity.getCode(), null, entity.getId());
                List<MGroupEntity> nameList_updCheck = selectByName(entity.getName(), null, entity.getId());
                List<MGroupEntity> simple_name_updCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：集团编号【"+ entity.getCode() +"】出现重复！", entity.getCode());
                }
                if (nameList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：集团全称【"+ entity.getName() +"】出现重复！", entity.getName());
                }
                if (simple_name_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：集团简称【"+ entity.getSimple_name() +"】出现重复！", entity.getSimple_name());
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
                    return CheckResultUtil.NG("删除出错：该集团【"+ entity.getSimple_name() +"】在组织机构中正在被使用，不能删除！", count);
                }
                break;
            case CheckResult.UNDELETE_CHECK_TYPE:
                /** 如果逻辑删除为true，表示为：页面点击了删除操作 */
                if(!entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                // 更新场合，不能重复设置
                List<MGroupEntity> codeList_delCheck = selectByCode(entity.getCode(), null, entity.getId());
                List<MGroupEntity> nameList_delCheck = selectByName(entity.getName(), null, entity.getId());
                List<MGroupEntity> simple_name_delCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_delCheck.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：复原集团编号【"+ entity.getCode() +"】这条数据会造成数据重复！", entity.getCode());
                }
                if (nameList_delCheck.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：复原集团全称【"+ entity.getName() +"】这条数据会造成数据重复！", entity.getName());
                }
                if (simple_name_delCheck.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：复原集团简称【"+ entity.getSimple_name() +"】这条数据会造成数据重复！", entity.getSimple_name());
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }

    /**
     *
     * 根据id获取数据
     *
     * @param id
     * @return
     */
    @Override
    public MGroupVo selectByid(Long id) {
        // 查询 数据
        return mapper.selectId(id);
    }
}
