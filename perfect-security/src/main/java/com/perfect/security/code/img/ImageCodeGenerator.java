package com.perfect.security.code.img;

import com.google.code.kaptcha.Producer;
import com.perfect.security.code.ValidateCodeGenerator;
import com.perfect.security.properties.PerfectSecurityProperties;

import javax.annotation.Resource;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageCodeGenerator implements ValidateCodeGenerator {

    private PerfectSecurityProperties perfectSecurityProperties;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Override
    public ImageCode createCode() {
        // 验证码有效时间
        int expireIn = perfectSecurityProperties.getCode().getImage().getExpireIn();

        String capStr = null;
        String code = null;
        BufferedImage bi = null;

        if ("math".equals(perfectSecurityProperties.getCode().getImage().getCaptchaType())) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            bi = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(perfectSecurityProperties.getCode().getImage().getCaptchaType())) {
            capStr = code = captchaProducer.createText();
            bi = captchaProducer.createImage(capStr);
        }

        return new ImageCode(bi, code, expireIn);
    }

    public void setPerfectSecurityProperties(PerfectSecurityProperties perfectSecurityProperties) {
        this.perfectSecurityProperties = perfectSecurityProperties;
    }
}
