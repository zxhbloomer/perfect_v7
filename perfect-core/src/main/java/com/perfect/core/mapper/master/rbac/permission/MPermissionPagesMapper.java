package com.perfect.core.mapper.master.rbac.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.entity.master.rbac.permission.MPermissionPagesEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 权限页面表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2020-08-07
 */
@Repository
public interface MPermissionPagesMapper extends BaseMapper<MPermissionPagesEntity> {

    /**
     * 表复制，s_pages->m_permission_pages
     * @param entity
     * @return
     */
    @Insert("                                                            "
        + "  INSERT INTO m_permission_pages (                            "
        + "   	         permission_id,                                  "
        + "   	         page_id,                                        "
        + "   	         `code`,                                         "
        + "   	         `name`,                                         "
        + "   	         component,                                      "
        + "   	         perms,                                          "
        + "   	         meta_title,                                     "
        + "   	         meta_icon,                                      "
        + "   	         descr,                                          "
        + "   	         tenant_id,                                      "
        + "   	         c_id,                                           "
        + "   	         c_time,                                         "
        + "   	         u_id,                                           "
        + "   	         u_time,                                         "
        + "   	         dbversion                                       "
        + "      )                                                       "
        + "       SELECT                                                 "
        + "              #{p1.permission_id,jdbcType=BIGINT},            "
        + "              t1.id,                                          "
        + "              t1.`code`,                                      "
        + "              t1.`name`,                                      "
        + "              t1.component,                                   "
        + "              t1.perms,                                       "
        + "              t1.meta_title,                                  "
        + "              t1.meta_icon,                                   "
        + "              t1.descr,                                       "
        + "              #{p1.tenant_id,jdbcType=BIGINT},                "
        + "              #{p1.c_id,jdbcType=BIGINT},                     "
        + "              #{p1.c_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.u_id,jdbcType=BIGINT},                     "
        + "              #{p1.u_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.dbversion,jdbcType=INTEGER}                "
        + "         FROM s_pages AS t1                                   "
        + "        where true                                            "
        + "          and t1.id IN (                                      "
        + "                         SELECT subt1.page_id                 "
        + "                           FROM m_menu subt1                  "
        + "                          WHERE subt1.root_id = #{p2}         "
        + "                       )                                      "
        + "              ")
    int copySPages2MPermissionPages(@Param("p1") MPermissionPagesEntity entity, @Param("p2") Long root_id);
}
