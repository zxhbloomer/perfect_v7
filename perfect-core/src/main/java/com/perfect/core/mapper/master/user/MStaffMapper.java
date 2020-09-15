package com.perfect.core.mapper.master.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.master.user.MStaffEntity;
import com.perfect.bean.vo.master.org.MPositionVo;
import com.perfect.bean.vo.master.org.MStaffPositionCountsVo;
import com.perfect.bean.vo.master.org.MStaffPositionVo;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.common.constant.PerfectDictConstant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 员工 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Repository
public interface MStaffMapper extends BaseMapper<MStaffEntity> {

    String common_select = "       "
        + "      SELECT                                                                                                         "
        + "            	t1.* ,                                                                                                  "
        + "            	t2.label as sex_text,                                                                                   "
        + "            	t3.label as service_text,                                                                               "
        + "            	t4.label as degree_text,                                                                                "
        + "            	t5.label as is_wed_text,                                                                                "
        + "            	t6.name as company_name,                                                                                "
        + "            	t6.simple_name as company_simple_name,                                                                  "
        + "            	t7.name as dept_name,                                                                                   "
        + "            	t7.simple_name as dept_simple_name,                                                                     "
        + "             c_staff.name as c_name,                                                                                 "
        + "             u_staff.name as u_name,                                                                                 "
        + "             t8.label as is_del_name,                                                                                "
        + "             t9.positions                                                                                            "
        + "        FROM                                                                                                         "
        + "            	m_staff t1                                                                                              "
        + "            	LEFT JOIN v_dict_info AS t2 ON t2.code = '" + PerfectDictConstant.DICT_SYS_SEX_TYPE + "' and t2.dict_value = t1.sex                      "
        + "            	LEFT JOIN v_dict_info AS t3 ON t3.code = '" + PerfectDictConstant.DICT_USR_SERVICE_TYPE + "' and t3.dict_value = t1.service              "
        + "            	LEFT JOIN v_dict_info AS t4 ON t4.code = '" + PerfectDictConstant.DICT_USR_DEGREE_TYPE + "' and t4.dict_value = t1.degree                "
        + "             LEFT JOIN v_dict_info AS t5 ON t5.code = '" + PerfectDictConstant.DICT_USR_WED_TYPE + "' and t5.dict_value = t1.is_wed                   "
        + "             LEFT JOIN m_company AS t6 ON t6.id = t1.company_id                                                      "
        + "             LEFT JOIN m_dept AS t7 ON t7.id = t1.dept_id                                                            "
        + "             LEFT JOIN m_staff c_staff ON t1.c_id = c_staff.id                                                       "
        + "             LEFT JOIN m_staff u_staff ON t1.u_id = u_staff.id                                                       "
        + "             LEFT JOIN v_dict_info AS t8 ON t8.code = '" + PerfectDictConstant.DICT_SYS_DELETE_MAP + "' and t8.dict_value = cast(t1.is_del as char(1)) "
        + "             LEFT JOIN (                                                                                             "
        + "                  select *                                                                                           "
        + "                    from (                                                                                           "
        + "                           SELECT                                                                                    "
        + "                                  subt1.staff_id,                                                                    "
        + "                                  count(*) over ( PARTITION BY subt1.staff_id ORDER BY subt2.NAME ) count_order,     "
        + "                                  count(*) over ( PARTITION BY subt1.staff_id  ) count_position,                     "
        + "                                  JSON_ARRAYAGG( JSON_OBJECT(                       "
        + "                                       'position_id', subt2.id, 'position_name', subt2.NAME, 'position_simple_name', subt2.simple_name                  "
        + "                                    ) ) over ( PARTITION BY subt1.staff_id ORDER BY subt2.NAME ) AS positions                                           "
        + "                              FROM                                                                                   "
        + "                                   m_staff_org subt1                                                                 "
        + "                                   INNER JOIN m_position subt2 ON subt1.serial_type = 'm_position' AND subt1.serial_id = subt2.id                       "
        + "                         ) tab"
        + "                   where tab.count_order = tab.count_position                                                        "
        + "                       ) t9 on t9.staff_id = t1.id                                                                   "
        + "       where true                                                                                                    "
        + "                    ";

    /**
     * 页面查询列表
     * @param page
     * @param searchCondition
     * @return
     */
    @Select("    "
        + common_select
        + "    and (t1.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null)           "
        + "    and (t1.is_del =#{p1.is_del,jdbcType=VARCHAR} or #{p1.is_del,jdbcType=VARCHAR} is null)                          "
        + "    and (t1.id =#{p1.id,jdbcType=BIGINT} or #{p1.id,jdbcType=BIGINT} is null)      "
        + "    and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)      "
        + "      ")
    IPage<MStaffVo> selectPage(Page page, @Param("p1") MStaffVo searchCondition);

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("    "
        + common_select
        + "    and (t1.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null)           "
        + "    and (t1.is_del =#{p1.is_del,jdbcType=VARCHAR} or #{p1.is_del,jdbcType=VARCHAR} is null)                          "
        + "    and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)      "
        + "      ")
    List<MStaffVo> select(@Param("p1") MStaffVo searchCondition);

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + common_select
        + "  and (t1.tenant_id  = #{p2} or #{p2} is null)                                                  "
        + "  and t1.id in                                                                                   "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>     "
        + "         #{item.id}                                                                              "
        + "        </foreach>                                                                               "
        + "  </script>                                                                                      ")
    List<MStaffVo> selectIdsIn(@Param("p1") List<MStaffVo> searchCondition, @Param("p2")Long tenant_id);

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + common_select
        + "  and (t1.tenant_id  = #{p2} or #{p2} is null)                                                  "
        + "  and t1.id in                                                                                   "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>     "
        + "         #{item.id}                                                                              "
        + "        </foreach>                                                                               "
        + "  </script>                                                                                      ")
    List<MStaffVo> exportSelectIdsIn(@Param("p1") List<MStaffVo> searchCondition, @Param("p2")Long tenant_id);

    /**
     * 按条件获取所有数据，没有分页
     * @param name
     * @return
     */
    @Select("    "
        + " select t.* "
        + "   from m_staff t "
        + "  where true "
        + "    and t.name =  #{p1}   "
        + "    and (t.id  =  #{p2} or #{p2} is null)   "
        + "    and (t.id  <> #{p3} or #{p3} is null)   "
        + "    and (t.tenant_id  = #{p4} or #{p4} is null)   "
        + "    and t.is_del =  0   "
        + "      ")
    List<MStaffEntity> selectByName(@Param("p1") String name, @Param("p2") Long equal_id,
                                      @Param("p3") Long not_equal_id, @Param("p4")Long tenant_id);

    /**
     * 按条件获取所有数据，没有分页
     * @param name
     * @return
     */
    @Select("    "
        + " select t.* "
        + "   from m_staff t "
        + "  where true "
        + "    and t.simple_name =  #{p1}   "
        + "    and (t.id  =  #{p2} or #{p2} is null)   "
        + "    and (t.id  <> #{p3} or #{p3} is null)   "
        + "    and (t.tenant_id  = #{p4} or #{p4} is null)   "
        + "    and t.is_del =  0   "
        + "      ")
    List<MStaffEntity> selectBySimpleName(@Param("p1") String name, @Param("p2") Long equal_id,
                                            @Param("p3") Long not_equal_id, @Param("p4")Long tenant_id);

    /**
     * 页面查询列表
     * @return
     */
    @Select("    "
        + common_select
        + "    and (t1.id =#{p1.id,jdbcType=BIGINT} or #{p1.id,jdbcType=BIGINT} is null)      "
        + "    and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)      "
        + "      ")
    MStaffVo selectByid(@Param("p1") MStaffVo searchCondition);

    /**
     * 放到session中
     * @param p1
     * @return
     */
    @Select( "                                                  "
        + " select t.*                                          "
        + "   from m_staff t                                    "
        + "  where t.user_id = #{p1}                            "
        + "    and (t1.tenant_id = #{p2} or #{p2} is null )    "
        + "                                                     ")
    MStaffEntity getDataByUser_id(@Param("p1") Long p1, @Param("p2")Long tenant_id);

    /**
     * 查询在组织架构中是否存在有被使用的数据
     * @param searchCondition
     * @return
     */
    @Select("                                                                                                   "
        + " select count(1)                                                                                     "
        + "   from m_org t                                                                                      "
        + "  where true                                                                                         "
        + "    and t.serial_type = '" + PerfectDictConstant.DICT_ORG_SETTING_TYPE_STAFF_SERIAL_TYPE + "'        "
        + "    and t.serial_id = #{p1.id,jdbcType=BIGINT}                                                       "
        + "                                                                                                     ")
    int isExistsInOrg(@Param("p1") MStaffEntity searchCondition);

    /**
     * 查询岗位员工
     * @param searchCondition
     * @return
     */
    @Select("                                                                                                                                        "
        + "   SELECT                                                                                                                                 "
        + "           t1.*,                                                                                                                          "
        + "           t2.group_full_name,                                                                                                            "
        + "           t2.group_full_simple_name,                                                                                                     "
        + "           t2.company_name,                                                                                                               "
        + "           t2.company_simple_name,                                                                                                        "
        + "           t2.dept_full_name,                                                                                                             "
        + "           t2.dept_full_simple_name,                                                                                                      "
        + "           t3.staff_id,                                                                                                                   "
        + "           (case when t3.staff_id is null then 0 else 1 end) settled                                                                      "
        + "      FROM                                                                                                                                "
        + "      m_position t1                                                                                                                       "
        + " LEFT JOIN v_org_route_dept_position_info t2 ON t1.id = t2.current_id                                                                     "
        + " LEFT JOIN m_staff_org t3 on t3.serial_type = '"+ PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE +"'                      "
        + "       and t3.serial_id = t1.id                                                                                                           "
        + "       and t3.staff_id = #{p1.id,jdbcType=BIGINT}                                                                                         "
        + "     where true                                                                                                                           "
        + "     and (                                                                                                                                "
        + "           case                                                                                                                           "
        + "           when 0=#{p1.active_tabs_index,jdbcType=INTEGER} then true                                                                      "
        + "           when 1=#{p1.active_tabs_index,jdbcType=INTEGER} then staff_id is not null                                                      "
        + "           when 2=#{p1.active_tabs_index,jdbcType=INTEGER} then staff_id is null                                                          "
        + "            end                                                                                                                           "
        + "        )                                                                                                                                 "
        + "     and (CONCAT(t1.name,t1.simple_name) like CONCAT ('%',#{p1.position_name,jdbcType=VARCHAR},'%') or #{p1.position_name,jdbcType=VARCHAR} ='')                           "
        + "     and (CONCAT(t2.group_full_name,t2.group_full_simple_name) like CONCAT ('%',#{p1.group_name,jdbcType=VARCHAR},'%') or #{p1.group_name,jdbcType=VARCHAR} ='')           "
        + "     and (CONCAT(t2.company_name,t2.company_simple_name) like CONCAT ('%',#{p1.company_name,jdbcType=VARCHAR},'%') or #{p1.company_name,jdbcType=VARCHAR} ='')             "
        + "     and (CONCAT(t2.dept_full_name,t2.dept_full_simple_name) like CONCAT ('%',#{p1.dept_name,jdbcType=VARCHAR},'%') or #{p1.dept_name,jdbcType=VARCHAR} ='')               "
        + "     and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)                                       "
        + "                                                                                                                                          "
    )
    IPage<MPositionVo> getPositionStaffData(Page page, @Param("p1") MStaffPositionVo searchCondition);

    /**
     * 查询岗位员工，所有数据count
     * @param searchCondition
     * @return
     */
    @Select("                                                                                                                                        "
        + "   select *                                                                                                                               "
        + "     from (                                                                                                                               "
        + "             SELECT t1.id,                                                                                                                "
        + "                    0 as active_tabs_index,                                                                                               "
        + "                    count(1) as `count`                                                                                                   "
        + "               FROM m_position t1                                                                                                         "
        + "          LEFT JOIN v_org_route_dept_position_info t2 ON t1.id = t2.current_id                                                            "
        + "          LEFT JOIN m_staff_org t3 on t3.serial_type = '"+ PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE +"'             "
        + "                and t3.serial_id = t1.id and t3.staff_id = #{p1.id,jdbcType=BIGINT}                                                       "
        + "              where (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)                            "
        + "                and (CONCAT(t1.name,t1.simple_name) like CONCAT ('%',#{p1.position_name,jdbcType=VARCHAR},'%') or #{p1.position_name,jdbcType=VARCHAR} ='')                           "
        + "                and (CONCAT(t2.group_full_name,t2.group_full_simple_name) like CONCAT ('%',#{p1.group_name,jdbcType=VARCHAR},'%') or #{p1.group_name,jdbcType=VARCHAR} ='')           "
        + "                and (CONCAT(t2.company_name,t2.company_simple_name) like CONCAT ('%',#{p1.company_name,jdbcType=VARCHAR},'%') or #{p1.company_name,jdbcType=VARCHAR} ='')             "
        + "                and (CONCAT(t2.dept_full_name,t2.dept_full_simple_name) like CONCAT ('%',#{p1.dept_name,jdbcType=VARCHAR},'%') or #{p1.dept_name,jdbcType=VARCHAR} ='')               "
        + "          union all                                                                                                                       "
        + "             SELECT t1.id,                                                                                                                "
        + "                    1 as active_tabs_index,                                                                                               "
        + "                    count(1) as `count`                                                                                                   "
        + "               FROM m_position t1                                                                                                         "
        + "          LEFT JOIN v_org_route_dept_position_info t2 ON t1.id = t2.current_id                                                            "
        + "          LEFT JOIN m_staff_org t3 on t3.serial_type = '"+ PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE +"'             "
        + "                and t3.serial_id = t1.id and t3.staff_id = #{p1.id,jdbcType=BIGINT}                                                       "
        + "              where staff_id is not null                                                                                                  "
        + "                and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)                            "
        + "                and (CONCAT(t1.name,t1.simple_name) like CONCAT ('%',#{p1.position_name,jdbcType=VARCHAR},'%') or #{p1.position_name,jdbcType=VARCHAR} ='')                           "
        + "                and (CONCAT(t2.group_full_name,t2.group_full_simple_name) like CONCAT ('%',#{p1.group_name,jdbcType=VARCHAR},'%') or #{p1.group_name,jdbcType=VARCHAR} ='')           "
        + "                and (CONCAT(t2.company_name,t2.company_simple_name) like CONCAT ('%',#{p1.company_name,jdbcType=VARCHAR},'%') or #{p1.company_name,jdbcType=VARCHAR} ='')             "
        + "                and (CONCAT(t2.dept_full_name,t2.dept_full_simple_name) like CONCAT ('%',#{p1.dept_name,jdbcType=VARCHAR},'%') or #{p1.dept_name,jdbcType=VARCHAR} ='')               "
        + "          union all                                                                                                                       "
        + "             SELECT t1.id,                                                                                                                "
        + "                    2 as active_tabs_index,                                                                                               "
        + "                    count(1) as `count`                                                                                                   "
        + "               FROM m_position t1                                                                                                         "
        + "          LEFT JOIN v_org_route_dept_position_info t2 ON t1.id = t2.current_id                                                            "
        + "          LEFT JOIN m_staff_org t3 on t3.serial_type = '"+ PerfectDictConstant.DICT_ORG_SETTING_TYPE_POSITION_SERIAL_TYPE +"'             "
        + "                and t3.serial_id = t1.id and t3.staff_id = #{p1.id,jdbcType=BIGINT}                                                       "
        + "              where staff_id is null                                                                                                      "
        + "                and (t1.tenant_id =#{p1.tenant_id,jdbcType=BIGINT} or #{p1.tenant_id,jdbcType=BIGINT} is null)                            "
        + "                and (CONCAT(t1.name,t1.simple_name) like CONCAT ('%',#{p1.position_name,jdbcType=VARCHAR},'%') or #{p1.position_name,jdbcType=VARCHAR} ='')                           "
        + "                and (CONCAT(t2.group_full_name,t2.group_full_simple_name) like CONCAT ('%',#{p1.group_name,jdbcType=VARCHAR},'%') or #{p1.group_name,jdbcType=VARCHAR} ='')           "
        + "                and (CONCAT(t2.company_name,t2.company_simple_name) like CONCAT ('%',#{p1.company_name,jdbcType=VARCHAR},'%') or #{p1.company_name,jdbcType=VARCHAR} ='')             "
        + "                and (CONCAT(t2.dept_full_name,t2.dept_full_simple_name) like CONCAT ('%',#{p1.dept_name,jdbcType=VARCHAR},'%') or #{p1.dept_name,jdbcType=VARCHAR} ='')               "
        + "       ) t                                                                                                                                "
        + "    ORDER BY t.active_tabs_index                                                                                                          "
        + "                                                                                                                                          "
    )
    List<MStaffPositionCountsVo> getPositionStaffDataCount(@Param("p1") MStaffPositionVo searchCondition);
}
