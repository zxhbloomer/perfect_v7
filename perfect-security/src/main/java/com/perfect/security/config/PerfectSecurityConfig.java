package com.perfect.security.config;

import com.perfect.common.constant.PerfectConstant;
import com.perfect.core.service.client.user.IMUserService;
import com.perfect.security.code.ValidateCodeGenerator;
import com.perfect.security.code.img.ImageCodeFilter;
import com.perfect.security.code.img.ImageCodeGenerator;
import com.perfect.security.code.sms.DefaultSmsSender;
import com.perfect.security.code.sms.SmsCodeFilter;
import com.perfect.security.code.sms.SmsCodeSender;
import com.perfect.security.cors.CorsFilter;
import com.perfect.security.handler.PerfectAuthenticationAccessDeniedHandler;
import com.perfect.security.handler.PerfectAuthenticationFailureHandler;
import com.perfect.security.handler.PerfectAuthenticationSucessHandler;
import com.perfect.security.handler.PerfectLogoutHandler;
import com.perfect.security.properties.PerfectSecurityProperties;
import com.perfect.security.session.PerfectExpiredSessionStrategy;
import com.perfect.security.session.PerfectInvalidSessionStrategy;
import com.perfect.security.xss.XssFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * security ????????????
 * @author zxh
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PerfectSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * ?????????????????????
     */
    @Autowired
    private PerfectAuthenticationSucessHandler perfectAuthenticationSucessHandler;

    @Autowired
    private IMUserService userDetailService;

    /**
     * ?????????????????????
     */
    @Autowired
    private PerfectAuthenticationFailureHandler perfectAuthenticationFailureHandler;

    @Autowired
    private PerfectSecurityProperties perfectSecurityProperties;

    @Autowired
    private PerfectSmsCodeAuthenticationSecurityConfig perfectSmsCodeAuthenticationSecurityConfig;

    @Qualifier("db1")
    @Autowired
    private DataSource dataSource;

    /**
     * ????????????
     * @return
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }


    /**
     * spring security??????????????????????????????
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    /**
//     * ?????????????????????
//     * @param auth
//     * @throws Exception
//     */
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("admin").password("123456").roles("ADMIN");
//    }

    /**
     * ?????? rememberMe ??????????????????
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] anonResourcesUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(
            perfectSecurityProperties.getAnonResourcesUrl(),",");

        ImageCodeFilter imageCodeFilter = new ImageCodeFilter();
        imageCodeFilter.setAuthenticationFailureHandler(perfectAuthenticationFailureHandler);
        imageCodeFilter.setPerfectSecurityProperties(perfectSecurityProperties);
        imageCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(perfectAuthenticationFailureHandler);
        smsCodeFilter.setPerfectSecurityProperties(perfectSecurityProperties);
        smsCodeFilter.setSessionRegistry(sessionRegistry());
        smsCodeFilter.afterPropertiesSet();

        http.exceptionHandling()
            /** ????????????????????? */
            .accessDeniedHandler(accessDeniedHandler())
            .and()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                /** ????????????????????? */
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                /** ????????????????????????????????? */
                .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                /** ???????????? */
                .formLogin()
                /** ??????????????? URL */
                .loginPage(perfectSecurityProperties.getLoginUrl())
                .usernameParameter("username").passwordParameter("password")
                /** ?????????????????? URL */
                .loginProcessingUrl(perfectSecurityProperties.getCode().getImage().getLoginProcessingUrl())
                /** ?????????????????? */
                .successHandler(perfectAuthenticationSucessHandler)
                /** ?????????????????? */
                .failureHandler(perfectAuthenticationFailureHandler)
            .and()
                /** ????????????????????? */
                .rememberMe()
                /** ?????? token ??????????????? */
                .tokenRepository(persistentTokenRepository())
                /** rememberMe ???????????????????????? */
                .tokenValiditySeconds(perfectSecurityProperties.getRememberMeTimeout())
                /** ???????????????????????? */
                .userDetailsService(userDetailService)
            .and()
                /** ?????? session????????? */
                .sessionManagement()
                /** ?????? session?????? */
                .invalidSessionStrategy(invalidSessionStrategy())
                /** ???????????????????????? */
                .maximumSessions(perfectSecurityProperties.getMAX_SESSIONS())
                /** ??????????????????????????? */
                .expiredSessionStrategy(new PerfectExpiredSessionStrategy())
                /** ?????? session???????????? */
                .sessionRegistry(sessionRegistry())
            .and()
            .and()
                /** ???????????? */
                .logout()
                /** ????????????????????? */
                .addLogoutHandler(logoutHandler())
                /** ???????????? url */
                //                .logoutUrl(perfectSecurityProperties.getLogoutUrl())
                /** ?????????????????? */
                //                .logoutSuccessUrl("/")
                /** ?????? JSESSIONID */
                .deleteCookies("JSESSIONID")
            .and()
                /** ???????????? */
                .authorizeRequests()
                /** ??????????????????????????? */
                .antMatchers(anonResourcesUrl).permitAll()
                .antMatchers(
                    /** ???????????? */
                    perfectSecurityProperties.getLoginUrl(),
                    /** ???????????? url */
                    PerfectConstant.FEBS_REGIST_URL,
                    /** ??????????????????????????? */
                    perfectSecurityProperties.getCode().getImage().getCreateUrl(),
                    /** ??????????????????????????? */
                    perfectSecurityProperties.getCode().getSms().getCreateUrl()
                )
                /** ????????????????????? */
                .permitAll()
                /** ???????????? *//** ??????????????? */
                .anyRequest().authenticated()
            .and()
                .csrf().disable()
                /** ????????????????????????????????? */
                .apply(perfectSmsCodeAuthenticationSecurityConfig)
        ;
    }


    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setPerfectSecurityProperties(perfectSecurityProperties);
        return imageCodeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsSender();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * ?????? javaconfig ?????????????????????????????? sessionRegistry
     * @return
     */
    @Bean
    public PerfectAuthenticationSucessHandler perfectAuthenticationSucessHandler() {
        PerfectAuthenticationSucessHandler authenticationSucessHandler = new PerfectAuthenticationSucessHandler();
        authenticationSucessHandler.setSessionRegistry(sessionRegistry());
        return authenticationSucessHandler;
    }

    /**
     * ?????????????????????
     * @return
     */
    @Bean
    public LogoutHandler logoutHandler(){
        PerfectLogoutHandler perfectLogoutHandler = new PerfectLogoutHandler();
        perfectLogoutHandler.setSessionRegistry(sessionRegistry());
        return perfectLogoutHandler;
    }

    @Bean
    public InvalidSessionStrategy invalidSessionStrategy(){
        PerfectInvalidSessionStrategy perfectInvalidSessionStrategy = new PerfectInvalidSessionStrategy();
        perfectInvalidSessionStrategy.setPerfectSecurityProperties(perfectSecurityProperties);
        return perfectInvalidSessionStrategy;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new PerfectAuthenticationAccessDeniedHandler();
    }

    /**
     * XssFilter Bean
     */
    @Bean
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        String[] anonResourcesUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(
            perfectSecurityProperties.getAnonResourcesUrl(),",");
        webSecurity.ignoring()
            .antMatchers(anonResourcesUrl);
    }

}
