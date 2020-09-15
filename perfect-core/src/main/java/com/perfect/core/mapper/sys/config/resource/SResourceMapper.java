package com.perfect.core.mapper.sys.config.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.sys.config.resource.SResourceEntity;
import com.perfect.bean.vo.sys.config.resource.SResourceVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-08-16
 */
@Repository
public interface SResourceMapper extends BaseMapper<SResourceEntity> {

    /**
     * 页面查询列表
     * @param page
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from s_resource t "
        + "  where true "
        + "    and (t.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "   <if test='p1.types != null and p1.types.length!=0' >"
        + "    and t.type in "
        + "        <foreach collection='p1.types' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item}  "
        + "        </foreach>"
        + "   </if>"
        + "    and (t.is_del =#{p1.is_del,jdbcType=VARCHAR} or #{p1.is_del,jdbcType=VARCHAR} is null) "
        + "  </script>") IPage<SResourceEntity> selectPage(Page page, @Param("p1") SResourceVo searchCondition );

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from s_resource t "
        + "  where true "
        + "    and (t.name like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "   <if test='p1.code.length!=0' > "
        + "    and t.type in "
        + "        <foreach collection='p1.code' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item}  "
        + "        </foreach> "
        + "   </if> "
        + "    and (t.is_del =#{p1.is_del,jdbcType=VARCHAR} or #{p1.is_del,jdbcType=VARCHAR} is null) "
        + "  </script>")
    List<SResourceEntity> select(@Param("p1") SResourceVo searchCondition );

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from s_resource t "
        + "  where t.id in "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item.id}  "
        + "        </foreach>"
        + "  </script>")
    List<SResourceEntity> selectIdsIn(@Param("p1") List<SResourceVo> searchCondition );

    /**
     * 没有分页，按id筛选条件
     * @param name
     * @return
     */
    @Select("    "
        + " select t.* "
        + "   from s_resource t "
        + "  where t.id in "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item.id}  "
        + "        </foreach>"
        + "      ")
    SResourceEntity selectName(@Param("p1") String name );
}
