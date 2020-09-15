package com.perfect.core.serviceimpl.quartz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.quartz.SJobLogEntity;
import com.perfect.core.mapper.quartz.SJobLogMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.quartz.ISJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-04
 */
@Service
public class SJobLogServiceImpl extends BaseServiceImpl<SJobLogMapper, SJobLogEntity> implements ISJobLogService {

    @Autowired
    private SJobLogMapper mapper;

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SJobLogEntity jobLog)
    {
        mapper.insert(jobLog);
    }
}
