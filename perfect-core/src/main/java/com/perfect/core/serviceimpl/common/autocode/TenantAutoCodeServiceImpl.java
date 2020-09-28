package com.perfect.core.serviceimpl.common.autocode;

import com.perfect.bean.entity.sys.platform.syscode.SCodeEntity;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.core.service.common.autocode.IAutoCodeService;
import com.perfect.core.service.sys.platform.syscode.ISCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TenantAutoCode
 * @Description: 自动生成编码：租户类
 * @Author: zxh
 * @date: 2019/12/13
 * @Version: 1.0
 */
@Component
public class TenantAutoCodeServiceImpl implements IAutoCodeService {

    @Autowired
    ISCodeService service;

    @Override
    public SCodeEntity autoCode() {
        String type = PerfectDictConstant.DICT_SYS_CODE_TYPE_S_TENANT;
        UpdateResult<SCodeEntity> upd = service.createCode(type);
        if(upd.isSuccess()){
            return upd.getData();
        }
        return null;
    }
}
