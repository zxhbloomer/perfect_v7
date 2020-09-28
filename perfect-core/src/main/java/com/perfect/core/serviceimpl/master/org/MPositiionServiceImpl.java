package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.org.MPositionEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.org.MPositionVo;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.string.StringUtil;
import com.perfect.core.mapper.master.org.MPositionMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.master.org.IMPositionService;
import com.perfect.core.serviceimpl.common.autocode.MPositionAutoCodeServiceImpl;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  岗位主表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class MPositiionServiceImpl extends BaseServiceImpl<MPositionMapper, MPositionEntity> implements IMPositionService {

    @Autowired
    private MPositionMapper mapper;

    @Autowired
    private MPositionAutoCodeServiceImpl autoCode;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<MPositionVo> selectPage(MPositionVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 分页条件
        Page<MPositionEntity> pageCondition =
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
    public List<MPositionVo> select(MPositionVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        // 查询 数据
        List<MPositionVo> list = mapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MPositionEntity> selectIdsIn(List<MPositionVo> searchCondition) {
        // 查询 数据
        List<MPositionEntity> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MPositionVo> selectIdsInForExport(List<MPositionVo> searchCondition) {
        // 查询 数据
        List<MPositionVo> list = mapper.selectIdsInForExport(searchCondition);
        return list;
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdsIn(List<MPositionVo> searchCondition) {
        List<MPositionEntity> list = mapper.selectIdsIn(searchCondition, getUserSessionTenantId());
        list.forEach(
            bean -> {
                bean.setIs_del(!bean.getIs_del());
                bean.setTenant_id(getUserSessionTenantId());
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
    public InsertResult<Integer> insert(MPositionEntity entity) {
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
    public UpdateResult<Integer> update(MPositionEntity entity) {
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
    public MPositionVo selectByid(Long id){
        MPositionVo searchCondition = new MPositionVo();
        searchCondition.setId(id);
        searchCondition.setTenant_id(getUserSessionTenantId());
        return mapper.selectByid(searchCondition);
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param code
     * @return
     */
    public List<MPositionEntity> selectByCode(String code, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MPositionEntity> list = mapper.selectByCode(code, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MPositionEntity> selectByName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MPositionEntity> list = mapper.selectByName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param name
     * @return
     */
    public List<MPositionEntity> selectBySimpleName(String name, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<MPositionEntity> list = mapper.selectBySimpleName(name, equal_id, not_equal_id, getUserSessionTenantId());
        return list;
    }

    /**
     * check逻辑
     * @return
     */
    public CheckResult checkLogic(MPositionEntity entity, String moduleType){
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                List<MPositionEntity> codeList_insertCheck = selectByCode(entity.getCode(), null, null);
//                List<MPositionEntity> nameList_insertCheck = selectByName(entity.getName(), null, null);
//                List<MPositionEntity> simple_name_insertCheck = selectBySimpleName(entity.getSimple_name(), null, null);
                if (codeList_insertCheck.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：岗位编号【"+ entity.getCode() +"】出现重复", entity.getCode());
                }
//                if (nameList_insertCheck.size() >= 1) {
//                    return CheckResultUtil.NG("新增保存出错：岗位全称【"+ entity.getName() +"】出现重复", entity.getName());
//                }
//                if (simple_name_insertCheck.size() >= 1) {
//                    return CheckResultUtil.NG("新增保存出错：岗位简称【"+ entity.getSimple_name() +"】出现重复", entity.getSimple_name());
//                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合，不能重复设置
                List<MPositionEntity> codeList_updCheck = selectByCode(entity.getCode(), null, entity.getId());
//                List<MPositionEntity> nameList_updCheck = selectByName(entity.getName(), null, entity.getId());
//                List<MPositionEntity> simple_name_updCheck = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_updCheck.size() >= 1) {
                    return CheckResultUtil.NG("更新保存出错：岗位编号【"+ entity.getCode() +"】出现重复", entity.getCode());
                }
//                if (nameList_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("更新保存出错：岗位全称【"+ entity.getName() +"】出现重复", entity.getName());
//                }
//                if (simple_name_updCheck.size() >= 1) {
//                    return CheckResultUtil.NG("更新保存出错：岗位简称【"+ entity.getSimple_name() +"】出现重复", entity.getSimple_name());
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
                    return CheckResultUtil.NG("删除出错：该岗位【"+ entity.getSimple_name() +"】在组织机构中正在被使用，不能删除！", count);
                }
                break;
            case CheckResult.UNDELETE_CHECK_TYPE:
                /** 如果逻辑删除为true，表示为：页面点击了删除操作 */
                if(!entity.getIs_del()) {
                    return CheckResultUtil.OK();
                }
                List<MPositionEntity> codeList_undelete_Check = selectByCode(entity.getCode(), null, entity.getId());
//                List<MPositionEntity> nameList_undelete_Check = selectByName(entity.getName(), null, entity.getId());
//                List<MPositionEntity> simple_name_undelete_Check = selectBySimpleName(entity.getSimple_name(), null, entity.getId());

                if (codeList_undelete_Check.size() >= 1) {
                    return CheckResultUtil.NG("复原出错：复原岗位编号【"+ entity.getCode() +"】出现重复", entity.getCode());
                }
//                if (nameList_undelete_Check.size() >= 1) {
//                    return CheckResultUtil.NG("复原出错：复原岗位全称【"+ entity.getName() +"】出现重复", entity.getName());
//                }
//                if (simple_name_undelete_Check.size() >= 1) {
//                    return CheckResultUtil.NG("复原出错：复原岗位简称【"+ entity.getSimple_name() +"】出现重复", entity.getSimple_name());
//                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }
}
