package com.perfect.core.config.mybatis.typehandlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.perfect.bean.vo.master.rbac.permission.operation.OperationFunctionInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: JsonHandler
 * @Description: mybatis处理JSON array类型
 * @Author: zxh
 * @date: 2020/4/13
 * @Version: 1.0
 */
@MappedTypes(value = {OperationFunctionInfoVo.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class OperationFunctionInfoVoTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private Class<T> clazz;

    public OperationFunctionInfoVoTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> jsonList, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,
            JSON.toJSONString(jsonList,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteClassName)
        );
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getJsonList(resultSet.getString(s));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getJsonList(resultSet.getString(i));
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getJsonList(callableStatement.getString(i));
    }

    private List<T> getJsonList(String content) {
        List<T> jsonResult = new ArrayList<>();

        if (StringUtils.isNotBlank(content)) {
            //            List<T> jsonList = JSON.parseObject(content, new com.alibaba.fastjson.TypeReference<List<T>>(){});
            //            List<T> x = JSON.parseArray(content, jsonResult.getClass());
            //            if (!NullUtil.isNull(jsonList)) {
            //                jsonResult.addAll(jsonList);
            //            }
            jsonResult = JSON.parseArray(content, clazz);

        }

        return jsonResult;
    }
}
