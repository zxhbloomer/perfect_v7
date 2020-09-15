package com.perfect.bean.vo.master.user;

import com.perfect.bean.config.base.v1.BaseVo;
import com.perfect.common.annotations.ExcelAnnotion;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工
 * </p>
 *
 * @author zxh
 * @since 2019-07-13
 */
@Data
@NoArgsConstructor
@ApiModel(value = "员工主表导出Bean", description = "员工主表导出Bean")
@EqualsAndHashCode(callSuper=false)
public class MStaffExportVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 4733748930234972849L;

    private Long id;

    /**
     * 姓名
     */
    @ExcelAnnotion(name = "员工")
    private String name;

    /**
     * 全称拼音
     */
    @ExcelAnnotion(name = "员工全称拼音")
    private String name_py;

    /**
     * 简称
     */
    @ExcelAnnotion(name = "员工简称")
    private String simple_name;

    /**
     * 简称拼音
     */
    @ExcelAnnotion(name = "员工简称拼音")
    private String simple_name_py;

    /**
     * 男=1,女=2
     */
    @ExcelAnnotion(name = "性别")
    private String sex_text;

    /**
     * 生日
     */
    @ExcelAnnotion(name = "生日")
    private LocalDate birthday;

    /**
     * 身份证号码
     */
    @ExcelAnnotion(name = "身份证号码")
    private String id_card;

    /**
     * 护照号码
     */
    @ExcelAnnotion(name = "护照号码")
    private String passport;

    /**
     * 是否在职：在职=1,不在职=0,离职=2,离退休=3,返聘=4
     */
    @ExcelAnnotion(name = "是否在职")
    private String service_text;

    /**
     * 婚否
     */
    @ExcelAnnotion(name = "是否在职")
    private String is_wed_text;

    /**
     * 名族
     */
    @ExcelAnnotion(name = "名族")
    private String nation;

    /**
     * 学历
     */
    @ExcelAnnotion(name = "学历")
    private String degree_text;

    /**
     * 邮箱地址
     */
    @ExcelAnnotion(name = "邮箱地址")
    private String email;

    /**
     * 家庭电话
     */
    @ExcelAnnotion(name = "家庭电话")
    private String home_phone;

    /**
     * 办公室电话
     */
    @ExcelAnnotion(name = "办公室电话")
    private String office_phone;

    /**
     * 手机号码
     */
    @ExcelAnnotion(name = "手机号码")
    private String mobile_phone;

    /**
     * 备用手机号码
     */
    @ExcelAnnotion(name = "备用手机号码")
    private String mobile_phone_backup;

    /**
     * 备用电子邮件
     */
    @ExcelAnnotion(name = "备用电子邮件")
    private String email_backup;


    /**
     * 所属公司
     */
    private Long company_id;
    @ExcelAnnotion(name = "所属公司")
    private String company_name;
    private String company_simple_name;

    /**
     * 默认部门
     */
    private Long dept_id;
    @ExcelAnnotion(name = "默认部门")
    private String dept_name;
    private String dept_simple_name;

    /**
     * 是否删除
     */
    private Boolean is_del;
    @ExcelAnnotion(name = "是否删除")
    private String is_del_name;


    @ExcelAnnotion(name = "新增人")
    private String c_name;
    @ExcelAnnotion(name = "新增时间")
    private LocalDateTime c_time;

    private Long u_id;

    @ExcelAnnotion(name = "更新人")
    private String u_name;
    @ExcelAnnotion(name = "更新时间")
    private LocalDateTime u_time;
}
