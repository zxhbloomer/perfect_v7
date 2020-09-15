package com.perfect.core.serviceimpl.log.mq;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.log.mq.SLogMqEntity;
import com.perfect.bean.entity.quartz.SJobLogEntity;
import com.perfect.bean.entity.sys.config.config.SConfigEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.sys.config.config.SConfigVo;
import com.perfect.common.exception.BusinessException;
import com.perfect.core.mapper.log.mq.SLogMqMapper;
import com.perfect.core.mapper.quartz.SJobLogMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.log.mq.ISLogMqService;
import com.perfect.core.service.quartz.ISJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-04
 */
@Service
public class SLogMqServiceImpl extends BaseServiceImpl<SLogMqMapper, SLogMqEntity> implements ISLogMqService {

    @Autowired
    private SLogMqMapper mapper;

    /**
     * 获取列表，查询所有数据
     *
     * @return
     */
    @Override
    public List<SLogMqEntity> selectAll() {
        // 查询 数据
        List<SLogMqEntity> list = mapper.selectAll();
        return list;
    }

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    @Override
    public SLogMqEntity selectByid(Long id) {
        // 查询 数据
        return mapper.selectId(id);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(SLogMqEntity entity) {
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
    public UpdateResult<Integer> update(SLogMqEntity entity) {
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }

    /**
     * 获取列表，查询所有数据
     *
     * @param key
     * @return
     */
    @Override
    public SLogMqEntity selectByKey(String key) {
        // 查询 数据
        SLogMqEntity list = mapper.selectByKey(key);
        return list;
    }
}
