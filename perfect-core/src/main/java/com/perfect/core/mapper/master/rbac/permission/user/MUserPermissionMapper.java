package com.perfect.core.mapper.master.rbac.permission.user;

import com.perfect.bean.bo.session.user.rbac.PermissionMenuBo;
import com.perfect.bean.bo.session.user.rbac.PermissionMenuMetaBo;
import com.perfect.bean.bo.session.user.rbac.PermissionOperationBo;
import com.perfect.common.constant.PerfectDictConstant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: 获取用户权限
 * @Description: TODO
 * @Author: zxh
 * @date: 2020/8/26
 * @Version: 1.0
 */
@Repository
public interface MUserPermissionMapper {


    @Select("                                                                                    "
        + "          select t1.*,                                                                "
        + "                 right(t1.code,2) nav_code,                                           "
        + "                 CONCAT(@rownum := @rownum +1,'') AS `index`                          "
        + "            from m_menu t1,                                                           "
        + "                 (SELECT @rownum := 0) t2                                             "
        + "           where t1.type = '"+ PerfectDictConstant.DICT_SYS_MENU_TYPE_TOPNAV +"'      "
        + "             and (t1.tenant_id = #{p1} or #{p1} is null)                              "
        + "        order by t1.code                                                              "
        + "                                                                                      ")
    List<PermissionMenuBo> getPermissionMenuTopNav(@Param("p1")Long tenant_id);

    @Select("                                                                                                            "
        + "                    WITH recursive tab1 AS (                                                              "
        + "                        SELECT                                                                            "
        + "                               t0.id AS menu_id,                                                          "
        + "                               t0.tenant_id,                                                              "
        + "                               t0.parent_id,                                                              "
        + "                               1 LEVEL,                                                                   "
        + "                               t0.NAME,                                                                   "
        + "                               t0.NAME AS depth_name,                                                     "
        + "                               cast(t0.id AS CHAR ( 50 )) depth_id                                        "
        + "                          FROM m_menu t0                                                                  "
        + "                         WHERE t0.parent_id IS NULL                                                       "
        + "                        UNION ALL                                                                         "
        + "                        SELECT                                                                            "
        + "                               t2.id AS menu_id,                                                          "
        + "                               t2.tenant_id,                                                              "
        + "                               t2.parent_id,                                                              "
        + "                               t1.LEVEL + 1 AS LEVEL,                                                     "
        + "                               t2.NAME,                                                                   "
        + "                               CONCAT( t1.depth_name, '>', t2.NAME ) depth_name,                          "
        + "                               CONCAT(                                                                    "
        + "                                   cast(                                                                  "
        + "                                   t1.depth_id AS CHAR ( 50 )),                                           "
        + "                                   ',',                                                                   "
        + "                                   cast(                                                                  "
        + "                                   t2.id AS CHAR ( 50 ))) depth_id                                        "
        + "                          FROM m_menu t2,                                                                 "
        + "                               tab1 t1                                                                    "
        + "                         WHERE t2.parent_id = t1.menu_id                                                  "
        + "                        )                                                                                 "
        + "                SELECT                                                                                    "
        + "                       t2.id,                                                                             "
        + "                       t2.is_default,                                                                     "
        + "                       t1.menu_id,                                                                        "
        + "                       t1.menu_id AS `value`,                                                             "
        + "                       t1.parent_id,                                                                      "
        + "                       t2.root_id,                                                                        "
        + "                       t1.LEVEL,                                                                          "
        + "                       t2.type,                                                                           "
        + "                       t3.path as redirect,                                                               "
        + "                       t2.visible,                                                                        "
        + "                       t2.page_id,                                                                        "
        + "                       t2.page_code,                                                                      "
        + "                       t2.path,                                                                           "
        + "                       t2.full_path,                                                                      "
        + "                       t2.default_open,                                                                   "
        + "                       t2.route_name,                                                                     "
        + "                       t2.meta_title,                                                                     "
        + "                       t2.meta_icon,                                                                      "
        + "                	   case when t2.type = '"+ PerfectDictConstant.DICT_SYS_MENU_TYPE_TOPNAV +"' then 'Layout'   "
        + "                	   else t2.component                                                                         "
        + "                	   end as component,                                                                         "
        + "                       t2.affix,                                                                          "
        + "                       t2.tenant_id,                                                                      "
        + " JSON_OBJECT('title',t2.meta_title,'icon',t2.meta_icon,'affix',t2.affix,'name',t2.meta_title) as meta     "
        + "                  FROM tab1 t1                                                                            "
        + "            INNER JOIN m_menu t2 ON t1.menu_id = t2.id                                                    "
        + "                   AND t1.tenant_id = t2.tenant_id                                                        "
        /** 每个头部导航栏，都需要有默认打开的菜单 */
        + "             left join (                                                                                  "
        + "                          SELECT left(subt1.code , 8) top_nav_code,                                       "
        + "                                 min(subt1.code) page_code,                                               "
        + "                                 subt1.path,subt1.tenant_id                                               "
        + "                            FROM m_menu subt1                                                             "
        + "    where length(subt1.code) > 8 and subt1.type = '"+ PerfectDictConstant.DICT_SYS_MENU_TYPE_PAGE +"'     "
        + "                        group by left(subt1.code , 8)                                                     "
        + "             ) t3 on  t2.code = t3.top_nav_code                                                           "
        + "                  and t2.tenant_id = t3.tenant_id                                                         "
        + "                 WHERE TRUE                                                                               "
        + "                   AND ( t2.tenant_id = #{p1} or #{p1} is null )                                          "
        + "              ORDER BY t2.CODE;                                                                           "
        + "                ")
    @Results({
        @Result(property = "meta", column = "meta", javaType = PermissionMenuMetaBo.class, typeHandler = com.perfect.core.config.mybatis.typehandlers.PermissionMenuMetaBoTypeHandler.class),
    })
    List<PermissionMenuBo> getSystemMenu(@Param("p1")Long tenant_id);

    /**
     * 获取菜单权限
     * @param staff_id
     * @param tenant_id
     * @return
     */
    @Select("                                                                                                            "
        + "             SELECT                                                                                           "
        + "                	   t2.id,                                                                                    "
        + "                	   t2.is_enable,                                                                             "
        + "                	   t2.is_default,                                                                            "
        + "                	   t1.menu_id,                                                                               "
        + "                	   t1.menu_id AS `value`,                                                                    "
        + "                	   t1.parent_id,                                                                             "
        + "                	   t2.root_id,                                                                               "
        + "                	   t1.LEVEL,                                                                                 "
        + "                	   t2.type,                                                                                  "
        + "                	   t2.visible,                                                                               "
        + "                	   t2.page_id,                                                                               "
        + "                	   t2.page_code,                                                                             "
        + "                	   t2.full_path,                                                                             "
        + "                	   t2.default_open,                                                                             "
        + "                	   t2.route_name,                                                                            "
        + "                	   t2.meta_title,                                                                            "
        + "                	   t2.meta_icon,                                                                             "
        + "                	   t2.component,                                                                             "
        + "                	   t2.affix,                                                                                 "
        + "    JSON_OBJECT('title',t2.meta_title,'icon',t2.meta_icon,'affix',t2.affix,'name',t2.meta_title) as meta,     "
        + "                	   t2.tenant_id                                                                              "
        + "               FROM                                                                                           "
        + "                	   v_permission_tree t1                                                                      "
        + "         INNER JOIN m_permission_menu t2 ON t1.menu_id = t2.menu_id                                           "
        + "                AND t1.tenant_id = t2.tenant_id                                                               "
        + "                AND t1.permission_id = t2.permission_id                                                       "
        + "         INNER JOIN m_permission t3 on t3.id = t2.permission_id                                               "
        + "         INNER JOIN m_staff t4 on t3.serial_type = '" + PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE + "' "
        + "         	   and t3.serial_id = t4.dept_id                                                                 "
        + "         	   and t4.id = #{p1}                                                                             "
        + "                and t3.`status` = true                                                                        "
        + "              WHERE TRUE                                                                                      "
        + "                AND ( t2.tenant_id = #{p2} or #{p2} is null )                                                 "
        + "                AND ( t2.is_enable = true )                                                                   "
        + "           ORDER BY t2.CODE                                                                                   "
        + "                ")
    @Results({
        @Result(property = "meta", column = "meta", javaType = PermissionMenuMetaBo.class, typeHandler = com.perfect.core.config.mybatis.typehandlers.PermissionMenuMetaBoTypeHandler.class),
    })
    List<PermissionMenuBo> getPermissionMenu(@Param("p1") Long staff_id,@Param("p2")Long tenant_id);


    @Select("                                     "
        + "       SELECT t3.*                                                               "
        + "         FROM m_staff t1                                                         "
        + "   INNER JOIN m_permission t2 ON t2.serial_type = '" + PerfectDictConstant.DICT_ORG_SETTING_TYPE_DEPT_SERIAL_TYPE + "' "
        + "       	 AND t2.serial_id = t1.dept_id                                          "
        + "       	 AND t2.`status` = TRUE                                                 "
        + "   INNER JOIN m_permission_pages t3 on t3.permission_id = t2.id                  "
        + "        WHERE t1.id = #{p1}                                                      "
        + "          and ( t1.tenant_id = #{p2} or #{p2} is null )                          "
        + "                ")
    List<PermissionOperationBo> getPermissionOperation(@Param("p1") Long staff_id,@Param("p2")Long tenant_id);
}
