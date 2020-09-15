package com.perfect.core.mapper.master.rbac.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.entity.master.rbac.permission.MPermissionOperationEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 权限页面操作表 Mapper 接口
 * </p>
 *
 * @author zxh
 * @since 2020-08-07
 */
@Repository
public interface MPermissionOperationMapper extends BaseMapper<MPermissionOperationEntity> {

    /**
     * 表复制，s_pages_function->m_permission_operation
     * @param entity
     * @return
     */
    @Insert("                                                            "
        + "  INSERT INTO m_permission_operation (                        "
        + "   	  	     permission_id,                                  "
        + "   	  	     operation_id,                                   "
        + "   	  	     page_id,                                        "
        + "   	  	     is_enable,                                      "
        + "   	  	     type,                                           "
        + "   	  	     function_id,                                    "
        + "   	  	     sort,                                           "
        + "   	  	     perms,                                          "
        + "   	  	     descr,                                          "
        + "   	  	     tenant_id,                                      "
        + "   	  	     c_id,                                           "
        + "   	  	     c_time,                                         "
        + "   	  	     u_id,                                           "
        + "   	  	     u_time,                                         "
        + "   	  	     dbversion                                       "
        + "      )                                                       "
        + "       SELECT                                                 "
        + "              #{p1.permission_id,jdbcType=BIGINT},            "
        + "              t1.id,                                          "
        + "              t1.page_id,                                     "
        + "              #{p1.is_enable,jdbcType=BOOLEAN},               "
        + "              t1.type,                                        "
        + "              t1.function_id,                                 "
        + "              t1.sort,                                        "
        + "              t1.perms,                                       "
        + "              t1.descr,                                       "
        + "              #{p1.tenant_id,jdbcType=BIGINT},                "
        + "              #{p1.c_id,jdbcType=BIGINT},                     "
        + "              #{p1.c_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.u_id,jdbcType=BIGINT},                     "
        + "              #{p1.u_time,jdbcType=TIMESTAMP},                "
        + "              #{p1.dbversion,jdbcType=INTEGER}                "
        + "         FROM s_pages_function AS t1                          "
        + "        where true                                            "
        + "          and t1.page_id IN (                                 "
        + "                         SELECT subt1.page_id                 "
        + "                           FROM m_menu subt1                  "
        + "                          WHERE subt1.root_id = #{p2}         "
        + "                       )                                      "
        + "              ")
    int copyMPermissionOperation2MPermissionOperation(@Param("p1") MPermissionOperationEntity entity, @Param("p2") Long root_id);
}
