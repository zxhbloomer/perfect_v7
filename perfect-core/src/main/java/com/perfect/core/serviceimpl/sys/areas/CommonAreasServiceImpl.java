package com.perfect.core.serviceimpl.sys.areas;

import com.perfect.bean.utils.common.tree.TreeUtil;
import com.perfect.bean.vo.sys.areas.SAreaCitiesVo;
import com.perfect.bean.vo.sys.areas.SAreaProvincesVo;
import com.perfect.bean.vo.sys.areas.SAreasCascaderTreeVo;
import com.perfect.bean.vo.sys.areas.SAreasVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.core.mapper.sys.areas.SAreasMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.areas.ICommonAreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.perfect.bean.vo.common.component.NameAndValueVo;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class CommonAreasServiceImpl extends BaseServiceImpl<SAreasMapper, NameAndValueVo> implements ICommonAreasService {

    @Autowired
    private SAreasMapper mapper;

    /**
     * 市
     * @param condition
     * @return
     */
    @Override
    public List<SAreaProvincesVo> getProvinces(SAreaProvincesVo condition) {
        return mapper.getProvinces(condition);
    }

    /**
     * 市
     * @param condition
     * @return
     */
    @Override
    public List<SAreaCitiesVo> getCities(SAreaCitiesVo condition) {
        return mapper.getCities(condition);
    }

    /**
     * 区
     * @param condition
     * @return
     */
    @Override
    public List<SAreasVo> getAreas(SAreasVo condition) {
        return mapper.getAreas(condition);
    }

    /**
     * 获取省市区级联
     * @return
     */
    @Cacheable(value = PerfectConstant.CACHE_PC.CACHE_AREAS_CASCADER)
    @Override
    public List<SAreasCascaderTreeVo> getAreasCascaderTreeVo() {
        List<SAreasCascaderTreeVo> listVo = mapper.getCascaderList();
        List<SAreasCascaderTreeVo> rtnList = TreeUtil.getTreeList(listVo);
        return rtnList;
    }
}
