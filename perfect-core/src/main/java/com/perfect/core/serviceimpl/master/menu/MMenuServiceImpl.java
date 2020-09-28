package com.perfect.core.serviceimpl.master.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.perfect.bean.entity.master.menu.MMenuEntity;
import com.perfect.bean.pojo.result.*;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.DeleteResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.utils.common.tree.TreeUtil;
import com.perfect.bean.vo.common.component.TreeNode;
import com.perfect.bean.vo.master.menu.MMenuDataVo;
import com.perfect.bean.vo.master.menu.MMenuPageFunctionVo;
import com.perfect.bean.vo.master.menu.MMenuRedirectVo;
import com.perfect.bean.vo.master.menu.MMenuVo;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.exception.InsertErrorException;
import com.perfect.common.exception.UpdateErrorException;
import com.perfect.common.utils.bean.BeanUtilsSupport;
import com.perfect.core.mapper.master.menu.MMenuMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.master.menu.IMMenuService;
import com.perfect.core.serviceimpl.common.autocode.MMenuAutoCodeImpl;
import com.perfect.core.serviceimpl.common.autocode.MMenuRouteNameAutoCodeImpl;
import com.perfect.core.utils.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  菜单 服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-08-23
 */
@Service
public class MMenuServiceImpl extends BaseServiceImpl<MMenuMapper, MMenuEntity> implements IMMenuService {

    @Autowired
    private MMenuMapper mapper;
    @Autowired
    private MMenuAutoCodeImpl mMenuAutoCode;
    @Autowired
    private MMenuRouteNameAutoCodeImpl mMenuRouteNameAutoCode;
    @Autowired
    private MMenuServiceImpl self;

    /**
     * 获取列表，查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public MMenuVo getTreeData(MMenuDataVo searchCondition) {
        MMenuVo mMenuVo = new MMenuVo();
        // 查询 菜单 数据
        List<MMenuDataVo> list = mapper.select(searchCondition);
        setDepthId(list);
        // 设置树bean
        List<MMenuDataVo> rtnList = TreeUtil.getTreeList(list);
        // 获取按钮清单
        List<Long> root_ids = new ArrayList<>();
        rtnList.stream()
            .collect(Collectors.toMap(MMenuDataVo::getRoot_id, Function.identity(), (oldValue, newValue) -> oldValue))
            .values()
            .stream()
            .forEach(item -> root_ids.add(item.getRoot_id()));
        searchCondition.setRoot_ids((Long[]) root_ids.toArray(new Long[root_ids.size()]));
        List<MMenuPageFunctionVo> pageFunctionVoList = mapper.getAllMenuButton(searchCondition);

        mMenuVo.setMenu_data(rtnList);
        mMenuVo.setMenu_buttons(pageFunctionVoList);

        return mMenuVo;
    }


    /**
     * 级联：获取列表，查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MMenuDataVo> getCascaderList(MMenuVo searchCondition) {
        // 查询 数据
        List<MMenuDataVo> list = mapper.getCascaderList(searchCondition);
        setDepthId(list);
        List<MMenuDataVo> rtnList = TreeUtil.getTreeList(list);
        return rtnList;
    }

    /**
     * 格式化depth_id，parent_depth_id成数组
     * @param list
     */
    private void setDepthId(List<MMenuDataVo> list){
        // 循环结果，格式化depth_id，parent_depth_id成数组
        for (MMenuDataVo vo:list) {
            // 格式化depth_id
            if(vo.getDepth_id() != null) {
                String[] split_depth_id = vo.getDepth_id().split(",");
                List<Long> depth_id_array = new ArrayList<>();
                for (int i = 0; i < split_depth_id.length; i++) {
                    depth_id_array.add(Long.valueOf(split_depth_id[i]));
                }
                vo.setDepth_id_array(depth_id_array);
            }
            // 格式化parent_depth_id
            if(vo.getParent_depth_id() != null) {
                String[] split_parent_depth_id = vo.getParent_depth_id().split(",");
                List<Long> parent_depth_id_array = new ArrayList<>();
                for (int i = 0; i < split_parent_depth_id.length; i++) {
                    parent_depth_id_array.add(Long.valueOf(split_parent_depth_id[i]));
                }
                vo.setParent_depth_id_array(parent_depth_id_array);
            }
        }
    }

    /**
     * 级联：获取列表，查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public MMenuVo getCascaderGet(MMenuVo searchCondition) {
        // 查询 数据
        MMenuVo vo = mapper.getCascaderGet(searchCondition);
        return vo;
    }

    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    @Override
    public MMenuDataVo selectByid(Long id) {
        // 查询 数据
        return mapper.selectId(id);
    }

    /**
     * 获取列表，根据id查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<MMenuEntity> selectIdsIn(List<MMenuVo> searchCondition) {
        // 查询 数据
        List<MMenuEntity> list = mapper.selectIdsIn(searchCondition);
        return list;
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<MMenuDataVo> addMenuGroup(MMenuDataVo vo) {

        MMenuEntity entity = (MMenuEntity)BeanUtilsSupport.copyProperties(vo, MMenuEntity.class);

        // 默认菜单
        if( vo.getIs_default() == null) {
            vo.setIs_default(false);
        }

        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 插入逻辑保存
        entity.setVisible(false);
        entity.setType(PerfectDictConstant.DICT_SYS_MENU_TYPE_ROOT);

        // 设置path
        entity.setPath("/");

        // 获取id
        int insertCount = mapper.insert(entity);
        if(insertCount ==0){
            throw new InsertErrorException("保存失败，请查询后重新再试。");
        }
        // 修改root_id
        entity.setRoot_id(entity.getId());
        // 更新数据库
        entity.setC_id(null);
        entity.setC_time(null);
//        if(StringUtil.isEmpty(entity.getCode())){
//            entity.setCode(mMenuAutoCode.autoCode().getCode());
//        }
        int updCount = mapper.updateById(entity);
        if(updCount ==0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 设置返回值
        return InsertResultUtil.OK(this.selectByid(entity.getId()));
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<MMenuDataVo> addTopNav(MMenuDataVo vo) {
        MMenuEntity entity = (MMenuEntity)BeanUtilsSupport.copyProperties(vo, MMenuEntity.class);
        entity.setVisible(false);
        // 获取父亲的entity
        MMenuEntity parentEntity = getById(entity.getParent_id());
        Integer son_count = parentEntity.getSon_count();
        son_count = (son_count == null ? 0 : son_count)  + 1;
        parentEntity.setSon_count(son_count);
        // 保存父亲的儿子的个数
        parentEntity.setC_id(null);
        parentEntity.setC_time(null);
        if(mapper.updateById(parentEntity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 获取父亲的code
        String parentCode = parentEntity.getCode();
        // 计算当前编号
        // 获取当前son_count
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String str = String.format("%04d", son_count);
        entity.setCode(parentCode + str);
        entity.setSon_count(0);

        // 设置name
        entity.setMeta_title(vo.getName());

        // 设置type
        entity.setType(PerfectDictConstant.DICT_SYS_MENU_TYPE_TOPNAV);

        // 设置路径
        entity.setPath(vo.getPath());

        // 保存
        if(mapper.insert(entity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 设置返回值
        return InsertResultUtil.OK(this.selectByid(entity.getId()));
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<MMenuDataVo> addSubNode(MMenuDataVo vo) {

        MMenuEntity entity = (MMenuEntity)BeanUtilsSupport.copyProperties(vo, MMenuEntity.class);

        // 插入逻辑保存
        entity.setVisible(false);

        // 获取父亲的entity
        MMenuEntity parentEntity = getById(entity.getParent_id());
        Integer son_count = parentEntity.getSon_count();
        son_count = (son_count == null ? 0 : son_count)  + 1;
        parentEntity.setSon_count(son_count);
        // 保存父亲的儿子的个数
        parentEntity.setC_id(null);
        parentEntity.setC_time(null);
        if(mapper.updateById(parentEntity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 获取父亲的code
        String parentCode = parentEntity.getCode();
        // 计算当前编号
        // 获取当前son_count
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String str = String.format("%04d", son_count);
        entity.setCode(parentCode + str);
        entity.setSon_count(0);

        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }

        // 设置type
        entity.setType(PerfectDictConstant.DICT_SYS_MENU_TYPE_NODE);

        // 设置meta_titile
        entity.setMeta_title(entity.getName());

        // 设置component 为null
        entity.setComponent(null);

        // 设置路径
        entity.setPath(vo.getPath());

        // 保存
        if(mapper.insert(entity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 设置返回值
        return InsertResultUtil.OK(this.selectByid(entity.getId()));
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<MMenuDataVo> addSubMenu(MMenuDataVo vo) {

        MMenuEntity entity = (MMenuEntity)BeanUtilsSupport.copyProperties(vo, MMenuEntity.class);

        // 插入逻辑保存
        entity.setVisible(false);

        // 获取父亲的entity
        MMenuEntity parentEntity = getById(entity.getParent_id());
        Integer son_count = parentEntity.getSon_count();
        son_count = (son_count == null ? 0 : son_count)  + 1;
        parentEntity.setSon_count(son_count);
        // 保存父亲的儿子的个数
        parentEntity.setC_id(null);
        parentEntity.setC_time(null);
        if(mapper.updateById(parentEntity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 获取父亲的code
        String parentCode = parentEntity.getCode();
        // 计算当前编号
        // 获取当前son_count
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String str = String.format("%04d", son_count);
        entity.setCode(parentCode + str);
        entity.setSon_count(0);

        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }

        // 设置type
        entity.setType(PerfectDictConstant.DICT_SYS_MENU_TYPE_PAGE);

        // 设置路径
        entity.setPath(vo.getPath());

        // 设置路由名，自动生成
        entity.setRoute_name(mMenuRouteNameAutoCode.autoCode().getCode());

        // 保存
        if(mapper.insert(entity) == 0){
            throw new UpdateErrorException("保存失败，请查询后重新再试。");
        }
        // 设置返回值
        return InsertResultUtil.OK(this.selectByid(entity.getId()));
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     * @param entity 实体对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<MMenuDataVo> update(MMenuEntity entity) {
        switch (entity.getType()) {
            case PerfectDictConstant.DICT_SYS_MENU_TYPE_TOPNAV:
                break;
            default:
                // 更新前check
                CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
                if (cr.isSuccess() == false) {
                    throw new BusinessException(cr.getMessage());
                }
                break;
        }

        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        entity.setMeta_title(entity.getName());
        if(entity.getType().equals(PerfectDictConstant.DICT_SYS_MENU_TYPE_NODE)){
            entity.setComponent(null);
        }
        if(mapper.updateById(entity) == 0) {
            throw new UpdateErrorException("保存的数据已经被修改，请查询后重新编辑更新。");
        }
        return UpdateResultUtil.OK(this.selectByid(entity.getId()));
    }

    /**
     * 批量删除复原
     *
     * @param searchCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeleteResult<String> realDeleteByCode(MMenuDataVo searchCondition) {
        // 删除当前以及子菜单
        mapper.realDeleteByCode(searchCondition);
        return DeleteResultUtil.OK("OK");
    }

//    /**
//     * 获取列表，查询所有数据
//     *
//     * @param code
//     * @return
//     */
//    public List<MMenuEntity> selectByCode(String code, Long equal_id, Long not_equal_id) {
//        // 查询 数据
//        List<MMenuEntity> list = mapper.selectByCode(code, equal_id, not_equal_id);
//        return list;
//    }
    /**
     * check逻辑
     * @return
     */
    public CheckResult checkLogic(MMenuEntity entity, String moduleType){
        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合
                // url check
                if(countUrl(entity, moduleType) >= 1) {
                    return CheckResultUtil.NG("新增保存出错：请求地址【"+ entity.getPath() +"】出现重复!", entity.getName());
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 更新场合
                // url check
                if(countUrl(entity, moduleType) >= 1) {
                    return CheckResultUtil.NG("更新保存出错：请求地址【"+ entity.getPath() +"】出现重复!", entity.getName());
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }

    /**
     * 获取相同url的count
     * @param entity
     * @param moduleType
     * @return
     */
    private Integer countUrl(MMenuEntity entity, String moduleType){
        return mapper.selectCount(new QueryWrapper<MMenuEntity>()
            .eq("path", entity.getPath())
            .eq(entity.getTenant_id() == null ? false:true,"tenant_id", entity.getTenant_id())
            .ne(CheckResult.UPDATE_CHECK_TYPE.equals(moduleType) ? true:false, "root_id", entity.getRoot_id())
        );
    }


    /**
     * 拖拽保存
     * 未使用乐观锁，需要注意
     * @param beans
     * @return
     */
    @Override
    public Boolean dragsave(List<MMenuDataVo> beans) {
        List<MMenuEntity> entities = new ArrayList<>();
        int code = 0;
        List<MMenuEntity> beanList = dragData2List(beans, null ,entities, code);
        /**
         * 注意调用方法，必须使用外部调用，激活aop，内部调用不能激活aop和注解
         */
        return self.dragsave2Db(beanList);
    }

    /**
     * 拖拽数据规整
     * @param beans         ：循环的beans
     * @param parent_bean   ：父亲bean
     * @param entities      ：最终返回的list bean
     * @param code          ：
     * @return
     */
    private List<MMenuEntity> dragData2List(List<? extends TreeNode> beans, MMenuEntity parent_bean, List<MMenuEntity> entities, int code) {
        for (TreeNode bean : beans) {
            code = code + 1;
            MMenuEntity entity = new MMenuEntity();
            entity.setId(bean.getId());
            entity.setParent_id(bean.getParent_id());
            if(parent_bean == null) {
                entity.setCode(String.format("%04d", code));
            } else {
                entity.setCode(parent_bean.getCode() + String.format("%04d", code));
            }
            entities.add(entity);
            if(bean.getChildren().size() !=0){
                dragData2List(bean.getChildren(), entity, entities, 0);
            }
        }
        return entities;
    }

    /**
     * 拖拽保存
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean dragsave2Db(List<MMenuEntity> list){
        // 编号重置
        for (MMenuEntity entity : list) {
            if(entity.getParent_id() != null){
                setParentSonCount(list, entity.getParent_id());
            }
        }
        // 更新开始
        for (MMenuEntity entity : list) {
            entity.setSon_count(entity.getSon_count() == null ? 0 : entity.getSon_count());
            entity.setU_id(SecurityUtil.getLoginUser_id());
            entity.setU_time(LocalDateTime.now());
            mapper.updateDragSave(entity);
        }
        return true;
    }

    /**
     * 设置儿子个数
     * @return
     */
    private List<MMenuEntity> setParentSonCount(List<MMenuEntity> entities, Long parent_id) {
        for(MMenuEntity entity : entities){
            if(entity.getId().equals(parent_id)){
                entity.setSon_count(entity.getSon_count() == null ? 1 : entity.getSon_count() + 1);
            }
        }
        return entities;
    }

    /**
     * 菜单重定向更新保存
     * @param bean
     * @return
     */
    @Override
    public InsertOrUpdateResult<MMenuRedirectVo> saveRedirect(MMenuRedirectVo bean) {
        return null;
    }
}
