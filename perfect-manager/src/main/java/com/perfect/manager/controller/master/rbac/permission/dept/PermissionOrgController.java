package com.perfect.manager.controller.master.rbac.permission.dept;

import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.rbac.permission.dept.MOrgDeptPermissionTreeVo;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.core.service.master.rbac.permission.dept.IMPermissionOrgService;
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
@RequestMapping(value = "/api/v1/permission/org")
@Slf4j
@Api("权限类页面左侧的树")
public class PermissionOrgController extends BaseController {

    @Autowired
    private IMPermissionOrgService service;

    @SysLogAnnotion("根据查询条件，获取组织机构信息")
    @ApiOperation("获取组织机构树数据")
    @PostMapping("/tree/dept/list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<MOrgDeptPermissionTreeVo>>> treeList(@RequestBody(required = false) MOrgDeptPermissionTreeVo searchCondition) {
        searchCondition.setTenant_id(getUserSessionTenantId());
        List<MOrgDeptPermissionTreeVo> vo = service.getTreeList(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(vo));
    }
}
