package com.perfect.manager.controller.sys.config.tenant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.utils.common.tree.TreeUtil;
import com.perfect.bean.vo.sys.config.config.SConfigVo;
import com.perfect.bean.vo.sys.config.tenant.STenantTreeVo;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.constant.JsonResultTypeConstants;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.core.service.sys.config.tenant.ITenantService;
import com.perfect.framework.base.controller.v1.BaseController;
import com.perfect.manager.mq.tenant.TenantMqProducter;
import com.perfect.mq.rabbitmq.callback.manager.config.tenant.TenantMqCallbackInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangxh
 */
@RestController
@RequestMapping(value = "/api/v1/tenant")
@Slf4j
@Api("租户相关")
public class TenantController extends BaseController implements TenantMqCallbackInterface {

    @Autowired
    private ITenantService service;

    @Autowired
    private TenantMqProducter tenantMqProducter;



    @SysLogAnnotion("根据查询条件，获取租户信息")
    @ApiOperation("获取租户树数据")
    @PostMapping("/tree/list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<STenantTreeVo>>> treeList(@RequestBody(required = false) SConfigVo searchCondition) {
        List<STenantTreeVo> vo = service.getTreeList(null,null);
        List<STenantTreeVo> rtnVo = TreeUtil.getTreeList(vo);
        return ResponseEntity.ok().body(ResultUtil.OK(rtnVo));
    }

    @SysLogAnnotion("根据查询条件，获取租户信息")
    @ApiOperation("获取级联数据")
    @PostMapping("/cascader/list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<STenantTreeVo>>> cascaderList(@RequestBody(required = false)SConfigVo searchCondition) {
        List<STenantTreeVo> vo = service.getCascaderList(null,null);
        List<STenantTreeVo> rtnVo = TreeUtil.getTreeList(vo);
        return ResponseEntity.ok().body(ResultUtil.OK(rtnVo, JsonResultTypeConstants.STRING_EMPTY_BOOLEAN_FALSE));
    }

    @SysLogAnnotion("根据参数id，获取租户信息")
    @ApiOperation("根据参数id，获取租户信息")
    @PostMapping("{ id }")
    @ResponseBody
    public ResponseEntity<JsonResult<STenantEntity>> info(@RequestParam("id") String id) {
        STenantEntity entity = service.getById(id);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("根据查询条件，获取租户信息")
    @ApiOperation("根据参数id，获取租户信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<STenantVo>>> list(@RequestBody(required = false) STenantVo searchCondition) {
        IPage<STenantVo> entity = service.selectPage(searchCondition);
            return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("租户数据更新保存")
    @ApiOperation("租户数据更新保存")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<STenantVo>> save(@RequestBody(required = false) STenantEntity bean) {

        if(service.update(bean).isSuccess()){
            // 获取更新后的数据
            STenantVo vo = service.selectByid(bean.getId());
            // 调用mq
            tenantMqProducter.mqSendAfterDataSave(vo);
            return ResponseEntity.ok().body(ResultUtil.OK(vo,"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("租户数据新增保存")
    @ApiOperation("租户数据新增保存")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<STenantVo>> insert(@RequestBody(required = false) STenantEntity bean) {
        // 默认启用
        bean.setIs_enable(true);
        if(service.insert(bean).isSuccess()){
            // 获取更新后的数据
            STenantVo vo = service.selectByid(bean.getId());
            // 调用mq
            tenantMqProducter.mqSendAfterDataSave(vo);
            return ResponseEntity.ok().body(ResultUtil.OK(vo,"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @Override
    public void mqCallBackTestFunction(String parameterClass , String parameter) {
        log.debug("testmq");
    }

    @Override
    public void mqCallBackTestFunction(List<String> callbackBean) {
        log.debug("testmq");
    }



}
