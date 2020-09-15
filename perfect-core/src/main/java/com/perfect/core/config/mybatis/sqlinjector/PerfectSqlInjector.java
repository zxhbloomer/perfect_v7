package com.perfect.core.config.mybatis.sqlinjector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.perfect.core.config.mybatis.sqlinjector.methods.PerfectUpdateById;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义Sql注入
 */
public class PerfectSqlInjector extends DefaultSqlInjector {
    /**
     * 如果只需增加方法，保留MP自带方法
     * 可以super.getMethodList() 再add
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        //增加自定义方法
        methodList.add(new PerfectUpdateById());
        return methodList;
    }
}
