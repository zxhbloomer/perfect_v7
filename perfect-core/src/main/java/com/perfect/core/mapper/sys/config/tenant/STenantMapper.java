package com.perfect.core.mapper.sys.config.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.sys.config.tenant.STenantEntity;
import com.perfect.bean.vo.sys.config.tenant.STenantTreeVo;
import com.perfect.bean.vo.sys.config.tenant.STenantVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 模块表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
@Repository
public interface STenantMapper extends BaseMapper<STenantEntity> {

    /**
     * 树查询使用
     */
    String commonTreeSql = "   "
        + "           with recursive tab1  as (               "
        + "           select t0.id,                                                     "
        + "                  t0.parent_id,                                              "
        + "                  1 level,                                                   "
        + "                  t0.name,                                                   "
        + "                  t0.name  as depth_name                                     "
        + "             from s_tenant t0                                                "
        + "            where t0.parent_id is null                                       "
        + "            union all                                                        "
        + "            select t2.id,                                                    "
        + "                   t2.parent_id,                                             "
        + "                   t1.level + 1 as level,                                    "
        + "                   t2.name,                                                  "
        + "                   CONCAT( t1.depth_name,'>',t2.name)  depth_name            "
        + "              from s_tenant t2,                                              "
        + "                   tab1 t1                                                   "
        + "             where t2.parent_id = t1.id                                      "
        + "             )                                                               "
        + "                select t1.id,                                                "
        + "                   t1.parent_id,                                             "
        + "                   t1.id as value,                                           "  //级联使用，父结点id
        + "                   t1.level,                                                 "
        + "                   t1.name,                                                  "
        + "                   t1.depth_name,                                            "
        + "                   t3.code as parent_code,                                   "
        + "                   t3.serial_no as parent_serial_no,                         "
        + "                   t2.serial_no,                                             "
        + "                   t2.code,                                                  "
        + "                   t2.name as label,                                         "
        + "                   t2.is_enable,                                             "
        + "                   t2.enable_time,                                           "
        + "                   t2.disable_time,                                          "
        + "                   t2.is_freeze,                                             "
        + "                   t2.is_leaf,                                               "
        + "                   t2.sort,                                                  "
        + "                   t2.descr,                                                 "
        + "                   t2.is_del,                                                "
        + "                   t2.c_id,                                                  "
        + "                   t2.c_time,                                                "
        + "                   t2.u_id,                                                  "
        + "                   t2.u_time,                                                "
        + "                   t2.dbversion                                              "
        + "              from tab1 t1                                                   "
        + "        inner join s_tenant t2                                               "
        + "                on t1.id = t2.id                                             "
        + "        LEFT JOIN s_tenant t3                                                "
        + "                ON t1.parent_id = t3.id                                      "
        + "           ";

    /**
     * 表格查询使用
     */
    String commonGridSql = "   "
        + "    SELECT                               "
        + "    		t1.id,                          "
        + "    		t1.parent_id,                    "
        + "         (case                           "
        + "            when t1.parent_id is null then '根结点'  "
        + "            else t2.name                 "
        + "          end) as parent_name,           "
        + "    		t1.serial_no,                   "
        + "    		t1.`code`,                      "
        + "    		t1.`name`,                      "
        + "    		t1.simple_name,                 "
        + "    		t1.is_enable,                    "
        + "    		t1.enable_time,                 "
        + "    		t1.disable_time,                "
        + "    		t1.is_freeze,                    "
        + "    		t1.is_leaf,                      "
        + "    		t1.`level`,                     "
        + "    		t1.sort,                        "
        + "    		t1.descr,                       "
        + "    		t1.is_del,                       "
        + "    		t1.c_id,                        "
        + "    		t1.c_time,                      "
        + "    		t1.u_id,                        "
        + "    		t1.u_time,                      "
        + "    		t1.dbversion                    "
        + "    FROM                                  "
        + "    		s_tenant AS t1                  "
        + "    		LEFT JOIN s_tenant t2 on t1.parent_id = t2.id                  "
        + "  where true                                                     ";

    /**
     * 获取树的数据
     * @param id
     * @return
     */
    @Select(
        "       "
        + commonTreeSql
        + "  where (                                                             "
        + "          case                                                        "
        + "             when t1.id = #{p1} then true                             "
        + "             when t1.id <> #{p1} then t1.parent_id = #{p1}             "
        + "             else false                                               "
        + "         end                                                          "
        + "         or #{p1} is null                                             "
        + "        )                                                             "
        + "    and (t1.depth_name like CONCAT ('%',#{p2},'%') or #{p2} is null)                                                             "
        + "        ;"
    )
    List<STenantTreeVo> getTreeList(@Param("p1") Long id, @Param("p2") String name);

    /**
     * 获取树的数据，级联
     * @param id
     * @return
     */
    @Select(
              "       "
            + commonTreeSql
            + "  where (                                                             "
            + "          case                                                        "
            + "             when t1.id = #{p1} then true                             "
            + "             when t1.id <> #{p1} then t1.parent_id = #{p1}             "
            + "             else false                                               "
            + "         end                                                          "
            + "         or #{p1} is null                                             "
            + "        )                                                             "
            + "    and (t1.depth_name like CONCAT ('%',#{p2},'%') or #{p2} is null)                                                             "
            + "        ;"
    )
    List<STenantTreeVo> getCascaderList(@Param("p1") Long id, @Param("p2") String name);

    /**
     * 页面查询列表
     * 
     * @param page
     * @param searchCondition
     * @return
     */
    @Select(" <script> "
        + commonGridSql
        + "    and (t1.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "  </script>")
    IPage<STenantVo> selectPage(Page<STenantVo> page, @Param("p1") STenantVo searchCondition);


    /**
     * 没有分页，按id筛选条件
     * 
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from s_tenant t "
        + "  where t.id in "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item.id}  "
        + "        </foreach>"
        + "  </script>")
    List<STenantEntity> selectIdsIn(@Param("p1") List<STenantVo> searchCondition);

    /**
     * 按id查询
     * 
     * @param id
     * @return
     */
    @Select(" "
        + commonGridSql
        + "  and t1.id =  #{p1} "
        + "        ")
    STenantVo selectId(@Param("p1") Long id);

    /**
     * 按条件获取所有数据，没有分页
     * 
     * @param code
     * @return
     */
    @Select("    "
        + " select t.* "
        + "   from s_tenant t "
        + "  where true "
        + "    and t.code =  #{p1}"
        + "      ")
    List<STenantEntity> selectByCode(@Param("p1") String code);

    /**
     * 按条件获取所有数据，没有分页
     * 
     * @param name
     * @return
     */
    @Select("    "
        + " select t.* "
        + "   from s_tenant t "
        + "  where true "
        + "    and t.name =  #{p1}"
        + "      ")
    List<STenantEntity> selectByName(@Param("p1") String name);
}
