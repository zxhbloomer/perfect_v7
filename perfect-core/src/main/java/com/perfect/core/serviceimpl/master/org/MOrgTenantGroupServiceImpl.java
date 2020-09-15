package com.perfect.core.serviceimpl.master.org;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.org.MOrgTenantGroupEntity;
import com.perfect.core.mapper.master.org.MOrgTenantGroupMapper;
import com.perfect.core.service.master.org.IMOrgTenantGroupService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 集团与集团关系表，多集团嵌套关系表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2020-05-15
 */
@Service
public class MOrgTenantGroupServiceImpl extends ServiceImpl<MOrgTenantGroupMapper, MOrgTenantGroupEntity> implements
    IMOrgTenantGroupService {

}
