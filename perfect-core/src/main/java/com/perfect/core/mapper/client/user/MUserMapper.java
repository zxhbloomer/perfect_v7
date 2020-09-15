package com.perfect.core.mapper.client.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.perfect.bean.entity.master.user.MUserEntity;
import com.perfect.bean.vo.master.user.MUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  用户表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-06-24
 */
@Repository
public interface MUserMapper extends BaseMapper<MUserEntity> {

    /**
     *
     * @param p1
     * @return
     */
    @Select( "                                                 "
        + "    select t.*                                      "
        + "      from m_user t                                 "
        + "     where t.login_name = #{p1}                     "
//        + "       and t.is_enable = true                       "
//        + "       and t.is_del = false                         "
        + "                                                    ")
    MUserEntity getDataByName(@Param("p1") String p1);


    /**
     * 页面查询列表
     * @return
     */
    @Select("                                                                                                                    "
        + "      SELECT                                                                                                          "
        + "              t1.* ,                                                                                                  "
        + "            	 t2.label as type_text                                                                                   "
        + "        FROM                                                                                                          "
        + "              m_user t1                                                                                               "
        + "   left join  v_dict_info t2 on t2.code = 'usr_login_type' and t1.type = t2.dict_value                                "
        + "       where  true                                                                                                    "
        + "         and  (t1.id = #{p1})                                                                                         "
        + "         and (t1.tenant_id  = #{p2} or #{p2} is null)                                                                "
        + "                                                                                                                      ")
    MUserVo selectByid(@Param("p1") Long id, @Param("p2")Long tenant_id);

    /**
     * 按条件获取所有数据，没有分页
     */
    @Select("                                                                                                    "
        + " select t.*                                                                                           "
        + "   from m_user t                                                                                      "
        + "  where true                                                                                          "
        + "    and t.login_name =  #{p1}                                                                         "
        + "    and (t.id  =  #{p2} or #{p2} is null)                                                             "
        + "    and (t.id  <> #{p3} or #{p3} is null)                                                             "
        + "    and t.is_del =  0                                                                                 "
        + "    and (t.tenant_id  = #{p4} or #{p4} is null)                                                      "
        + "                                                                                                      ")
    List<MUserEntity> selectLoginName(@Param("p1") String login_name, @Param("p2") Long equal_id, @Param("p3") Long not_equal_id, @Param("p4")Long tenant_id);
}
