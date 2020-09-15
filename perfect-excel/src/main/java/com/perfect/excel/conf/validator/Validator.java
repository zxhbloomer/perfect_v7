package com.perfect.excel.conf.validator;

import com.perfect.common.utils.string.StringUtil;

/**
 *
 * validator 类
 *
 * @author zxh
 * @date 2016/1/5
 */
public abstract class Validator {

    protected String errorMsg;

    protected String defaultMsg;

    /**
     * 抽象方法，验证
     * @param input
     * @return
     */
    public abstract boolean validate(String input);

    public String getErrorMsg() {
        if (StringUtil.isEmpty(errorMsg)) {
            return defaultMsg;
        }
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
