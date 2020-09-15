package com.perfect.security.handler;

import com.perfect.bean.result.utils.v1.ResponseResultUtil;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.enums.ResultEnum;
import com.perfect.common.exception.CredentialException;
import com.perfect.common.utils.CommonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理
 * @author Administrator
 */
public class PerfectAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        if (CommonUtil.isAjaxRequest(request)) {
            response.setContentType(PerfectConstant.JSON_UTF8);
            ResponseResultUtil.responseWriteError(request,
                response,
                new CredentialException("没有该权限！"),
                HttpStatus.UNAUTHORIZED.value(),
                ResultEnum.USER_NO_PERMISSION_ERROR,
                "没有该权限！");
        } else {
            redirectStrategy.sendRedirect(request, response, PerfectConstant.FEBS_ACCESS_DENY_URL);
        }
    }
}
