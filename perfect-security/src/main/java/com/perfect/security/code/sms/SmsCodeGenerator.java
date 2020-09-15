package com.perfect.security.code.sms;


import com.perfect.security.code.ValidateCode;
import com.perfect.security.code.ValidateCodeGenerator;
import com.perfect.security.properties.PerfectSecurityProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private PerfectSecurityProperties perfectSecurityProperties;

    @Override
    public ValidateCode createCode() {
        String code = RandomStringUtils.randomNumeric(perfectSecurityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, perfectSecurityProperties.getCode().getSms().getExpireIn());
    }
}
