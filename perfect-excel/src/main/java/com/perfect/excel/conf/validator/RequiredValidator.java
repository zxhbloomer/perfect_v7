package com.perfect.excel.conf.validator;

import com.perfect.common.utils.string.StringUtil;

/**
 * Created by gordian on 2016/1/5.
 */
public class RequiredValidator extends Validator {

    public RequiredValidator() {
        defaultMsg = "不能为空";
    }

    @Override
    public boolean validate(String input) {
        return StringUtil.isNotEmpty(input);
    }
}
