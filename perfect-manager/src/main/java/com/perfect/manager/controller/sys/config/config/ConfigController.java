package com.perfect.manager.controller.sys.config.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.entity.sys.config.config.SConfigEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.sys.config.config.SConfigDataExportVo;
import com.perfect.bean.vo.sys.config.config.SConfigVo;
import com.perfect.bean.vo.sys.config.resource.SResourceExportVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.sys.config.config.ISConfigService;
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
@RequestMapping(value = "/api/v1/config")
@Slf4j
@Api("系统参数相关")
public class ConfigController extends BaseController {

    @Autowired
    private ISConfigService service;

    @Autowired
    private RestTemplate restTemplate;

    @SysLogAnnotion("根据查询条件，获取系统参数信息")
    @ApiOperation("根据参数id，获取系统参数信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<SConfigVo>>> list(@RequestBody(required = false) SConfigVo searchCondition) {
        IPage<SConfigVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("系统参数数据更新保存")
    @ApiOperation("根据参数id，获取系统参数信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SConfigEntity>> save(@RequestBody(required = false) SConfigEntity bean) {

        if(service.update(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.getById(bean.getId()),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("系统参数数据新增保存")
    @ApiOperation("根据参数id，获取系统参数信息")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SConfigEntity>> insert(@RequestBody(required = false) SConfigEntity bean) {
        // 默认启用
        bean.setIs_enable(true);
        if(service.insert(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.getById(bean.getId()),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("系统参数数据导出")
    @ApiOperation("根据选择的数据，系统参数数据导出")
    @PostMapping("/export_all")
    public void exportAll(@RequestBody(required = false) SConfigVo searchCondition, HttpServletResponse response) throws IOException {
        List<SConfigVo> searchResult = service.select(searchCondition);
        List<SConfigDataExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, SConfigDataExportVo.class);
        ExcelUtil<SConfigDataExportVo> util = new ExcelUtil<>(SConfigDataExportVo.class);
        util.exportExcel("系统参数数据导出", "系统参数数据", rtnList, response);
    }

    @SysLogAnnotion("系统参数数据导出")
    @ApiOperation("根据选择的数据，系统参数数据导出")
    @PostMapping("/export_selection")
    public void exportSelection(@RequestBody(required = false) List<SConfigVo> searchConditionList, HttpServletResponse response) throws IOException {
        List<SConfigVo> searchResult = service.selectIdsInForExport(searchConditionList);
        List<SResourceExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, SConfigDataExportVo.class);
        ExcelUtil<SResourceExportVo> util = new ExcelUtil<>(SResourceExportVo.class);
        util.exportExcel("系统参数数据导出", "系统参数数据", rtnList, response);
    }

    @SysLogAnnotion("根据选择的数据逻辑物理删除，部分数据")
    @ApiOperation("根据参数id，逻辑删除数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false) List<SConfigVo> searchConditionList) {
        service.realDeleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("根据选择的数据启用禁用，部分数据")
    @ApiOperation("根据参数id，启用禁用数据")
    @PostMapping("/enabled")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> enabled(@RequestBody(required = false) List<SConfigVo> searchConditionList) {
        service.enabledByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }
}
