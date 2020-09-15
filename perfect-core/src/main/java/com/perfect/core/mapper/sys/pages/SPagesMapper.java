package com.perfect.core.mapper.sys.pages;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.sys.pages.SPagesEntity;
import com.perfect.bean.vo.sys.pages.SPagesVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 页面表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2020-06-05
 */
@Repository
public interface SPagesMapper extends BaseMapper<SPagesEntity> {

    /**
     * 页面查询列表
     * @param page
     * @param searchCondition
     * @return
     */
    @Select("                                                                                                            "
        + "  SELECT                                                                                                      "
        + "         t.*,                                                                                                 "
        + "         c_staff.name as c_name,                                                                              "
        + "         u_staff.name as u_name                                                                               "
        + "    FROM                                                                                                      "
        + "  	    s_pages t                                                                                            "
        + "  LEFT JOIN m_staff c_staff ON t.c_id = c_staff.id                                                            "
        + "  LEFT JOIN m_staff u_staff ON t.u_id = u_staff.id                                                            "
        + "  where true                                                                                                  "
        + "    and (t.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null)                      "
        + "    and (t.meta_title like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null)                "
        + "    and (t.component like CONCAT ('%',#{p1.component,jdbcType=VARCHAR},'%') or #{p1.component,jdbcType=VARCHAR} is null)       "
        + "    and (t.code like CONCAT ('%',#{p1.code,jdbcType=VARCHAR},'%') or #{p1.code,jdbcType=VARCHAR} is null)                      "
        + "                                                                                                              ")
    IPage<SPagesVo> selectPage(Page page, @Param("p1") SPagesVo searchCondition);

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("                                                                                                            "
        + "  SELECT                                                                                                      "
        + "         t.*,                                                                                                 "
        + "         c_staff.name as c_name,                                                                              "
        + "         u_staff.name as u_name                                                                               "
        + "  FROM                                                                                                        "
        + "  	s_pages t                                                                                                "
        + "  LEFT JOIN m_staff c_staff ON t.c_id = c_staff.id                                                            "
        + "  LEFT JOIN m_staff u_staff ON t.u_id = u_staff.id                                                            "
        + "  where true                                                                                                  "
        + "    and (t.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null)     "
        + "    and (t.meta_title  like CONCAT ('%',#{p1.meta_title,jdbcType=VARCHAR},'%') or #{p1.meta_title,jdbcType=VARCHAR} is null) "
        + "    and (t.code  like CONCAT ('%',#{p1.code,jdbcType=VARCHAR},'%') or #{p1.code,jdbcType=VARCHAR} is null)    "
        + "                                                                                                              ")
    List<SPagesVo> select(@Param("p1") SPagesVo searchCondition);

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("   <script>   "
        + "  SELECT                                                                                        "
        + "       *                                                                                        "
        + "  FROM                                                                                          "
        + "  	s_pages t                                                                                  "
        + "  where t.id in                                                                                 "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>    "
        + "         #{item.id}  "
        + "        </foreach>    "
        + "  </script>    ")
    List<SPagesVo> selectIdsIn(@Param("p1") List<SPagesVo> searchCondition);


    /**
     * 按条件获取所有数据，没有分页
     * @param id
     * @return
     */
    @Select("    "
        + "  SELECT                                                                                                      "
        + "         t.*,                                                                                                 "
        + "         c_staff.name as c_name,                                                                              "
        + "         u_staff.name as u_name                                                                               "
        + "    FROM                                                                                                      "
        + "  	    s_pages t                                                                                            "
        + "  LEFT JOIN m_staff c_staff ON t.c_id = c_staff.id                                                            "
        + "  LEFT JOIN m_staff u_staff ON t.u_id = u_staff.id                                                            "
        + "  where t.id =  #{p1}                                                                                         "
        + "      ")
    SPagesVo selectId(@Param("p1") Long id);

}
