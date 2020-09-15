package com.perfect.manager.controller.master.org;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.entity.master.org.MGroupEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.master.org.MGroupExportVo;
import com.perfect.bean.vo.master.org.MGroupVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.master.org.IMGroupService;
import com.perfect.excel.export.ExcelUtil;
import com.perfect.framework.base.controller.v1.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangxh
 */
@RestController
@RequestMapping(value = "/api/v1/org/group")
@Slf4j
@Api("集团表相关")
public class OrgGroupController extends BaseController {

    @Autowired
    private IMGroupService service;

    @Autowired
    private RestTemplate restTemplate;

    @SysLogAnnotion("根据查询条件，获取集团主表信息")
    @ApiOperation("根据参数id，获取集团主表信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<MGroupVo>>> list(@RequestBody(required = false) MGroupVo searchCondition) {
        IPage<MGroupVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("集团主表数据更新保存")
    @ApiOperation("根据参数id，获取集团主表信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MGroupVo>> save(@RequestBody(required = false) MGroupEntity bean) {

        if(service.update(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("集团主表数据新增保存")
    @ApiOperation("根据参数id，获取集团主表信息")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<MGroupVo>> insert(@RequestBody(required = false) MGroupEntity bean) {
        if(service.insert(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("集团主表数据导出")
    @ApiOperation("根据选择的数据，集团主表数据导出")
    @PostMapping("/export_all")
    public void exportAll(@RequestBody(required = false) MGroupVo searchCondition, HttpServletResponse response) throws IOException {
        List<MGroupVo> searchResult = service.select(searchCondition);
        List<MGroupExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, MGroupExportVo.class);
        ExcelUtil<MGroupExportVo> util = new ExcelUtil<>(MGroupExportVo.class);
        util.exportExcel("集团主表数据导出", "集团主表数据", rtnList, response);
    }

    @SysLogAnnotion("集团主表数据导出")
    @ApiOperation("根据选择的数据，集团主表数据导出")
    @PostMapping("/export_selection")
    public void exportSelection(@RequestBody(required = false) List<MGroupVo> searchConditionList, HttpServletResponse response) throws IOException {
        List<MGroupVo> searchResult = service.selectIdsInForExport(searchConditionList);
        List<MGroupExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, MGroupExportVo.class);
        ExcelUtil<MGroupExportVo> util = new ExcelUtil<>(MGroupExportVo.class);
        util.exportExcel("集团主表数据导出", "集团主表数据", rtnList, response);
    }

    @SysLogAnnotion("集团主表数据逻辑删除复原")
    @ApiOperation("根据参数id，逻辑删除复原数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false) List<MGroupVo> searchConditionList) {
        service.deleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }
}
