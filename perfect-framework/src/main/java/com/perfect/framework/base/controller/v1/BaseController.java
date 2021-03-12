package com.perfect.framework.base.controller.v1;

import com.alibaba.fastjson.JSON;
import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.bo.sys.SysInfoBo;
import com.perfect.bean.pojo.fs.UploadFileResultPojo;
import com.perfect.bean.vo.master.user.MStaffVo;
import com.perfect.common.annotations.SysLogAnnotion;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.properies.PerfectConfigProperies;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.common.utils.servlet.ServletUtil;
import com.perfect.core.service.client.user.IMUserService;
import com.perfect.core.service.master.rbac.permission.user.IMUserPermissionRbacService;
import com.perfect.excel.bean.importconfig.template.ExcelTemplate;
import com.perfect.excel.export.ExcelUtil;
import com.perfect.excel.upload.PerfectExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * controller父类
 * 
 * @author zhangxh
 */
@Slf4j
@Component
public class BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PerfectConfigProperies perfectConfigProperies;

    @Autowired
    private IMUserService service;

    @Autowired
    private IMUserPermissionRbacService imUserPermissionRbacService;

    /** 开发者模式，可以跳过验证码 */
    @Value("${perfect.security.develop-model}")
    private Boolean developModel;

    /**
     * 通用文件上传
     * @param fileUrl
     * @return
     */
    public <T> T uploadFile(String fileUrl, Class<T> classOfT) throws IllegalAccessException, InstantiationException {
        // 上传的url
        String uploadFileUrl = perfectConfigProperies.getFsUrl();
        FileSystemResource resource = new FileSystemResource(fileUrl);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("appid", perfectConfigProperies.getFsAppid());
        param.add("username", perfectConfigProperies.getFsUsername());
        param.add("groupid", perfectConfigProperies.getFsGroupid());
        /**
         * request 头信息
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
        ResponseEntity<Map> re = restTemplate.exchange(uploadFileUrl, HttpMethod.POST, httpEntity, Map.class);
        if (re.getStatusCode().value() != HttpStatus.OK.value()) {
            throw new BusinessException("错误文件处理失败");
        }
        UploadFileResultPojo uploadFileResultPojo = JSON.parseObject(JSON.toJSONString(re.getBody().get("data")), UploadFileResultPojo.class);
        // 判断文件是否存在
        File file = new File(fileUrl);
        if (file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("文件删除失败");
            }
        }
        // 复制UploadFileResultPojo类中的属性到，返回的bean中
        T rtnBean = classOfT.newInstance();
        BeanUtilsSupport.copyProperties(uploadFileResultPojo, rtnBean);

        return rtnBean;
    }

    /**
     * 获取excel导入文件，并check是否是excel文件，然后根据模板定义进行导入
     * 如果有错误，则会生成错误excel，供客户下载查看。
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public PerfectExcelReader downloadExcelAndImportData(String fileUrl, String jsonConfig) throws IOException {
        ExcelTemplate et = initExcelTemplate(jsonConfig);

        // 文件下载到流
        ResponseEntity<byte[]> rtnResponse = restTemplate.getForEntity(fileUrl, byte[].class);
        InputStream is =  new ByteArrayInputStream(rtnResponse.getBody());

        // 文件分析，判断是否是excel文档
        if (FileMagic.valueOf(is) == FileMagic.OLE2){
            // Office 2003 ，xls
        } else if (FileMagic.valueOf(is) == FileMagic.OOXML) {
            // Office 2007 +，xlsx
        } else {
            // 非excel文档，报错
            throw new IllegalArgumentException("导入的文件不是office excel，请选择正确的文件来进行上传");
        }

        // 2、按模版进行读取数据
        PerfectExcelReader perfectExcelReader = new PerfectExcelReader(is, et);

        return perfectExcelReader;
    }

    /**
     * 获取excel模版
     * @param jsonConfig
     * @return
     */
    public ExcelTemplate initExcelTemplate(String jsonConfig){
        // 1、获取模板配置类
        ExcelTemplate et = JSON.parseObject(jsonConfig, ExcelTemplate.class);
        // 初始化
        et.initValidator();
        return et;
    }

    /**
     * 通用文件下载
     * @param filePath
     * @param fileName
     * @param response
     */
    public void fileDownLoad(String filePath, String fileName, HttpServletResponse response) throws IOException {
        ExcelUtil.download(filePath, fileName , response);
    }

    /**
     * 获取当前登录用户的session数据
     * @return
     */
    public void setStaffInUserSession(MStaffVo staff_info){
        UserSessionBo bo = (UserSessionBo) ServletUtil.getUserSession();
        bo.setStaff_info(staff_info);
//        return bo;
    }

    /**
     * 获取当前登录用户的session数据
     * @return
     */
    public UserSessionBo getUserSession(){
        UserSessionBo bo = (UserSessionBo) ServletUtil.getUserSession();
        return bo;
    }

    /**
     * 获取当前登录用户的session数据:租户数据
     * @return
     */
    public Long getUserSessionTenantId(){
        Long tenant_Id = getUserSession().getTenant_Id();
        return tenant_Id;
    }

    /**
     * 获取当前登录用户的session数据:员工id
     * @return
     */
    public Long getUserSessionStaffId(){
        Long id = getUserSession().getStaff_Id();
        return id;
    }

    /**
     * 获取当前登录用户的session数据:账户id
     * @return
     */
    public Long getUserSessionAccountId(){
        Long id = getUserSession().getAccountId();
        return id;
    }

    /**
     * 加密密码
     * @param psdOrignalCode
     * @return
     */
    public String getPassword(String psdOrignalCode){
        //加密对象
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(psdOrignalCode);
        return encodePassword;
    }

    /**
     * 执行usersession往session中保存的逻辑
     *
     */
    public void doResetUserSessionByLoginUserId(Long loginUser_id) {
        // 如果更新的是当前登录的用户则，刷新当前登录用户，否则pass
        if(loginUser_id.equals(this.getUserSessionAccountId())) {
            this.resetUserSession(this.getUserSessionAccountId(), PerfectConstant.LOGINUSER_OR_STAFF_ID.LOGIN_USER_ID);
        }
    }

    /**
     * 执行usersession往session中保存的逻辑
     *
     */
    public void doResetUserSessionByStaffId(Long staff_id) {
        // 如果更新的是当前登录的用户则，刷新当前登录用户，否则pass
        if(staff_id.equals(this.getUserSessionStaffId())) {
            this.resetUserSession(this.getUserSessionStaffId(), PerfectConstant.LOGINUSER_OR_STAFF_ID.STAFF_ID);
        }
    }

    /**
     * 获取user信息，权限信息，并保存到redis中
     * 1：userbean信息
     * 2：系统参数
     * 执行usersession往session中保存的逻辑
     */
    @SysLogAnnotion("设置用户session，包含：用户、员工、租户、系统参数、菜单权限、操作权限数据")
    public void resetUserSession(Long id, String loginOrStaffId ) {
        /** 设置1：userbean信息  */
        UserSessionBo userSessionBo = service.getUserBean(id, loginOrStaffId);
        String sessionId = ServletUtil.getSession().getId();

        // 设置系统信息
        SysInfoBo sysInfoBo = new SysInfoBo();
        sysInfoBo.setDevelopModel(developModel);
        /** 设置2：系统参数  */
        userSessionBo.setSys_Info(sysInfoBo);

        /** 设置session id */
        userSessionBo.setSession_id(sessionId);
        userSessionBo.setAppKey("PC_APP");
        userSessionBo.setTenant_Id(userSessionBo.getStaff_info().getTenant_id());
        userSessionBo.setTenantAdmin(false);

        /** 把用户session，保存到redis中 */
        HttpSession session = ServletUtil.getSession();
        String key_session = PerfectConstant.SESSION_PREFIX.SESSION_USER_PREFIX_PREFIX + "_" + sessionId;
        if (ServletUtil.getUserSession() != null) {
            session.removeAttribute(key_session);
            session.setAttribute(key_session, userSessionBo);
        } else {
            session.setAttribute(key_session, userSessionBo);
        }
    }
}
