package com.perfect.core.mapper.master.rbac.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.entity.master.rbac.permission.MPermissionMenuEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 权限菜单信息 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2020-08-07
 */
@Repository
public interface MPermissionMenuMapper extends BaseMapper<MPermissionMenuEntity> {

    /**
     * 表复制，m_menu->m_permission_menu
     * @param entity
     * @return
     */
    @Insert("                                                            "
        + "  INSERT INTO m_permission_menu (                             "
        + "   	         permission_id,                                  "
        + "   	         menu_id,                                        "
        + "   	         is_enable,                                      "
        + "   	         is_default,                                     "
        + "   	         `code`,                                         "
        + "   	         `name`,                                         "
        + "   	         root_id,                                        "
        + "   	         parent_id,                                      "
        + "   	         son_count,                                      "
        + "   	         sort,                                           "
        + "   	         type,                                           "
        + "   	         visible,                                        "
        + "   	         perms,                                          "
        + "   	         page_id,                                        "
        + "   	         page_code,                                      "
        + "   	         parent_path,                                    "
        + "   	         path,                                           "
        + "   	         full_path,                                      "
        + "   	         default_open,                                      "
        + "   	         route_name,                                     "
        + "   	         meta_title,                                     "
        + "   	         meta_icon,                                      "
        + "   	         component,                                      "
        + "   	         affix,                                          "
        + "   	         descr,                                          "
        + "   	         tenant_id,                                      "
        + "   	         c_id,                                           "
        + "   	         c_time,                                         "
        + "   	         u_id,                                           "
        + "   	         u_time,                                         "
        + "              dbversion                                       "
        + "            )                                                 "
        + "       SELECT                                                 "
        + "              #{p1.permission_id,jdbcType=BIGINT},            "
        + "              t1.id,                                          "
        + "              #{p1.is_enable,jdbcType=BOOLEAN},               "
        + "              t1.is_default,                                  "
        + "              t1.`code`,                                      "
        + "              t1.`name`,                                      "
        + "              t1.root_id,                                     "
        + "              t1.parent_id,                                   "
        + "              t1.son_count,                                   "
        + "              t1.sort,                                        "
        + "              t1.type,                                        "
        + "              t1.visible,                                     "
        + "              t1.perms,                                       "
        + "              t1.page_id,                                     "
        + "              t1.page_code,                                   "
        + "              t1.parent_path,                                 "
        + "              t1.path,                                        "
        + "              t1.full_path,                                   "
        + "              t1.default_open,                                   "
        + "              t1.route_name,                                  "
        + "              t1.meta_title,                                  "
        + "              t1.meta_icon,                                   "
        + "              t1.component,                                   "
        + "              t1.affix,                                       "
        + "              t1.descr,                                       "
        + "              #{p1.tenant_id,jdbcType=BIGINT},                "
        + "              #{p1.c_id,jdbcType=BIGINT},                     "
        + "              #{p1.c_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.u_id,jdbcType=BIGINT},                     "
        + "              #{p1.u_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.dbversion,jdbcType=INTEGER}                "
        + "         FROM m_menu AS t1                                    "
        + "        where true                                            "
        + "          and t1.root_id = #{p1.menu_id,jdbcType=BIGINT}      "
        + "              ")
    int copyMMenu2MPermissionMenu(@Param("p1") MPermissionMenuEntity entity);

}
