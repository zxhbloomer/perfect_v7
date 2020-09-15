package com.perfect.manager.controller.sys.pages;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.sys.pages.SPagesFunctionVo;
import com.perfect.common.annotations.RepeatSubmitAnnotion;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.core.service.sys.pages.ISPagesFunctionService;
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
@RequestMapping(value = "/api/v1/sys/pages_fun")
@Slf4j
@Api("页面按钮相关")
public class SysPagesFunctionController extends BaseController {

    @Autowired
    private ISPagesFunctionService service;

    @SysLogAnnotion("根据查询条件，获取vue页面按钮信息")
    @ApiOperation("根据参数id，获取vue页面按钮信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<IPage<SPagesFunctionVo>>> list(@RequestBody(required = false) SPagesFunctionVo searchCondition) {
        IPage<SPagesFunctionVo> entity = service.selectPage(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("页面按钮数据更新保存")
    @ApiOperation("页面按钮数据更新保存")
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SPagesFunctionVo>> save(@RequestBody(required = false) SPagesFunctionVo bean) {
        UpdateResult<SPagesFunctionVo> rtn = service.update(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"更新成功"));
        } else {
            throw new UpdateErrorException(rtn.getMessage());
        }
    }

    @SysLogAnnotion("快速编辑按钮")
    @ApiOperation("快速编辑按钮")
    @PostMapping("/save_assign")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SPagesFunctionVo>> save_assign(@RequestBody(required = false) SPagesFunctionVo bean) {
        UpdateResult<SPagesFunctionVo> rtn = service.update_assign(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }

    @SysLogAnnotion("页面按钮数据新增保存")
    @ApiOperation("页面按钮数据新增保存")
    @PostMapping("/insert")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<SPagesFunctionVo>> insert(@RequestBody(required = false)
        SPagesFunctionVo bean) {
        InsertResult<SPagesFunctionVo> rtn = service.insert(bean);
        if(rtn.isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK(rtn.getData(),"插入成功"));
        } else {
            throw new InsertErrorException("新增保存失败。");
        }
    }

    @SysLogAnnotion("页面按钮表数据物理删除，部分数据")
    @ApiOperation("页面按钮表数据物理删除，部分数据")
    @PostMapping("/delete")
    @ResponseBody
    @RepeatSubmitAnnotion
    public ResponseEntity<JsonResult<String>> delete(@RequestBody(required = false)
        List<SPagesFunctionVo> searchConditionList) {
        service.realDeleteByIdsIn(searchConditionList);
        return ResponseEntity.ok().body(ResultUtil.OK("OK"));
    }
}
