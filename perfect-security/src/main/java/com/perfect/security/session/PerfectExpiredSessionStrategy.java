package com.perfect.security.session;

import com.perfect.bean.result.utils.v1.ResponseResultUtil;
import com.perfect.common.enums.ResultEnum;
import com.perfect.common.exception.CredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;

/**
 * 处理 session过期
 * 导致 session 过期的原因有：
 * 1. 并发登录控制
 * 2. 被踢出
 */
public class PerfectExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        ResponseResultUtil
            .responseWriteError(event.getRequest(),
                event.getResponse(),
                new CredentialException("登录已失效"),
                HttpStatus.UNAUTHORIZED.value(),
                ResultEnum.USER_LOGIN_TIME_OUT_ERROR,
                "登录已失效");
    }

}
