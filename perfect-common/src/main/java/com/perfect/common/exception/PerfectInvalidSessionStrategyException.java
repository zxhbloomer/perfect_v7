package com.perfect.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author zxh
 * @date 2019/9/27
 */
public class PerfectInvalidSessionStrategyException extends AuthenticationException {

    private static final long serialVersionUID = -1468881768452376477L;

    public PerfectInvalidSessionStrategyException(String message) {
        super(message);
    }
}
