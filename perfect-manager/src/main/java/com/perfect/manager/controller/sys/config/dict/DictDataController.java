package com.perfect.manager.controller.sys.config.dict;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.entity.sys.config.dict.SDictDataEntity;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.sys.config.dict.SDictDataVo;
import com.perfect.bean.vo.sys.config.dict.SDictTypeExportVo;
import com.perfect.bean.vo.sys.config.resource.SResourceExportVo;
import com.perfect.bean.vo.sys.rbac.role.SRoleVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.enums.ResultEnum;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.service.sys.config.dict.ISDictDataService;
import com.perfect.excel.export.ExcelUtil;
import com.perfect.excel.upload.PerfectExcelReader;
import com.perfect.framework.base.controller.v1.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangxh
 */
@RestController
@RequestMapping(value = "/api/v1/dictdata")
@Slf4j
@Api("字典数据表相关")
public class DictDataController extends BaseController {

    @Autowired
    private ISDictDataService service;

    @Autowired
    private RestTemplate restTemplate;

    @SysLogAnnotion("根据参数id，获取字典数据表信息")
    @ApiOperation("根据参数id，获取字典数据表信息")
    @PostMapping("{ id }")
    @ResponseBody
    public ResponseEntity<JsonResult<SDictDataEntity>> info(@RequestParam("id") String id) {

        SDictDataEntity entity = service.getById(id);

//        ResponseEntity<OAuth2AccessToken
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("根据查询条件，获取字典数据表信息")
    @ApiOperation("根据参数id，获取字典数据表信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<SDictDataVo>>> list(@RequestBody(required = false) SDictDataVo searchCondition)  {
        IPage<SDictDataVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("字典数据表数据更新保存")
    @ApiOperation("根据参数id，获取字典数据表信息")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SDictDataVo>> save(@RequestBody(required = false) SDictDataEntity bean) {
        if(service.update(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("字典数据表数据新增保存")
    @ApiOperation("根据参数id，获取字典数据表信息")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SDictDataVo>> insert(@RequestBody(required = false) SDictDataEntity bean) {
        if(service.insert(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(service.selectByid(bean.getId()),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("字典数据表数据导出")
    @ApiOperation("根据选择的数据，字典数据表数据导出")
    @PostMapping("/export_all")
    public void exportAll(@RequestBody(required = false) SDictDataVo searchCondition, HttpServletResponse response) throws IOException {
        List<SDictDataVo> searchResult = service.select(searchCondition);
        List<SDictTypeExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, SDictTypeExportVo.class);
        ExcelUtil<SDictTypeExportVo> util = new ExcelUtil<>(SDictTypeExportVo.class);
        util.exportExcel("字典数据表数据导出", "字典数据表数据", rtnList, response);
    }

    @SysLogAnnotion("字典数据表数据导出")
    @ApiOperation("根据选择的数据，字典数据表数据导出")
    @PostMapping("/export_selection")
    public void exportSelection(@RequestBody(required = false) List<SDictDataVo> searchConditionList, HttpServletResponse response) throws IOException {
        List<SDictDataVo> searchResult = service.selectIdsIn(searchConditionList);
        List<SResourceExportVo> rtnList = BeanUtilsSupport.copyProperties(searchResult, SDictTypeExportVo.class);
        ExcelUtil<SResourceExportVo> util = new ExcelUtil<>(SResourceExportVo.class);
        util.exportExcel("字典数据表数据导出", "字典数据表数据", rtnList, response);
    }

    @SysLogAnnotion("字典数据表数据逻辑删除复原")
    @ApiOperation("根据参数id，逻辑删除复原数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false) List<SDictDataVo> searchConditionList) {
        service.deleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }

    @SysLogAnnotion("字典数据导入")
    @ApiOperation("字典数据模板导入")
    @PostMapping("/import")
    public ResponseEntity<JsonResult<Object>> importData(@RequestBody(required = false) SDictDataVo uploadData,
        HttpServletResponse response) throws Exception {

        // file bean 保存数据库

        // 文件下载并check类型
        // 1、获取模板配置类
        //        String json = "{\"dataRows\":{\"dataCols\":[{\"index\":0,\"name\":\"type\"},{\"convertor\":\"datetime\",\"index\":1,\"listValiDator\":[{\"validtorName\":\"required\"},{\"param\":[{\"name\":\"dateFormat\",\"value\":\"yyyy-MM-dd HH:mm:ss\"}],\"validtorName\":\"datetime\"}],\"name\":\"code\"},{\"index\":2,\"name\":\"name\"},{\"index\":3,\"name\":\"descr\"},{\"index\":4,\"name\":\"simple_name\"}]},\"titleRows\":[{\"cols\":[{\"colSpan\":1,\"title\":\"角色类型\"},{\"colSpan\":1,\"title\":\"角色编码\"},{\"colSpan\":1,\"title\":\"角色名称\"},{\"colSpan\":1,\"title\":\"说明\"},{\"colSpan\":1,\"title\":\"简称\"}]}]}";
        String json = "{\"dataRows\":{\"dataCols\":[{\"index\":0,\"name\":\"type\"},{\"index\":1,\"name\":\"code\"},{\"index\":2,\"name\":\"name\"},{\"index\":3,\"name\":\"descr\"},{\"index\":4,\"name\":\"simple_name\"}]},\"titleRows\":[{\"cols\":[{\"colSpan\":1,\"title\":\"角色类型\"},{\"colSpan\":1,\"title\":\"角色编码\"},{\"colSpan\":1,\"title\":\"角色名称\"},{\"colSpan\":1,\"title\":\"说明\"},{\"colSpan\":1,\"title\":\"简称\"}]}]}";
        PerfectExcelReader pr = super.downloadExcelAndImportData(uploadData.getFsType2Url(), json);
        List<SDictDataEntity> beans = pr.readBeans(SDictDataEntity.class);
        SRoleVo SRoleVo = new SRoleVo();
        if (pr.isDataValid()) {
            pr.closeAll();
            // 读取没有错误，开始插入
            service.saveBatches(beans);
            return ResponseEntity.ok().body(ResultUtil.OK(beans));
        } else {
            // 读取失败，需要返回错误
            File rtnFile = pr.getValidateResultsInFile("字典数据导入错误");
            pr.closeAll();
            SRoleVo errorInfo = super.uploadFile(rtnFile.getAbsolutePath(), SRoleVo.class);
            return ResponseEntity.ok().body(ResultUtil.OK(errorInfo, ResultEnum.IMPORT_DATA_ERROR));
        }
    }

    @SysLogAnnotion("字典数据表排序后保存")
    @ApiOperation("list数据的保存")
    @PostMapping("/save_list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<SDictDataVo>>> saveList(@RequestBody(required = false) List<SDictDataVo> beanList) {
        UpdateResult<List<SDictDataVo>> result = service.saveList(beanList);
        if(result.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(result.getData(),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }
}
