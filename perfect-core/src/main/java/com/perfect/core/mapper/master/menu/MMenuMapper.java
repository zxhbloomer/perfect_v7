package com.perfect.core.mapper.master.menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.entity.master.menu.MMenuEntity;
import com.perfect.bean.vo.master.menu.MMenuDataVo;
import com.perfect.bean.vo.master.menu.MMenuPageFunctionVo;
import com.perfect.bean.vo.master.menu.MMenuRedirectVo;
import com.perfect.bean.vo.master.menu.MMenuVo;
import com.perfect.common.constant.PerfectDictConstant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 集团主表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Repository
public interface MMenuMapper extends BaseMapper<MMenuEntity> {

    String commonTreeGrid = "    "
        + "                                                                                            "
        + "      with recursive tab1  as (                                                             "
        + "     select t0.id,                                                                          "
        + "            t0.parent_id,                                                                   "
        + "            1 level,                                                                        "
        + "            t0.name,                                                                        "
        + "            t0.name  as depth_name,                                                         "
        + "            cast(t0.id as char(50)) depth_id                                                "
        + "       from m_menu t0                                                                       "
        + "      where t0.parent_id is null                                                            "
        + "      union all                                                                             "
        + "      select t2.id,                                                                         "
        + "             t2.parent_id,                                                                  "
        + "             t1.level + 1 as level,                                                         "
        + "             t2.name,                                                                       "
        + "             CONCAT( t1.depth_name,'>',t2.name)  depth_name,                                "
        + "             CONCAT( cast(t1.depth_id as char(50)),',',cast(t2.id as char(50)))  depth_id   "
        + "        from m_menu t2,                                                                     "
        + "             tab1 t1                                                                        "
        + "       where t2.parent_id = t1.id                                                           "
        + "       )                                                                                    "
        + "	  select t1.id,                                                                            "
        + "			 t1.id as value,                                                                   "  // 级联value
        + "			 t1.name,                                                                          "
        + "			 t1.name as label,                                                                 " // 级联label
        + "             t1.parent_id,                                                                  "
        + "             t2.root_id,                                                                    "
        + "             t1.level,                                                                      "
        + "             t1.depth_name,                                                                 "
        + "             t1.depth_id,                                                                   "
        + "             t4.depth_id as parent_depth_id,                                                "
        + "             t2.code,                                                                       "
        + "             t2.is_default,                                                                 "
        + "             t2.type,                                                                       "
        + "             t3.label as type_name,                                                         "
        + "             t2.visible,                                                                    "
        + "             t2.perms,                                                                      "
        + "             t2.page_id,                                                                    "
        + "             t2.page_code,                                                                  "
        + "             t2.path,                                                                       "
        + "             t2.route_name,                                                                 "
        + "             t2.meta_title,                                                                 "
        + "             t2.meta_icon,                                                                  "
        + "             t2.component,                                                                  "
        + "             t2.affix,                                                                      "
        + "             t2.descr,                                                                      "
        + "             t2.tenant_id,                                                                  "
        + "             t2.c_id,                                                                       "
        + "             t2.c_time,                                                                     "
        + "             t2.u_id,                                                                       "
        + "             t2.u_time,                                                                     "
        + "             t2.dbversion,                                                                  "
        + "             t5.function_info,                                                              "
        + "             c_staff.name as c_name,                                                        "
        + "             u_staff.name as u_name                                                         "
        + "         from tab1 t1                                                                       "
        + "   inner join m_menu t2                                                                     "
        + "		   on t1.id = t2.id                                                                    "
        + "   left join v_dict_info t3                                                                 "
        + "		   on t3.code = '" + PerfectDictConstant.DICT_SYS_MENU_TYPE + "' and t3.dict_value = t2.type "
        + "	 LEFT join (                                                                               "
        + "				with recursive tab1  as (                                                      "
        + "             select t0.id,                                                                  "
        + "                    t0.parent_id,                                                           "
        + "                    1 level,                                                                "
        + "                    t0.name,                                                                "
        + "                    t0.name  as depth_name,                                                 "
        + "                    cast(t0.id as char(50)) depth_id                                        "
        + "               from m_menu t0                                                               "
        + "              where t0.parent_id is null                                                    "
        + "              union all                                                                     "
        + "              select t2.id,                                                                 "
        + "											 t2.parent_id,                                     "
        + "                     t1.level + 1 as level,                                                 "
        + "                     t2.name,                                                               "
        + "                     CONCAT( t1.name,'>',t2.name)  depth_name,                              "
        + "                     CONCAT( cast(t1.id as char(50)),',',cast(t2.id as char(50)))  depth_id "
        + "                from m_menu t2,                                                             "
        + "                     tab1 t1                                                                "
        + "               where t2.parent_id = t1.id                                                   "
        + "               ) select id,depth_id from tab1                                               "
        + "							) t4 on t4.id = t1.parent_id                                       "
        + "  LEFT JOIN (                                                                               " // 按钮数据，按json方式
        + "          SELECT                                                                            "
        + "          		subtab1.id,                                                                "
        + "          		JSON_ARRAYAGG( JSON_OBJECT( 'id', subtab3.id,                              "
        + "                                             'code', subtab3.CODE,                          "
        + "                                             'name', subtab3.NAME,                          "
        + "                                             'perms', subtab2.perms ) ) AS function_info    "
        + "          	FROM                                                                           "
        + "          		s_pages subtab1                                                            "
        + "          		LEFT JOIN s_pages_function subtab2 ON subtab1.id = subtab2.page_id         "
        + "          		LEFT JOIN s_function subtab3 on subtab3.id = subtab2.function_id           "
        + "          	WHERE                                                                          "
        + "          		subtab2.id IS NOT NULL                                                     "
        + "          	GROUP BY                                                                       "
        + "          		subtab1.id                                                                 "
        + "            ) t5 on t5.id = t2.page_id                                                      "
        + "  LEFT JOIN m_staff c_staff ON t2.c_id = c_staff.id                                                        "
        + "  LEFT JOIN m_staff u_staff ON t2.u_id = u_staff.id                                                        "
        + "                                                                                            ";

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("    "
        + commonTreeGrid
        + "  where true "
        + "    AND (t2.tenant_id = #{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)        "
        + "    and (t1.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "    and (t2.visible =#{p1.visible,jdbcType=VARCHAR} or #{p1.visible,jdbcType=VARCHAR} is null)             "
        + "  order by t2.code                                                                                         "
        + "      ")
    @Results({
        @Result(property = "module_info", column = "module_info", javaType = List.class, typeHandler = com.perfect.core.config.mybatis.typehandlers.JsonArrayTypeHandler.class),
    })
    List<MMenuDataVo> select(@Param("p1") MMenuDataVo searchCondition);

    /**
     * 获取所有的菜单按钮
     * @param searchCondition
     * @return
     */
    @Select(" <script>   "
        + "     SELECT distinct t1.id,                                                                              "
        + "            t1.code,                                                                                     "
        + "            t1.name,                                                                                     "
        + "            t1.sort                                                                                      "
        + "       FROM s_function t1                                                                                "
        + " inner join s_pages_function t2 on t1.id = t2.function_id                                                "
        + " inner join m_menu t3 on t3.page_id = t2.page_id                                                         "
        + "   <if test='p1.root_ids.length!=0' >                                                                    "
        + "        and t3.root_id in                                                                                "
        + "        <foreach collection='p1.root_ids' item='item' index='index' open='(' separator=',' close=')'>    "
        + "         #{item}                                                                                         "
        + "        </foreach>                                                                                       "
        + "   </if>                                                                                                 "
        + "        and (t3.tenant_id = #{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)  "
        + "   order by t1.sort                                                                                      "
        + "  </script>    ")
    List<MMenuPageFunctionVo> getAllMenuButton(@Param("p1") MMenuDataVo searchCondition);

    /**
     *
     * 根据id获取数据
     *
     * @param id
     * @return
     */
    @Select("    "
        + commonTreeGrid
        + "  where true "
        + "    and t2.id =#{p1} "
        + "      ")
    MMenuDataVo selectId(@Param("p1") Long id);

    /**
     * 级联,按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("    "
        + commonTreeGrid
        + "  where true "
        + "    AND (t2.tenant_id = #{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)        "
        + "    and (t1.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "    and (t2.visible =#{p1.visible,jdbcType=VARCHAR} or #{p1.visible,jdbcType=VARCHAR} is null) "
        + "      ")
    List<MMenuDataVo> getCascaderList(@Param("p1") MMenuVo searchCondition);

    /**
     * 级联,按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("    "
        + commonTreeGrid
        + "  where true "
        + "    and (t1.id #{p1.id)"
        + "      ")
    MMenuVo getCascaderGet(@Param("p1") MMenuVo searchCondition);

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from m_menu t "
        + "  where t.id in "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item.id}  "
        + "        </foreach>"
        + "  </script>")
    List<MMenuEntity> selectIdsIn(@Param("p1") List<MMenuVo> searchCondition);

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Delete("                                                             "
        + " delete                                                        "
        + "   from m_menu                                                 "
        + "  where true                                                   "
        + "    and code like CONCAT (#{p1.code,jdbcType=VARCHAR},'%')     "
        + "      ")
    void realDeleteByCode(@Param("p1") MMenuDataVo searchCondition);


    /**
     * 拖拽的保存
     * @param entity
     * @return
     */
    @Update("                                                                        "
        + "    update m_menu t                                                        "
        + "       set t.parent_id = #{p1.parent_id,jdbcType=BIGINT} ,                "
        + "           t.code = #{p1.code,jdbcType=VARCHAR} ,                         "
        + "           t.son_count = #{p1.son_count,jdbcType=INTEGER},                "
        + "           t.u_id = #{p1.u_id,jdbcType=BIGINT},                           "
        + "           t.u_time = #{p1.u_time,jdbcType=TIMESTAMP}                     "
        + "     where t.id = #{p1.id,jdbcType=BIGINT}                                "
        + "                                                                          "
    )
    int updateDragSave(@Param("p1") MMenuEntity entity);

    /**
     * 删除菜单的同时，需要考虑删除重定向数据
     * @param searchCondition
     * @return
     */
    @Delete("                                                                                  "
        + "  delete from m_menu_redirect t1                                                    "
        + "   where EXISTS (                                                                   "
        + "                  select true                                                       "
        + "                    from m_menu t2                                                  "
        + "                   where true                                                       "
        + "                     and t2.code like CONCAT (#{p1.code,jdbcType=VARCHAR},'%')      "
        + "                     and t1.menu_page_id = t2.id                                    "
        + "      ")
    int delRedirect(@Param("p1") MMenuDataVo searchCondition);

    /**
     * 获取重定向数据
     * @param tenant_id
     * @return
     */
    @Select("                                                                                 "
        + "          SELECT t1.id,                                                            "
        + "                 t1.root_id,                                                       "
        + "                 t1.page_id,                                                       "
        + "                 t1.menu_page_id,                                                  "
        + "                 t2.meta_title as name                                             "
        + "            FROM m_menu_redirect AS t1                                             "
        + "      INNER JOIN m_menu t2 ON t1.menu_page_id = t2.id                              "
        + "             and t2.tenant_id = #{p1}                                       "
        + "                                                                                   "
        + "      ")
    MMenuRedirectVo getRedirectData(@Param("p1") Long tenant_id);
}
