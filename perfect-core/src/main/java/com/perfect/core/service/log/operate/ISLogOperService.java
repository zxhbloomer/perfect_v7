package com.perfect.core.service.log.operate;

import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.bo.log.operate.CustomOperateBo;
import com.perfect.bean.entity.log.operate.SLogOperEntity;
import com.perfect.bean.entity.master.org.MOrgEntity;
import com.perfect.bean.pojo.result.InsertResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxh
 * @since 2019-07-04
 */
public interface ISLogOperService extends IService<SLogOperEntity> {

    /**
     * 插入一条记录
     * @return
     */
    InsertResult<Boolean> save(CustomOperateBo cobo) ;
}
