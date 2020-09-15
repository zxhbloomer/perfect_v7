package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.org.MOrgDeptPositionEntity;
import com.perfect.core.mapper.master.org.MOrgDeptPositionMapper;
import com.perfect.core.service.master.org.IMOrgDeptPositionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位与部门关系表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2020-05-15
 */
@Service
public class MOrgDeptPositionServiceImpl extends ServiceImpl<MOrgDeptPositionMapper, MOrgDeptPositionEntity> implements
    IMOrgDeptPositionService {

}
