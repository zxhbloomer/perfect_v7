package com.perfect.manager.controller.master.user;

import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.bo.session.user.rbac.PermissionMenuBo;
import com.perfect.bean.bo.session.user.rbac.PermissionMenuOperationBo;
import com.perfect.bean.bo.session.user.rbac.PermissionOperationBo;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.user.MUserVo;
import com.perfect.bean.vo.master.user.UserInfoVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.constant.JsonResultTypeConstants;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.PasswordException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.string.StringUtil;
import com.perfect.core.service.client.user.IMUserService;
import com.perfect.core.service.master.rbac.permission.user.IMUserPermissionService;
import com.perfect.framework.base.controller.v1.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zxh
 */
@RestController
@RequestMapping(value = "/api/v1/user")
@Slf4j
@Api("用户相关")
public class UserController extends BaseController {

    @Autowired
    private IMUserService service;

    @Autowired
    private IMUserPermissionService imUserPermissionService;

    @SysLogAnnotion("获取用户信息")
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<JsonResult<UserInfoVo>> userInfo(@RequestParam("token") String token, @RequestParam("path") String path) {

        UserInfoVo userInfoVo = service.getUserInfo(token);

        /** 设置user session bean */
        userInfoVo.setUser_session_bean(getUserSession());

        //        ResponseEntity<OAuth2AccessToken
        return ResponseEntity.ok().body(ResultUtil.OK(userInfoVo, JsonResultTypeConstants.NULL_NOT_OUT));
    }

    @SysLogAnnotion("登出")
    @ApiOperation("登出")
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> logout() {
        return ResponseEntity.ok().body(ResultUtil.OK("登出成功"));
    }


    @SysLogAnnotion("员工主表数据更新保存")
    @ApiOperation("根据参数id，获取员工主表信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MUserVo>> save(@RequestBody(required = false) MUserEntity bean) {

        if(service.update(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("员工主表数据新增保存")
    @ApiOperation("根据参数id，获取员工主表信息")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MUserVo>> insert(@RequestBody(required = false) MUserEntity bean) {
        if(service.insert(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("获取用户信息")
    @ApiOperation("获取用户信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<MUserVo>> getUsrBeanById(@RequestBody(required = false) MUserEntity bean) {
        MUserVo mUserVo = service.selectByid(bean.getId());
        return ResponseEntity.ok().body(ResultUtil.OK(mUserVo));
    }

    @SysLogAnnotion("获取用户信息")
    @ApiOperation("获取用户信息")
    @PostMapping("/getpsd")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> getUsrPsdString(@RequestBody(required = false) MUserEntity bean, HttpServletRequest request) {
        MUserVo mUserVo = service.selectByid(bean.getId());
        if(!StringUtil.isEmpty(bean.getPwd())){
            String encodePsd = getPassword(bean.getPwd());
            // 保存到session中
            HttpSession session = request.getSession();
            session.setAttribute(PerfectConstant.SESSION_KEY_USER_PASSWORD, encodePsd);
            return ResponseEntity.ok().body(ResultUtil.OK(bean.getPwd()));
        } else {
            throw new PasswordException("密码设置失败。");
        }
    }

    @SysLogAnnotion("获取顶部导航栏数据")
    @ApiOperation("获取顶部导航栏数据")
    @PostMapping("/topnav")
    @ResponseBody
    public ResponseEntity<JsonResult<List<PermissionMenuBo>>> getTopNav() {
        List<PermissionMenuBo> user_permission_menu_topNav = imUserPermissionService.getPermissionMenuTopNav(getUserSession().getTenant_Id());
        return ResponseEntity.ok().body(ResultUtil.OK(user_permission_menu_topNav));
    }

    @SysLogAnnotion("获取菜单权限和操作权限数据")
    @ApiOperation("获取菜单权限和操作权限数据")
    @PostMapping("/userpermission")
    @ResponseBody
    public ResponseEntity<JsonResult<PermissionMenuOperationBo>> getUserPermission() {
        UserSessionBo bo = getUserSession();
        PermissionMenuOperationBo permissionMenuOperationBo = new PermissionMenuOperationBo();
        permissionMenuOperationBo.setSession_id(bo.getSession_id());
        permissionMenuOperationBo.setStaff_id(bo.getStaff_Id());
        permissionMenuOperationBo.setTenant_id(bo.getTenant_Id());

        /** 菜单权限数据 */
        List<PermissionMenuBo> user_permission_menu = imUserPermissionService.getPermissionMenu(bo.getStaff_Id(), bo.getTenant_Id());
        permissionMenuOperationBo.setUser_permission_menu(user_permission_menu);
        /** 操作权限数据  */
        List<PermissionOperationBo> user_permission_operation = imUserPermissionService.getPermissionOperation(bo.getStaff_Id(), bo.getTenant_Id());
        permissionMenuOperationBo.setUser_permission_operation(user_permission_operation);

        return ResponseEntity.ok().body(ResultUtil.OK(permissionMenuOperationBo));
    }
}
