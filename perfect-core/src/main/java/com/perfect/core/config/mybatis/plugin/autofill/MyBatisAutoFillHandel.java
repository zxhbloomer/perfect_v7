package com.perfect.core.config.mybatis.plugin.autofill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.perfect.core.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author zxh
 */
@Slf4j
public class MyBatisAutoFillHandel implements MetaObjectHandler {

    /**
     * 新增的时候自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(" ....新增的时候自动填充 ....");
        this.setFieldValByNameMy("c_time", LocalDateTime.now(), metaObject);
        this.setFieldValByNameMy("u_time", LocalDateTime.now(), metaObject);
        this.setFieldValByNameMy("dbversion", 0, metaObject);

        this.setFieldValByNameMy("c_id", SecurityUtil.getStaff_id() < 0 ? null : SecurityUtil.getStaff_id(), metaObject);
        this.setFieldValByNameMy("u_id", SecurityUtil.getStaff_id() < 0 ? null : SecurityUtil.getStaff_id(), metaObject);
        // 默认未删除
        this.setFieldValByNameMy("is_del", false, metaObject);
        // 默认未启用 是否启用(1:true-已启用,0:false-已禁用)
        this.setFieldValByNameMy("is_enable", false, metaObject);
    }

    /**
     * 更新的时候自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(" ....更新的时候自动填充 ....");
        this.setFieldValByName("u_time", LocalDateTime.now(), metaObject);
        this.setFieldValByName("u_id", SecurityUtil.getStaff_id() < 0 ? null : SecurityUtil.getStaff_id(), metaObject);
    }

    private void setFieldValByNameMy(String fieldName, Object fieldVal, MetaObject metaObject){
        try {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
        } catch (Exception e) {
            log.debug("自动填充未找到fieldName：" +fieldName);
        }
    }
}
