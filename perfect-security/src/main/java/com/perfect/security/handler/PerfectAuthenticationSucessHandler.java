package com.perfect.security.handler;

import com.perfect.bean.result.utils.v1.ResponseResultUtil;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.enums.ResultEnum;
import com.perfect.common.exception.CredentialException;
import com.perfect.common.utils.redis.RedisUtil;
import com.perfect.core.utils.security.SecurityUtil;
import com.perfect.framework.base.controller.v1.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理器
 */
public class PerfectAuthenticationSucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private BaseController baseController;

    @Autowired
    private RedisUtil redisUtil;

    private SessionRegistry sessionRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, CredentialException {
        response.setContentType(PerfectConstant.JSON_UTF8);
        Map<String,String> token = new HashMap<String,String>();
        token.put("token",getSessionId(authentication, request.getSession().getId()));

        // session
        try {
            baseController.resetUserSession(SecurityUtil.getLoginUser_id(), PerfectConstant.LOGINUSER_OR_STAFF_ID.LOGIN_USER_ID);
            ResponseResultUtil.responseWriteOK(token, response);
        } catch (Exception e) {
            ResponseResultUtil.responseWriteError(request,
                response,
                new CredentialException("获取权限发生错误，请联系管理员！"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ResultEnum.SYSTEM_ERROR,
                "获取权限发生错误，请联系管理员！");
        }
    }
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * 获取sessionid
     * @param authentication
     * @param dflt
     * @return
     */
    private String getSessionId(Authentication authentication, String dflt) {
        if (authentication != null && authentication.isAuthenticated() && authentication.getDetails() instanceof WebAuthenticationDetails) {
            String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
            return sessionId == null ? dflt : sessionId;
        } else {
            // anonymous
            return dflt;
        }
    }


}
