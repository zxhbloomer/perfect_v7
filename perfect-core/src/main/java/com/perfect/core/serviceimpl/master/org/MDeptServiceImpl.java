package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.org.MDeptEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.org.MDeptVo;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.string.StringUtil;
import com.perfect.core.mapper.master.org.MDeptMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.master.org.IMDeptService;
import com.perfect.core.serviceimpl.common.autocode.MDeptAutoCodeServiceImpl;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  部门主表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class MDeptServiceImpl extends BaseServiceImpl<MDeptMapper, MDeptEntity> implements IMDeptService {

    @Autowired
    private MDeptMapper mapper;
    @Autowired
    private MDeptAutoCodeServiceImpl autoCode;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MDeptVo> selectPage(MDeptVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 分页条件
        Page<MDeptEntity> pageCondition =
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
    public List<MDeptVo> select(MDeptVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 查询 数据
        List<MDeptVo> list = mapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MDeptEntity> selectIdsIn(List<MDeptVo> searchCondition) {
        // 查询 数据
        List<MDeptEntity> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据,导出用
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MDeptVo> selectIdsInForExport(List<MDeptVo> searchCondition) {
        // 查询 数据
        List<MDeptVo> list = mapper.selectIdsInForExport(searchCondition);
        return list;
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdsIn(List<MDeptVo> searchCondition) {
        List<MDeptEntity> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        list.forEach(
            bean -> {
                bean.setIs_del(!bean.getIs_del());
            }
        );
        saveOrUpdateBatch(list, 500);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(MDeptEntity entity) {
        // 编码如果为空，自动生成编码
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
        entity.setTenant_id(getUserSessionTenantId());
        return InsertResultUtil.OK(mapper.insert(entity));
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(MDeptEntity entity) {
        // 更新前check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        entity.setTenant_id(getUserSessionTenantId());
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * 获取数据byid
     * @param id
     * @return
     */
    @Override
    public MDeptVo selectByid(Long id){
        return mapper.selectByid(id, getUserSessionTenantId());
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param code
     * @return
     */
    public List<MDeptEntity> selectByCode(String code, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MDeptEntity> list = mapper.selectByCode(code, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MDeptEntity> selectByName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MDeptEntity> list = mapper.selectByName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MDeptEntity> selectBySimpleName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MDeptEntity> list = mapper.selectBySimpleName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkLogic(MDeptEntity entity, String moduleType){
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                List<MDeptEntity> codeList_insertCheck = selectByCode(entity.getCode(), null, null);
//                List<MDeptEntity> nameList_insertCheck = selectByName(entity.getName(), null, null);
//                List<MDeptEntity> simple_name_insertCheck = selectBySimpleName(entity.getSimple_name(), null, null);
                if (codeList_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：部门编号【" + entity.getCode() + "】出现重复", entity.getCode());
                }
//                if (nameList_insertCheck.size() >= 1) {
//                    return CheckResultUtil.NG("新增保存出错：部门全称【" + entity.getName() + "】出现重复", entity.getName());
//                }
//                if (simple_name_insertCheck.size() >= 1) {
//                    return CheckResultUtil.NG("新增保存出错：部门简称【" + entity.getSimple_name() + "】出现重复", entity.getSimple_name());
//                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合，不能重复设置
                List<MDeptEntity> codeList_updCheck = selectByCode(entity.getCode(), null, entity.getId());
//                List<MDeptEntity> nameList_updCheck = selectByName(entity.getName(), null, entity.getId());
//                List<MDeptEntity> simple_name_updCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：部门编号【" + entity.getCode() + "】出现重复！", entity.getCode());
                }
//                if (nameList_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("更新保存出错：部门全称【" + entity.getName() + "】出现重复！", entity.getName());
//                }
//                if (simple_name_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("更新保存出错：部门简称【" + entity.getSimple_name() + "】出现重复！", entity.getSimple_name());
//                }
                break;
            case CheckResult.DELETE_CHECK_TYPE:
                /** 如果逻辑删除为false，表示为：页面点击了删除操作 */
                if(entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                // 是否被使用的check，如果被使用则不能删除
                int count = mapper.isExistsInOrg(entity);
                if(count > 0){
                    return CheckResultUtil.NG("删除出错：该企业【"+ entity.getSimple_name() +"】在组织机构中正在使用！", count);
                }
                break;
            case CheckResult.UNDELETE_CHECK_TYPE:
                /** 如果逻辑删除为true，表示为：页面点击了删除操作 */
                if(!entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                // 更新场合，不能重复设置
                List<MDeptEntity> codeList_undel_Check = selectByCode(entity.getCode(), null, entity.getId());
//                List<MDeptEntity> nameList_undel_updCheck = selectByName(entity.getName(), null, entity.getId());
//                List<MDeptEntity> simple_name_undel_updCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_undel_Check.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：部门编号【" + entity.getCode() + "】出现重复", entity.getCode());
                }
//                if (nameList_undel_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("复原出错：部门全称【" + entity.getName() + "】出现重复", entity.getName());
//                }
//                if (simple_name_undel_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("复原出错：部门简称【" + entity.getSimple_name() + "】出现重复", entity.getSimple_name());
//                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }
}
