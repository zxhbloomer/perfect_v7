package com.perfect.manager.controller.master.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.org.MStaffPositionVo;
import com.perfect.bean.vo.master.user.MStaffExportVo;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.master.user.IMStaffService;
import com.perfect.excel.export.ExcelUtil;
import com.perfect.framework.base.controller.v1.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangxh
 */
@RestController
@RequestMapping(value = "/api/v1/staff")
@Slf4j
@Api("员工主表相关")
public class  MasterStaffController extends BaseController {

    @Autowired
    private IMStaffService service;

    @Autowired
    private RestTemplate restTemplate;

    @SysLogAnnotion("根据查询条件，获取员工主表信息")
    @ApiOperation("根据参数id，获取员工主表信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<MStaffVo>>> list(@RequestBody(required = false)
                                                                        MStaffVo searchCondition) {
        IPage<MStaffVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("员工主表数据更新保存")
    @ApiOperation("根据参数id，获取员工主表信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MStaffVo>> save(@RequestBody(required = false) MStaffVo bean, HttpServletRequest request) {
        if(service.update(bean, request.getSession()).isSuccess()){
            super.doResetUserSessionByStaffId(bean.getId());
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
    public ResponseEntity<JsonResult<MStaffVo>> insert(@RequestBody(required = false) MStaffVo bean, HttpServletRequest request) {
        if(service.insert(bean, request.getSession()).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("员工主表数据导出")
    @ApiOperation("根据选择的数据，员工主表数据导出")
    @PostMapping("/export_all")
    public void exportAll(@RequestBody(required = false) MStaffVo searchCondition, HttpServletResponse response)
        throws IOException {
        List<MStaffVo> searchResult = service.select(searchCondition);
        List<MStaffExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, MStaffExportVo.class);
        ExcelUtil<MStaffExportVo> util = new ExcelUtil<>(MStaffExportVo.class);
        util.exportExcel("员工主表数据导出", "员工主表数据", rtnList, response);
    }

    @SysLogAnnotion("员工主表数据导出")
    @ApiOperation("根据选择的数据，员工主表数据导出")
    @PostMapping("/export_selection")
    public void exportSelection(@RequestBody(required = false) List<MStaffVo> searchConditionList,
        HttpServletResponse response) throws IOException {
        List<MStaffVo> searchResult = service.exportBySelectIdsIn(searchConditionList);
        List<MStaffExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, MStaffExportVo.class);
        ExcelUtil<MStaffExportVo> util = new ExcelUtil<>(MStaffExportVo.class);
        util.exportExcel("员工主表数据导出", "员工主表数据", rtnList, response);
    }

    @SysLogAnnotion("员工主表数据逻辑删除复原")
    @ApiOperation("根据参数id，逻辑删除复原数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false) List<MStaffVo> searchConditionList) {
        service.deleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("查询岗位员工")
    @ApiOperation("根据参数id，查询岗位员工")
    @PostMapping("/position/list")
    @ResponseBody
    public ResponseEntity<JsonResult<MStaffPositionVo>> getPositionStaffData(@RequestBody(required = false) MStaffPositionVo searchCondition) {
        MStaffPositionVo vo = service.getPositionStaffData(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(vo));
    }

    @SysLogAnnotion("查询岗位员工")
    @ApiOperation("根据参数id，查询岗位员工")
    @PostMapping("/position/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> setPositionStaff(@RequestBody(required = false) MStaffPositionVo searchCondition) {
        service.setPositionStaff(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }
}
