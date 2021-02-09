package com.perfect.core.serviceimpl.master.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.perfect.bean.entity.master.user.MUserDefaultMenuEntity;
import com.perfect.core.mapper.master.user.MUserDefaultMenuMapper;
import com.perfect.core.service.master.user.IMUserDefaultMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户默认菜单表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2021-02-09
 */
@Service
public class MUserDefaultMenuServiceImpl extends ServiceImpl<MUserDefaultMenuMapper, MUserDefaultMenuEntity> implements IMUserDefaultMenuService {

}
