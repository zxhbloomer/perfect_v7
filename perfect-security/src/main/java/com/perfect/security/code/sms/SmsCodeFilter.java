package com.perfect.security.code.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.perfect.bean.vo.SSmsCodeVo;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.exception.ValidateCodeException;
import com.perfect.common.utils.CommonUtil;
import com.perfect.security.code.ValidateCode;
import com.perfect.security.properties.PerfectSecurityProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 短信验证码
 * @author zhangxh
 */
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private Set<String> url = new HashSet<>();
    @Autowired
    private PerfectSecurityProperties perfectSecurityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private SessionRegistry sessionRegistry;

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(perfectSecurityProperties.getCode().getSms().getUrl(), ",");
        url.addAll(Arrays.asList(configUrls));
        url.add(perfectSecurityProperties.getCode().getSms().getLoginProcessingUrl());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 如果是develop模式，则不需要考虑验证码
        if (perfectSecurityProperties.getDevelopModel()){
            filterChain.doFilter(CommonUtil.convertJsonType2FormData(httpServletRequest), httpServletResponse);
            return;
        }

        boolean match = false;
        for (String u : url) {
//            if (pathMatcher.match(u, httpServletRequest.getRequestURI())) {
            if (pathMatcher.match(u, httpServletRequest.getServletPath())) {
                match = true;
            }
        }
        if (match) {
            try {
                validateSmsCode(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(CommonUtil.convertJsonType2FormData(httpServletRequest), httpServletResponse);
    }

    private void validateSmsCode(ServletWebRequest servletWebRequest) throws IOException {
//        String smsCodeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");
//        String mobile = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mobile");

        String jsonBody = IOUtils.toString( servletWebRequest.getRequest().getInputStream(), "utf-8");
        SSmsCodeVo vo = JSON.parseObject(jsonBody, SSmsCodeVo.class);
        String smsCodeInRequest = vo.getSms_code();
        String mobile = vo.getMobile();

        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(servletWebRequest, PerfectConstant.SESSION_KEY_SMS_CODE + mobile);

        if (StringUtils.isBlank(smsCodeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在，请重新发送！");
        }
        if (codeInSession.isExpire()) {
            sessionStrategy.removeAttribute(servletWebRequest, PerfectConstant.SESSION_KEY_SMS_CODE + mobile);
            throw new ValidateCodeException("验证码已过期，请重新发送！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), smsCodeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        sessionStrategy.removeAttribute(servletWebRequest, PerfectConstant.SESSION_KEY_SMS_CODE + mobile);

    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public void setPerfectSecurityProperties(PerfectSecurityProperties perfectSecurityProperties) {
        this.perfectSecurityProperties = perfectSecurityProperties;
    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}