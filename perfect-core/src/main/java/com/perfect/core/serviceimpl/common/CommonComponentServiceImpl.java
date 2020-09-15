package com.perfect.core.serviceimpl.common;

import com.perfect.bean.vo.common.component.DictConditionVo;
import com.perfect.bean.vo.common.component.DictGroupVo;
import com.perfect.bean.vo.common.component.NameAndValueVo;
import com.perfect.bean.vo.common.component.PerfectComponentVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.core.mapper.common.CommonComponentMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.common.ICommonComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
public class CommonComponentServiceImpl extends BaseServiceImpl<CommonComponentMapper, NameAndValueVo> implements
    ICommonComponentService {

    @Autowired
    private CommonComponentMapper mapper;

    /**
     * 获取所有的下拉选项的数据bean
     * @return
     */
    @Override
    public PerfectComponentVo getAllSelectComponentBean(){
        PerfectComponentVo vo = new PerfectComponentVo();
        vo.setSelect_component_delete_map_normal(selectComponentDeleteMapNormal());
        vo.setSelect_component_delete_map_only_used_data(selectComponentDeleteMapOnlyUsedData());
        return vo;
    }

    /**
     * 下拉选项卡：删除类型字典
     * @return
     */
    @Override
    public List<NameAndValueVo> selectComponentDeleteMapNormal() {
        return mapper.getSelectDictDataNormal(PerfectDictConstant.DICT_SYS_DELETE_MAP);
    }

    /**
     * 下拉选项卡：删除类型字典
     * @return
     */
    @Override
    public List<NameAndValueVo> selectComponentDeleteMapOnlyUsedData() {
        return mapper.getSelectDictDataNormal(PerfectDictConstant.DICT_SYS_DELETE_MAP);
    }

    /**
     * 下拉选项卡：按参数查询
     * @return
     */
    @Cacheable(value = PerfectConstant.CACHE_PC.CACHE_DICT_TYPE, key = "#condition.para")
    @Override
    public List<NameAndValueVo> selectComponent(DictConditionVo condition) {
        return mapper.getSelectDictDataNormal(condition.getPara());
    }

    /**
     * 下拉选项卡，按组：按参数查询
     * @return
     */
    @Override
    public List<DictGroupVo> selectGroupComponent(DictConditionVo condition) {
        return mapper.getSelectDictGroupDataNormal(condition.getPara());
    }

    /**
     * 下拉选项卡：按参数查询，包含filter
     * @return
     */
    @Override
    public List<NameAndValueVo> selectComponentFilter(DictConditionVo condition) {
        return mapper.getSelectDictDataNormalFilter(condition);
    }

    /**
     * 根据字典类型，字典编码，获取字典值
     * @return
     */
    @Override
    public String getDictName(String code, String dict_value) {
        return mapper.getDictName(code, dict_value);
    }
}
