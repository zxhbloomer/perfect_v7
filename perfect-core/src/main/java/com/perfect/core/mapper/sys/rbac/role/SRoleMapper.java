package com.perfect.core.mapper.sys.rbac.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.entity.sys.rbac.role.SRoleEntity;
import com.perfect.bean.vo.sys.rbac.role.SRoleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2019-07-11
 */
@Repository
public interface SRoleMapper extends BaseMapper<SRoleEntity> {

    /**
     * 页面查询列表
     * @param page
     * @param searchCondition
     * @return
     */
    @Select("   "
        + " select t.* "
        + "   from s_role t "
        + "  where (t.name        like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "    and (t.code        like CONCAT ('%',#{p1.code,jdbcType=VARCHAR},'%') or #{p1.code,jdbcType=VARCHAR} is null) "
        + "    and (t.simple_name like CONCAT ('%',#{p1.simple_name,jdbcType=VARCHAR},'%') or #{p1.simple_name,jdbcType=VARCHAR} is null) ")
    IPage<SRoleEntity> selectPage(Page page, @Param("p1") SRoleVo searchCondition );

    /**
     * 按条件获取所有数据，没有分页
     * @param searchCondition
     * @return
     */
    @Select("   "
        + " select t.* "
        + "   from s_role t "
        + "  where (t.name        like CONCAT ('%',#{p1.name,jdbcType=VARCHAR},'%') or #{p1.name,jdbcType=VARCHAR} is null) "
        + "    and (t.code        like CONCAT ('%',#{p1.code,jdbcType=VARCHAR},'%') or #{p1.code,jdbcType=VARCHAR} is null) "
        + "    and (t.simple_name like CONCAT ('%',#{p1.simple_name,jdbcType=VARCHAR},'%') or #{p1.simple_name,jdbcType=VARCHAR} is null) ")
    List<SRoleEntity> select(@Param("p1") SRoleVo searchCondition );

    /**
     * 没有分页，按id筛选条件
     * @param searchCondition
     * @return
     */
    @Select("<script>"
        + " select t.* "
        + "   from s_role t "
        + "  where t.id in "
        + "        <foreach collection='p1' item='item' index='index' open='(' separator=',' close=')'>"
        + "         #{item.id}  "
        + "        </foreach>"
        + "  </script>")
    List<SRoleEntity> selectIdsIn(@Param("p1") List<SRoleVo> searchCondition );
}
