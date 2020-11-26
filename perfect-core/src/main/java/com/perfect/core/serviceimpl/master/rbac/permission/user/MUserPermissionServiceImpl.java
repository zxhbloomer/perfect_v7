package com.perfect.core.serviceimpl.master.rbac.permission.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.perfect.bean.bo.session.user.rbac.*;
import com.perfect.bean.entity.master.menu.MMenuEntity;
import com.perfect.bean.utils.common.tree.TreeUtil;
import com.perfect.common.constant.PerfectConstant;
import com.perfect.core.mapper.master.menu.MMenuMapper;
import com.perfect.core.mapper.master.rbac.permission.user.MUserPermissionMapper;
import com.perfect.core.service.master.rbac.permission.user.IMUserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户权限获取的逻辑实现
 * @ClassName: MUserPermissionService
 * @Description:
 * @Author: zxh
 * @date: 2020/8/26
 * @Version: 1.0
 */
@Service
@Slf4j
public class MUserPermissionServiceImpl implements IMUserPermissionService {

    @Autowired
    private MUserPermissionMapper mapper;

    @Autowired
    private MMenuMapper mMenuMapper;

    /**
     * 菜单权限数据，操作权限数据，顶部导航栏数据
     */
    @Override
    public PermissionAndTopNavBo getPermissionMenuTopNav(Long tenant_id, String pathOrIndex, String type, Long staff_id) {
        PermissionAndTopNavBo permissionAndTopNavBo = new PermissionAndTopNavBo();
        /** 获取顶部导航栏数据 */
        PermissionTopNavBo permissionTopNavBo = getTopNavData(tenant_id, pathOrIndex, type);
        permissionAndTopNavBo.setTop_nav_data(permissionTopNavBo);

        /** 获取菜单数据 */
        List<PermissionMenuBo> permissionMenuBoList = getPermissionMenu(staff_id, tenant_id, permissionTopNavBo.getActive_code());
        permissionAndTopNavBo.setUser_permission_menu(permissionMenuBoList);

        /** 获取所有nodes的id，为页面上展开所有的菜单服务 */
        List<String> allNodesId = getAllNodesId(tenant_id);
        permissionAndTopNavBo.setNodes_id(allNodesId.toArray(new String[allNodesId.size()]));


        /** 获取所有路由数据 */
        List<PermissionMenuBo> all_routers = getAllRoutersBean(tenant_id);
        permissionAndTopNavBo.setAll_routers(all_routers);

        /** 获取操作权限数据 */
        List<PermissionOperationBo> permissionOperationBoList = getPermissionOperation(staff_id, tenant_id);
        permissionAndTopNavBo.setUser_permission_operation(permissionOperationBoList);

        /** 获取redirect数据 */
        PermissionMenuBo redirect = getRedirectBean(tenant_id);
        permissionAndTopNavBo.setRedirect(redirect);

        return permissionAndTopNavBo;
    }

    /**
     * 获取所有路由数据
     * @param tenant_id
     * @return
     */
    private List<PermissionMenuBo> getAllRoutersBean(Long tenant_id){
        return mapper.getAllRouters(tenant_id);
    }

    /**
     * 获取所有nodes的id，为页面上展开所有的菜单服务
     * @param tenant_id
     * @return
     */
    private List<String> getAllNodesId(Long tenant_id){
        return mapper.getAllNodesId(tenant_id);
    }

    /**
     * 获取redirect的数据
     *
     * @param tenant_id
     * @return
     */
    private PermissionMenuBo getRedirectBean(Long tenant_id){
        PermissionMenuBo redirect = mapper.getRedirectData(tenant_id);
//        // TODO:测试代码
//        PermissionMenuBo redirect = new PermissionMenuBo();
//        redirect.setRedirect("/dashboard");
//        redirect.setPath("dashboard");
//        redirect.setComponent("/01_dashboard/index");
//        PermissionMenuMetaBo redirect_meta = new PermissionMenuMetaBo();
//        redirect_meta.setTitle("首页");
//        redirect_meta.setIcon("dashboard");
//        redirect_meta.setAffix(true);
//        redirect_meta.setActive_topnav_index("1");
//        redirect.setMeta(redirect_meta);
        redirect.setRedirect(redirect.getPath());
        return redirect;
    }

    /**
     * 菜单权限数据
     */
    @Override
    public List<PermissionMenuBo> getPermissionMenu(Long staff_id, Long tenant_id, String top_nav_code) {
        /** 判断是否有自定义菜单 */

        /** 如果没有，获取该员工的权限：（部门权限+ 岗位权限+ 员工权限+ 角色权限）- 排除权限 */
        // 获取系统菜单
        List<PermissionMenuBo> sysMenus = mapper.getSystemMenu(tenant_id, top_nav_code);
        // 部门权限defaultActive
        List<PermissionMenuBo> dept_permission_menu = mapper.getPermissionMenu(staff_id, tenant_id, top_nav_code);
        // 岗位权限
        List<PermissionMenuBo> position_permission_menu = null;
        // 员工权限
        List<PermissionMenuBo> staff_permission_menu = null;
        // 角色权限
        List<PermissionMenuBo> roles_permission_menu = null;
        // 排除权限
        List<PermissionMenuBo> remove_permission_menu = null;
        /** 权限合并 */
        for(PermissionMenuBo vo:sysMenus) {
            // 部门权限
            PermissionMenuBo dept_permission_menu_results = filterData(dept_permission_menu, vo);
            // 岗位权限
            PermissionMenuBo position_permission_menu_results = filterData(position_permission_menu, vo);
            // 员工权限
            PermissionMenuBo staff_permission_menu_results = filterData(staff_permission_menu, vo);
            // 角色权限
            PermissionMenuBo roles_permission_menu_results = filterData(roles_permission_menu, vo);
            // 排除权限
            PermissionMenuBo remove_permission_menu_results = filterData(remove_permission_menu, vo);
            /** 判断权限：（部门权限+ 岗位权限+ 员工权限+ 角色权限）- 排除权限 */
            vo.setIs_enable(getPermissionValue(dept_permission_menu_results,
                position_permission_menu_results,
                staff_permission_menu_results,
                roles_permission_menu_results,
                remove_permission_menu_results));
        }

        /** 如果有
         * TODO：暂时未实现
         * */


        /** 设置菜单树bean，并返回 */
        List<PermissionMenuBo> rtnList = TreeUtil.getTreeList(sysMenus, "menu_id");

        /** 递归菜单树，设置默认菜单 */

        return rtnList;
    }

    /**
     * 获取默认页面
     * @param tenant_id
     * @return
     */
    @Override
    public String getPermissionMenuDefaultPage(Long tenant_id) {
        /** 判断是否有自定义菜单 */

        /** 如果没有，获取default */
        MMenuEntity mMenuEntity = mMenuMapper.selectOne(new QueryWrapper<MMenuEntity>()
            .eq("tenant_id", tenant_id)
            .eq("default_open", true)
            .last("LIMIT 1")
        );

        /** 如果有
         * TODO：暂时未实现
         * */


        return mMenuEntity.getPath();
    }

    /**
     * 操作权限数据
     * @param staff_id
     * @param tenant_id
     * @return
     */
    @Override
    public List<PermissionOperationBo> getPermissionOperation(Long staff_id, Long tenant_id) {
        /** 获取操作权限数据 */
        List<PermissionOperationBo> list = mapper.getPermissionOperation(staff_id, tenant_id);
        return list;
    }

    /**
     * 查找集合中的数据，并返回
     * @param data
     * @param target_data
     * @return
     */
    private PermissionMenuBo filterData(List<PermissionMenuBo> data, PermissionMenuBo target_data){
        if(data == null) {
            return null;
        }
        Collection<PermissionMenuBo> filter = Collections2.filter(data, item -> item.getMenu_id().equals(target_data.getMenu_id()));
        return Iterables.getOnlyElement(filter);
    }

    /**
     * 判断权限：（部门权限+ 岗位权限+ 员工权限+ 角色权限）- 排除权限
     * @param dept_permission_menu          部门权限
     * @param position_permission_menu      岗位权限
     * @param staff_permission_menu         员工权限
     * @param roles_permission_menu         角色权限
     * @param remove_permission_menu        排除权限
     * @return
     */
    private boolean getPermissionValue(PermissionMenuBo dept_permission_menu,
        PermissionMenuBo position_permission_menu,
        PermissionMenuBo staff_permission_menu,
        PermissionMenuBo roles_permission_menu,
        PermissionMenuBo remove_permission_menu
        ){
        boolean rtn = false;
        if(dept_permission_menu != null){
            rtn = rtn || dept_permission_menu.getIs_enable();
        }
        if(position_permission_menu != null){
            rtn = rtn || position_permission_menu.getIs_enable();
        }
        if(staff_permission_menu != null){
            rtn = rtn || staff_permission_menu.getIs_enable();
        }
        if(roles_permission_menu != null){
            rtn = rtn || roles_permission_menu.getIs_enable();
        }
        if(remove_permission_menu != null){
            rtn = rtn & remove_permission_menu.getIs_enable();
        }

        return  rtn;
    }

    /**
     * 设置顶部导航栏数据
     * @param pathOrIndex
     * @param type
     * @return
     */
    private PermissionTopNavBo getTopNavData(Long tenant_id, String pathOrIndex, String type){

        PermissionTopNavBo permissionTopNavBo = new PermissionTopNavBo();

        List<PermissionTopNavDetailBo> topList;

        /** 根据参数获取顶部导航栏数据 */
        switch (type) {
            case PerfectConstant.TOP_NAV.TOP_NAV_FIND_BY_PATH:
                /** 按路径查询 */
                /** 获取导航栏数据 */
                topList = mapper.getTopNavByPath(tenant_id, pathOrIndex);
                permissionTopNavBo.setData(topList);
                /** 设置activeindex */
                try{
                    Collection<PermissionTopNavDetailBo> filter1 = Collections2.filter(topList, new Predicate<PermissionTopNavDetailBo>(){
                        @Override
                        public boolean apply(PermissionTopNavDetailBo input) {
                            if(input.getCode().equals(input.getActive_code())){
                                return true;
                            }else {
                                return false;
                            }
                        }
                    });

                    PermissionTopNavDetailBo bo1 = Iterables.getOnlyElement(filter1);
                    permissionTopNavBo.setActive_index(bo1.getIndex());
                    permissionTopNavBo.setActive_code(bo1.getCode());
                } catch (Exception e) {
                    log.debug("没有找到数据，找到第一个topnav");
                    permissionTopNavBo.setActive_index(topList.get(0).getIndex());
                    permissionTopNavBo.setActive_code(topList.get(0).getCode());
                }
                break;
            case PerfectConstant.TOP_NAV.TOP_NAV_FIND_BY_INDEX:
                /** 按排序查询 */
                /** 获取导航栏数据 */
                topList = mapper.getTopNav(tenant_id);
                permissionTopNavBo.setData(topList);
                Collection<PermissionTopNavDetailBo> filter2 = Collections2.filter(topList, item -> item.getIndex().equals(pathOrIndex));
                PermissionTopNavDetailBo bo2 = Iterables.getOnlyElement(filter2);
                permissionTopNavBo.setActive_index(bo2.getIndex());
                permissionTopNavBo.setActive_code(bo2.getCode());
                break;
            default:
                break;
        }

        return permissionTopNavBo;
    }
}
