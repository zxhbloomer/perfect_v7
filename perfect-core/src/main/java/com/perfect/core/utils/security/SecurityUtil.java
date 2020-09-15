package com.perfect.core.utils.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.perfect.bean.bo.user.login.MUserBo;
import com.perfect.bean.entity.master.user.MUserEntity;

/**
 * 安全类工具类
 * @author Administrator
 */
public class SecurityUtil {

    /**
     * 获取login的Authentication
     * @return
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取 MUserEntity
     * @return
     */
    public static MUserEntity getLoginUserEntity(){
        return ((MUserBo) SecurityUtil.getAuthentication().getPrincipal()).getMUserEntity();
    }

    /**
     * 获取login的userid
     * @return
     */
    public static long getLoginUser_id(){
        if(SecurityUtil.getAuthentication() == null){
            return -1;
        } else {
            return SecurityUtil.getLoginUserEntity().getId();
        }
    }

    /**
     * 获取login的userid
     * @return
     */
    public static long getStaff_id(){
        if(SecurityUtil.getAuthentication() == null){
            return -1;
        } else {
            return SecurityUtil.getLoginUserEntity().getStaff_id();
        }
    }

    /**
     * 获取Principal
     * @return
     */
    public static Object getPrincipal(){
        return SecurityUtil.getAuthentication().getPrincipal();
    }
}