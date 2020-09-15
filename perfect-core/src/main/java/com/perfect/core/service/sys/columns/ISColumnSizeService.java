package com.perfect.core.service.sys.columns;

import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.columns.SColumnSizeEntity;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.vo.sys.columns.SColumnSizeVo;

import java.util.List;

/**
 * <p>
 * 表格列宽 服务类
 * </p>
 *
 * @author zxh
 * @since 2020-06-09
 */
public interface ISColumnSizeService extends IService<SColumnSizeEntity> {

    /**
     * 获取列表，页面查询
     */
    List<SColumnSizeVo> getData(SColumnSizeVo searchCondition) ;

    /**
     * 获取列表，页面查询
     */
    UpdateResult<Boolean> saveColumnsSize(SColumnSizeVo searchCondition) ;
}
