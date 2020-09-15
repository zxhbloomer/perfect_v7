package com.perfect.core.mapper.sys.areas;

import java.util.List;

import com.perfect.bean.vo.sys.areas.SAreaCitiesVo;
import com.perfect.bean.vo.sys.areas.SAreaProvincesVo;
import com.perfect.bean.vo.sys.areas.SAreasCascaderTreeVo;
import com.perfect.bean.vo.sys.areas.SAreasVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.vo.common.component.NameAndValueVo;

/**
 * <p>
 * 获取下拉选项的 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Repository
public interface SAreasMapper extends BaseMapper<NameAndValueVo> {

    @Select( "   "
        + "  select                                                              "
        + "          t1.code as value,                                           "
        + "          t1.name as label,                                           "
        + "          t1.*                                                        "
        + "    FROM                                                              "
        + "       s_area_provinces t1                                            "
        + "   where true                                                         "
        + "     and (t1.code = #{p1.code,jdbcType=VARCHAR} or #{p1.code,jdbcType=VARCHAR} is null     )    "
        + "     order by t1.code    "
        + "      ")
    List<SAreaProvincesVo> getProvinces(@Param("p1") SAreaProvincesVo condition);

    @Select( "   "
            + "  select                                                              "
            + "          t1.code as value,                                           "
            + "          t1.name as label,                                           "
            + "          t1.*                                                        "
            + "    FROM                                                              "
            + "       s_area_cities t1                                               "
            + "   where true                                                         "
            + "     and (t1.province_code = #{p1.province_code,jdbcType=VARCHAR} or #{p1.province_code,jdbcType=VARCHAR} is null     )        "
            + "     and (t1.code = #{p1.code,jdbcType=VARCHAR} or #{p1.code,jdbcType=VARCHAR} is null     )                                          "
            + "     order by t1.code    "
            + "      ")
    List<SAreaCitiesVo> getCities(@Param("p1") SAreaCitiesVo condition);

    @Select( "   "
        + "  select                                                              "
        + "          t1.code as value,                                           "
        + "          t1.name as label,                                           "
        + "          t1.*                                                        "
        + "    FROM                                                              "
        + "       s_areas t1                                                     "
        + "   where true                                                         "
        + "     and (t1.province_code = #{p1.province_code,jdbcType=VARCHAR} or #{p1.province_code,jdbcType=VARCHAR} is null     )        "
        + "     and (t1.city_code = #{p1.city_code,jdbcType=VARCHAR} or #{p1.city_code,jdbcType=VARCHAR} is null     )        "
        + "     and (t1.code = #{p1.code,jdbcType=VARCHAR} or #{p1.code,jdbcType=VARCHAR} is null     )                                          "
        + "     order by t1.code    "
        + "      ")
    List<SAreasVo> getAreas(@Param("p1") SAreasVo condition);

    @Select( "   "
        + "     WITH recursive cte AS (                                                                           "
        + "     	SELECT                                                                                        "
        + "     	  t0.id,                                                                                      "
        + "     	  t0.id as value,                                                                             "
        + "     		t0.parent_id ,                                                                            "
        + "     		1 level,                                                                                  "
        + "     		t0.NAME,                                                                                  "
        + "     		t0.NAME label,                                                                            "
        + "     		t0.NAME depth_name,                                                                       "
        + "     		cast(t0.id as char(50)) depth_id                                                              "
        + "     	FROM                                                                                          "
        + "     		v_areas_tree t0                                                                           "
        + "     	where t0.parent_id is null                                                                    "
        + "     UNION ALL                                                                                         "
        + "     	SELECT                                                                                        "
        + "     	  t2.id,                                                                                      "
        + "     	  t2.id as value,                                                                             "
        + "     		t2.parent_id,                                                                             "
        + "     		t1.level + 1 as level,                                                                    "
        + "     		t2.NAME,                                                                                  "
        + "     		t2.NAME label,                                                                            "
        + "     		CONCAT( t1.depth_name, '>', t2.NAME ) depth_name,                                         "
        + "     		CONCAT( cast(t1.depth_id as char(50)),',',cast(t2.id as char(50))) depth_id                       "
        + "     	FROM                                                                                          "
        + "     		v_areas_tree t2,                                                                          "
        + "     		cte t1                                                                                    "
        + "     	WHERE                                                                                         "
        + "     	  t2.parent_id = t1.id                                                                        "
        + "     	)                                                                                             "
        + "     	select * from cte                                                                             "
        + "      ")
    List<SAreasCascaderTreeVo> getCascaderList();
}
