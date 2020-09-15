package com.perfect.manager.controller.sys.platform;

import com.perfect.bean.entity.sys.config.dict.SDictDataEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import com.perfect.bean.vo.sys.platform.SignUpVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import com.perfect.core.service.sys.platform.ISignUpService;
import com.perfect.framework.base.controller.v1.BaseController;
import com.perfect.manager.mq.tenant.TenantMq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: SignupController
 * @Description: 注册用户
 * @Author: zxh
 * @date: 2019/12/16
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/api/v1/signup")
@Slf4j
@Api("自主注册用户")
public class PlatformSignUpController extends BaseController {

    @Autowired
    ISignUpService service;

    @Autowired
    TenantMq tenantMq;

    @SysLogAnnotion("注册根据手机号码，租户名称，管理员，密码，生成注册信息")
    @ApiOperation("注册根据手机号码，租户名称，管理员，密码，生成注册信息")
    @PostMapping("/mobile")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> signUp(@RequestBody(required = false) SignUpVo bean) {
        // 1:check
        if(service.check(bean)) {
            bean.setEncodePassword(getPassword(bean.getPassword()));
            // 2:执行注册
            service.signUp(bean);
            // 3:启动租户的定时任务
            tenantMq.mqSendAfterDataSave(bean.getTenant_id());
        }
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("check手机号码是否已经被使用")
    @ApiOperation("check手机号码是否已经被使用")
    @PostMapping("/check/mobile")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> checkMobile(@RequestBody(required = false) SignUpVo bean) {
        // 1:check
        service.checkMobile(bean);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }
}
