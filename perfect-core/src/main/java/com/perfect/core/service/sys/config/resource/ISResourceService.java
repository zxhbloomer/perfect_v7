package com.perfect.core.service.sys.config.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.perfect.bean.entity.sys.config.resource.SResourceEntity;
import com.perfect.bean.vo.sys.config.resource.SResourceVo;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
public interface ISResourceService extends IService<SResourceEntity> {
    /**
     * 获取列表，页面查询
     */
    IPage<SResourceEntity> selectPage(SResourceVo searchCondition) ;

    /**
     * 获取所有数据
     */
    List<SResourceEntity> select(SResourceVo searchCondition) ;

    /**
     * 获取所选id的数据
     */
    List<SResourceEntity> selectIdsIn(List<SResourceVo> searchCondition) ;

    /**
     * 批量导入
     */
    boolean saveBatches(List<SResourceEntity> entityList);

    /**
     * 批量删除复原
     * @param searchCondition
     * @return
     */
    void deleteByIdsIn(List<SResourceVo> searchCondition);

}
