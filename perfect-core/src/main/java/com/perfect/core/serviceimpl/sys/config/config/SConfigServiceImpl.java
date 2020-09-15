package com.perfect.core.serviceimpl.sys.config.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.org.MCompanyEntity;
import com.perfect.bean.entity.sys.config.config.SConfigEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.DeleteResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.DeleteResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.master.org.MCompanyVo;
import com.perfect.bean.vo.sys.config.config.SConfigVo;
import com.perfect.common.exception.BusinessException;
import com.perfect.core.mapper.sys.config.config.SConfigMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.config.config.ISConfigService;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class SConfigServiceImpl extends BaseServiceImpl<SConfigMapper, SConfigEntity> implements ISConfigService {

    @Autowired
    private SConfigMapper mapper;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<SConfigVo> selectPage(SConfigVo searchCondition) {
        // 分页条件
        Page<SConfigEntity> pageCondition =
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
    public List<SConfigVo> select(SConfigVo searchCondition) {
        // 查询 数据
        List<SConfigVo> list = mapper.select(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<SConfigEntity> selectIdsIn(List<SConfigVo> searchCondition) {
        // 查询 数据
        List<SConfigEntity> list = mapper.selectIdsIn(searchCondition);
        return list;
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<SConfigVo> selectIdsInForExport(List<SConfigVo> searchCondition) {
        // 查询 数据
        List<SConfigVo> list = mapper.selectIdsInForExport(searchCondition);
        return list;
    }

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    @Override
    public SConfigVo selectByid(Long id) {
        // 查询 数据
        return mapper.selectId(id);
    }

    /**
     * 批量导入逻辑
     *
     * @param entityList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatches(List<SConfigEntity> entityList) {
        return super.saveBatch(entityList, 500);
    }


    /**
     * 插入一条记录（选择字段，策略插入）
     * 
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(SConfigEntity entity) {
        // 插入前check
        CheckResult cr = checkLogic(entity.getName(), entity.getConfig_key(), CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
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
    public UpdateResult<Integer> update(SConfigEntity entity) {
        // 更新前check
        CheckResult cr = checkLogic(entity.getName(), entity.getConfig_key(), CheckResult.UPDATE_CHECK_TYPE);
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
     * @param name
     * @return
     */
    @Override
    public List<SConfigEntity> selectByName(String name) {
        // 查询 数据
        List<SConfigEntity> list = mapper.selectByName(name);
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param key
     * @return
     */
    @Override
    public List<SConfigEntity> selectByKey(String key) {
        // 查询 数据
        List<SConfigEntity> list = mapper.selectByKey(key);
        return list;
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param value
     * @return
     */
    @Override
    public List<SConfigEntity> selectByValue(String value) {
        // 查询 数据
        List<SConfigEntity> list = mapper.selectByValue(value);
        return list;
    }


    /**
     * check逻辑
     * 
     * @return
     */
    public CheckResult checkLogic(String name, String key, String moduleType) {
        List<SConfigEntity> selectByName = selectByName(name);
        List<SConfigEntity> selectByKey = selectByKey(key);

        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                if (selectByName.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：参数名称出现重复", name);
                }
                if (selectByKey.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：参数键名出现重复", key);
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合，不能重复设置
                if (selectByName.size() >= 2) {
                    return CheckResultUtil.NG("新增保存出错：参数名称出现重复", name);
                }
                if (selectByKey.size() >= 2) {
                    return CheckResultUtil.NG("新增保存出错：参数键名出现重复", key);
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }


    /**
     * 批量删除复原
     *
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeleteResult<Integer> realDeleteByIdsIn(List<SConfigVo> searchCondition) {
        List<Long> idList = new ArrayList<>();
        searchCondition.forEach(bean -> {
            idList.add(bean.getId());
        });
        int result=mapper.deleteBatchIds(idList);
        return DeleteResultUtil.OK(result);
    }

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enabledByIdsIn(List<SConfigVo> searchCondition) {
        List<SConfigEntity> list = mapper.selectIdsIn(searchCondition);
        for(SConfigEntity entity : list) {
//            CheckResult cr;
//            if(entity.getIs_enable()){
//                /** 如果逻辑删除为true，表示为：页面点击了复原操作 */
//                cr = checkLogic(entity, CheckResult.UNDELETE_CHECK_TYPE);
//            } else {
//                /** 如果逻辑删除为false，表示为：页面点击了删除操作 */
//                cr = checkLogic(entity, CheckResult.DELETE_CHECK_TYPE);
//            }
//            if (cr.isSuccess() == false) {
//                throw new BusinessException(cr.getMessage());
//            }
            entity.setIs_enable(!entity.getIs_enable());
        }
        saveOrUpdateBatch(list, 500);
    }

}
