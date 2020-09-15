package com.perfect.manager.controller.master.rbac.permission.dept;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.rbac.permission.MMenuRootNodeListVo;
import com.perfect.bean.vo.master.rbac.permission.MPermissionVo;
import com.perfect.bean.vo.master.rbac.permission.operation.OperationMenuDataVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.core.service.master.rbac.permission.IMPermissionService;
import com.perfect.framework.base.controller.v1.BaseController;
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
@RequestMapping(value = "/api/v1/permission/dept")
@Slf4j
@Api("部门权限相关")
public class PermissionDeptController extends BaseController {

    @Autowired
    private IMPermissionService service;

    @SysLogAnnotion("根据查询条件，获取部门权限表信息")
    @ApiOperation("根据参数id，获取部门权限表信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<MPermissionVo>>> list(@RequestBody(required = false)
        MPermissionVo searchCondition)  {
        searchCondition.setTenant_id(getUserSessionTenantId());
        IPage<MPermissionVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("部门权限表数据更新保存")
    @ApiOperation("根据参数id，获取部门权限表信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MPermissionVo>> save(@RequestBody(required = false) MPermissionVo bean) {
        bean.setTenant_id(super.getUserSessionTenantId());
        UpdateResult<MPermissionVo> rtn = service.update(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("部门权限表数据新增保存")
    @ApiOperation("根据参数id，获取部门权限表信息")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MPermissionVo>> insert(@RequestBody(required = false) MPermissionVo bean) {
        bean.setTenant_id(getUserSessionTenantId());
        OperationMenuDataVo operationMenuDataVo = new OperationMenuDataVo();
        operationMenuDataVo.setTenant_id(super.getUserSessionTenantId());
        operationMenuDataVo.setC_id(super.getUserSessionStaffId());
        operationMenuDataVo.setU_id(super.getUserSessionStaffId());

        InsertResult<MPermissionVo> rtn = service.insert(bean, operationMenuDataVo);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("部门权限表数据逻辑删除复原")
    @ApiOperation("根据参数id，逻辑删除复原数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false) List<MPermissionVo> searchConditionList) {
        service.deleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("部门权限表数据启用禁用")
    @ApiOperation("根据参数id，启用禁用")
    @PostMapping("/enable")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> enable(@RequestBody(required = false) MPermissionVo searchConditionList) {
        service.enableById(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("部门权限表数据获取系统菜单根节点")
    @ApiOperation("部门权限表数据获取系统菜单根节点")
    @PostMapping("/get_sys_menu_root_node")
    @ResponseBody
    public ResponseEntity<JsonResult<MMenuRootNodeListVo>> getSystemMenuRootList() {
        MMenuRootNodeListVo searchCondition = new MMenuRootNodeListVo();
        searchCondition.setTenant_id(getUserSessionTenantId());
        return ResponseEntity.ok().body(ResultUtil.OK(service.getSystemMenuRootList(searchCondition)));
    }

//    @SysLogAnnotion("判断是否已经选择了菜单")
//    @ApiOperation("判断是否已经选择了菜单")
//    @PostMapping("/setted")
//    @ResponseBody
//    public ResponseEntity<JsonResult<Boolean>> isAlreadySetMenuId(@RequestBody(required = false) MPermissionVo searchCondition) {
//        searchCondition.setTenant_id(getUserSessionTenantId());
//        return ResponseEntity.ok().body(ResultUtil.OK(service.isAlreadySetMenuId(searchCondition)));
//    }

}
