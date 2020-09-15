package com.perfect.common.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.perfect.common.properies.PerfectConfigProperies;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author zxh
 */
@Component
public class ExceptionUtil {

    private static boolean SIMPLE_MODEL;

//    @Value("${perfect.config.simple-model}")
//    public void setSIMPLE_MODEL(boolean SIMPLE_MODEL) {
//        this.SIMPLE_MODEL = SIMPLE_MODEL;
//    }

    private static PerfectConfigProperies perfectConfigProperies;
    @Autowired
    public void setProperties(PerfectConfigProperies perfectConfigProperies) {
        ExceptionUtil.perfectConfigProperies = perfectConfigProperies;
    }

    /**
     * 将异常日志转换为字符串
     * @param e
     * @return
     */
    public static String getException(Throwable e) {
        String rtn = "";
        if (perfectConfigProperies.isSimpleModel()){
            rtn = e.toString();
            return rtn;
        }
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            rtn = writer.toString();
            return rtn;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e1) {
            }
        }
    }

    /**
     * 将异常日志转换为字符串
     * @param e
     * @return
     */
    public static String getException(Exception e) {
        String rtn = "";
        if (perfectConfigProperies.isSimpleModel()){
            rtn = e.toString();
            return rtn;
        }
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            rtn = writer.toString();
            return rtn;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e1) {
            }
        }
    }
}
