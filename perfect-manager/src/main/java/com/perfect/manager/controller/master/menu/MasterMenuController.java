package com.perfect.manager.controller.master.menu;

import com.perfect.bean.entity.master.menu.MMenuEntity;
import com.perfect.bean.pojo.result.InsertOrUpdateResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.menu.MMenuDataVo;
import com.perfect.bean.vo.master.menu.MMenuRedirectVo;
import com.perfect.bean.vo.master.menu.MMenuVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.constant.JsonResultTypeConstants;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.core.service.master.menu.IMMenuService;
import com.perfect.framework.base.controller.v1.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author zhangxh
 */
@RestController
@RequestMapping(value = "/api/v1/menus")
@Slf4j
@Api("菜单相关")
public class MasterMenuController extends BaseController {

    @Autowired
    private IMMenuService service;

    @Autowired
    private RestTemplate restTemplate;

    @SysLogAnnotion("根据查询条件，获取菜单主表信息")
    @ApiOperation("根据参数id，获取菜单主表信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<MMenuVo>> list(@RequestBody(required = false) MMenuDataVo searchCondition) {
        searchCondition.setTenant_id(super.getUserSessionTenantId());
        MMenuVo entity = service.getTreeData(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("根据查询条件，获取级联信息")
    @ApiOperation("获取级联数据")
    @PostMapping("/cascader/list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<MMenuDataVo>>> cascaderList(@RequestBody(required = false) MMenuVo searchCondition) {
        List<MMenuDataVo> vo = service.getCascaderList(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(vo, JsonResultTypeConstants.STRING_EMPTY_BOOLEAN_FALSE));
    }

    @SysLogAnnotion("系统菜单数据更新保存")
    @ApiOperation("系统菜单数据更新保存")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuDataVo>> save(@RequestBody(required = false) MMenuEntity bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        UpdateResult<MMenuDataVo> rtn = service.update(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"系统菜单数据更新保存成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("系统菜单数据新增菜单组")
    @ApiOperation("新增菜单组")
    @PostMapping("/addmenugroup")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuDataVo>> addMenuGroup(@RequestBody(required = false) MMenuDataVo bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        InsertResult<MMenuDataVo> rtn = service.addMenuGroup(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"新增菜单组成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("系统菜单数据新增菜单组")
    @ApiOperation("新增顶部导航栏")
    @PostMapping("/addtopnav")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuDataVo>> addTopNav(@RequestBody(required = false) MMenuDataVo bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        InsertResult<MMenuDataVo> rtn = service.addTopNav(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"新增菜单组成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("系统菜单数据新增菜单组")
    @ApiOperation("新增子节点")
    @PostMapping("/addsubnode")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuDataVo>> addSubNode(@RequestBody(required = false) MMenuDataVo bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        InsertResult<MMenuDataVo> rtn = service.addSubNode(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"新增菜单组成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("系统菜单数据新增菜单组")
    @ApiOperation("新增子菜单")
    @PostMapping("/addsubmenu")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuDataVo>> addSubMenu(@RequestBody(required = false) MMenuDataVo bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        InsertResult<MMenuDataVo> rtn = service.addSubMenu(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"新增菜单组成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("模块按钮表数据逻辑物理删除，部分数据")
    @ApiOperation("根据参数id，逻辑删除数据")
    @PostMapping("/realdelete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> realDelete(@RequestBody(required = false) MMenuDataVo searchCondition) {
        searchCondition.setTenant_id(super.getUserSessionTenantId());
        if(searchCondition == null) {
            return ResponseEntity.ok().body(ResultUtil.OK("没有数据"));
        } else {
            service.realDeleteByCode(searchCondition);
            return ResponseEntity.ok().body(ResultUtil.OK("OK"));
        }
    }

    @SysLogAnnotion("系统菜单数据更新保存，拖拽后，全量更新")
    @ApiOperation("系统菜单数据更新保存，拖拽后，全量更新")
    @PostMapping("/dragsave")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> dragsave(@RequestBody(required = false) List<MMenuDataVo> beans) {
        service.dragsave(beans);
        return ResponseEntity.ok().body(ResultUtil.OK("拖拽更新成功"));
    }

    @SysLogAnnotion("菜单重定向更新保存")
    @ApiOperation("菜单重定向更新保存")
    @PostMapping("/redirect/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MMenuRedirectVo>> saveRedirect(@RequestBody(required = false) MMenuRedirectVo bean) {
        InsertOrUpdateResult<MMenuRedirectVo> rtn = service.saveRedirect(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"菜单重定向更新保存成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }
}
