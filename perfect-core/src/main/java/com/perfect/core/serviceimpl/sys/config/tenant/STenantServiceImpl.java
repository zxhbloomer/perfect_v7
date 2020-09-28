package com.perfect.core.serviceimpl.sys.config.tenant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.quartz.SJobEntity;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.sys.config.tenant.STenantTreeVo;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import com.perfect.common.enums.quartz.QuartzEnum;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.string.StringUtil;
import com.perfect.core.mapper.quartz.SJobLogMapper;
import com.perfect.core.mapper.quartz.SJobMapper;
import com.perfect.core.mapper.sys.config.tenant.STenantMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import com.perfect.core.serviceimpl.common.autocode.TenantAutoCodeServiceImpl;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
@Service
public class STenantServiceImpl extends BaseServiceImpl<STenantMapper, STenantEntity> implements ITenantService {

    @Autowired
    private STenantMapper mapper;
    @Autowired
    private SJobMapper jobMapper;
    @Autowired
    private SJobLogMapper jobLogMapper;
    @Autowired
    private TenantAutoCodeServiceImpl tenantAutoCode;

    /**
     * 获取数据，树结构
     * 
     * @param id
     * @return
     */
    @Override
    public List<STenantTreeVo> getTreeList(Long id, String name) {
        List<STenantTreeVo> listVo = mapper.getTreeList(id, name);
        return listVo;
    }

    /**
     * 获取数据，级联结构
     *
     * @param id
     * @return
     */
    @Override
    public List<STenantTreeVo> getCascaderList(Long id, String name) {
        List<STenantTreeVo> listVo = mapper.getCascaderList(id, name);
        return listVo;
    }

    /**
     * 获取列表，页面查询
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<STenantVo> selectPage(STenantVo searchCondition) {
        // 分页条件
        Page<STenantVo> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        return mapper.selectPage(pageCondition, searchCondition);
    }

    /**
     * 获取列表，根据id查询所有数据
     * 
     * @param searchCondition
     * @return
     */
    @Override
    public List<STenantEntity> selectIdsIn(List<STenantVo> searchCondition) {
        // 查询 数据
        List<STenantEntity> list = mapper.selectIdsIn(searchCondition);
        return list;
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * 
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(STenantEntity entity) {
        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);

        // 初始化值
        entity.setIs_enable(false);
        entity.setIs_freeze(false);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 启用时间小于系统时间，则自动启用
        if(entity.getEnable_time().isBefore(LocalDateTime.now())){
            entity.setIs_enable(true);
        }
        // 禁用时间小于系统时间，则自动禁用
        if(entity.getDisable_time().isBefore(LocalDateTime.now())){
            entity.setIs_enable(false);
        }
        // 租户编码如果为空，自动生成租户编码
        if(StringUtil.isEmpty(entity.getCode())){
            entity.setCode(tenantAutoCode.autoCode().getCode());
        }
        // 插入逻辑保存
        return InsertResultUtil.OK(mapper.insert(entity));
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * 
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(STenantEntity entity) {
        // 更新前check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 启用时间小于系统时间，则自动启用
        if(entity.getEnable_time().isBefore(LocalDateTime.now())){
            entity.setIs_enable(true);
        }
        // 禁用时间小于系统时间，则自动禁用
        if(entity.getDisable_time().isBefore(LocalDateTime.now())){
            entity.setIs_enable(false);
        }
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    @Override
    public STenantVo selectByid(Long id) {
        if (id != null) {
            // 查询 数据
            return mapper.selectId(id);
        } else {
            return null;
        }

    }

    /**
     * 查询by code，返回结果
     *
     * @param code
     * @return
     */
    @Override
    public List<STenantEntity> selectByCode(String code) {
        // 查询 数据
        List<STenantEntity> list = mapper.selectByCode(code);
        return list;
    }

    /**
     * 查询by 名称，返回结果
     *
     * @param name
     * @return
     */
    @Override
    public List<STenantEntity> selectByName(String name) {
        // 查询 数据
        List<STenantEntity> list = mapper.selectByName(name);
        return list;
    }

    /**
     * check逻辑，模块编号 or 模块名称 不能重复
     * 
     * @return
     */
    public CheckResult checkLogic(STenantEntity entity, String moduleType) {
        List<STenantEntity> listCode = selectByCode(entity.getCode());
        List<STenantEntity> listName = selectByName(entity.getName());

        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                if (listCode.size() >= 1) {
                    // 模块编号不能重复
                    return CheckResultUtil.NG("新增保存出错：租户编码出现重复", listCode);
                }
                if (listName.size() >= 1) {
                    // 模块名称不能重复
                    return CheckResultUtil.NG("新增保存出错：租户名称出现重复", listName);
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合，不能重复设置
                if (listCode.size() >= 2) {
                    // 模块编号不能重复
                    return CheckResultUtil.NG("更新保存出错：租户编码出现重复", listCode);
                }
                if (listName.size() >= 2) {
                    // 模块名称不能重复
                    return CheckResultUtil.NG("更新保存出错：租户名称出现重复", listName);
                }
                break;
            default:

        }

        return CheckResultUtil.OK();
    }

    /**
     * 根据ID获取子结点数组
     *
     * @param id
     * @return
     */
    @Override
    public List<STenantTreeVo> getChildren(Long id, String name) {
        List<STenantTreeVo> list = mapper.getTreeList(id, name);
        return null;
    }

    /**
     * 启用
     *
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> enableUpdate(STenantEntity entity) {
        entity.setIs_enable(true);
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * 禁用
     *
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> disableUpdate(STenantEntity entity) {
        entity.setIs_enable(false);
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }


    /**
     * 启用
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean enableProcess(STenantEntity entity){
        boolean rtn = false;
        // 1、获取相应的job enable bean
        SJobEntity enableJobEntity = jobMapper.selectJobBySerialId(entity.getId(), QuartzEnum.TASK_TENANT_ENABLE.getJob_serial_type());
        enableJobEntity.setLast_run_time(LocalDateTime.now());
        enableJobEntity.setRun_times(enableJobEntity.getRun_times() == null ? 0 : enableJobEntity.getRun_times() + 1);
        enableJobEntity.setMsg(QuartzEnum.TASK_TENANT_ENABLE.getOk_msg());
        // 2、更新租户bean
        entity.setIs_enable(true);

        entity.setC_id(null);
        entity.setC_time(null);
        mapper.updateById(entity);
        // 3、更新job
        enableJobEntity.setC_id(null);
        enableJobEntity.setC_time(null);
        jobMapper.updateById(enableJobEntity);
//        // 4、更新job日志
//        SJobLogEntity sJobLogEntity = new SJobLogEntity();
//        BeanUtilsSupport.copyProperties(enableJobEntity, sJobLogEntity);
//        sJobLogEntity.setJob_id(enableJobEntity.getId());
//        jobLogMapper.insert(sJobLogEntity);

        rtn = true;
        return rtn;
    }

    /**
     * 禁用
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean disableProcess(STenantEntity entity){
        boolean rtn = false;
        // 1、获取相应的job enable bean
        SJobEntity disableJobEntity = jobMapper.selectJobBySerialId(entity.getId(), QuartzEnum.TASK_TENANT_DISABLE.getJob_serial_type());
        disableJobEntity.setLast_run_time(LocalDateTime.now());
        disableJobEntity.setRun_times(disableJobEntity.getRun_times() == null ? 0 : disableJobEntity.getRun_times() + 1);
        disableJobEntity.setMsg(QuartzEnum.TASK_TENANT_ENABLE.getOk_msg());
        // 2、更新租户bean
        entity.setIs_enable(true);
        entity.setC_id(null);
        entity.setC_time(null);
        mapper.updateById(entity);
        // 3、更新job
        disableJobEntity.setC_id(null);
        disableJobEntity.setC_time(null);
        jobMapper.updateById(disableJobEntity);
//        // 4、更新job日志
//        SJobLogEntity sJobLogEntity = new SJobLogEntity();
//        BeanUtilsSupport.copyProperties(disableJobEntity, sJobLogEntity);
//        sJobLogEntity.setJob_id(disableJobEntity.getId());
//        jobLogMapper.insert(sJobLogEntity);

        rtn = true;
        return rtn;
    }

}
