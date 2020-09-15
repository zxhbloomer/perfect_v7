package com.perfect.bean.bo.log.operate;

import com.perfect.common.enums.OperationEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: CustomOperateBo
 * @Description: 自定义operate操作日志
 * @Author: zxh
 * @date: 2020/1/16
 * @Version: 1.0
 */
@Data
public class CustomOperateBo {

    /** 业务名 */
    private String name;

    /** 业务名 */
    private OperationEnum type;

    private Integer platform;

    /** 明细部分 */
    private List<CustomOperateDetailBo> detail;

}
