package com.perfect.manager.controller.sys.columns;

import com.perfect.bean.pojo.result.JsonResult;
import com.perfect.bean.result.utils.v1.ResultUtil;
import com.perfect.bean.vo.sys.columns.SColumnSizeVo;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.core.service.sys.columns.ISColumnSizeService;
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
@RequestMapping(value = "/api/v1/columns")
@Slf4j
@Api("vue页面上表格列宽度相关")
public class ColumnsController extends BaseController {

    @Autowired
    private ISColumnSizeService service;

    @SysLogAnnotion("根据查询条件，vue页面上表格列宽度信息")
    @ApiOperation("根据参数id，vue页面上表格列宽度信息")
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<JsonResult<List<SColumnSizeVo>>> list(@RequestBody(required = false) SColumnSizeVo searchCondition) {
        /** 获取员工id */
        searchCondition.setStaff_id(getUserSessionStaffId());
        /** 拼接缓存的key */
        String cache_key = searchCondition.getPage_code()
            +":"
            + (searchCondition.getType() ==null ? "" : searchCondition.getType())
            +":"
            + searchCondition.getStaff_id();
        searchCondition.setCache_key(cache_key);
        List<SColumnSizeVo> entity = service.getData(searchCondition);
        return ResponseEntity.ok().body(ResultUtil.OK(entity));
    }

    @SysLogAnnotion("更新保存")
    @ApiOperation("更新保存")
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<JsonResult<String>> saveColumnsSize(@RequestBody(required = false) SColumnSizeVo bean) {
        /** 获取员工id */
        bean.setStaff_id(getUserSessionStaffId());
        /** 拼接缓存的key */
        String cache_key = bean.getPage_code()
            +":"
            + (bean.getType() ==null ? "" : bean.getType())
            +":"
            + bean.getStaff_id();
        bean.setCache_key(cache_key);
        if(service.saveColumnsSize(bean).isSuccess()){
            return ResponseEntity.ok().body(ResultUtil.OK("更新成功","更新成功"));
        } else {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
    }
}
