package com.perfect.core.serviceimpl.sys.platform.syscode;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.perfect.bean.bo.session.user.UserSessionBo;
import com.perfect.bean.entity.sys.platform.syscode.SCodeEntity;
import com.perfect.bean.pojo.result.CheckResult;
import com.perfect.bean.pojo.result.InsertResult;
import com.perfect.bean.pojo.result.UpdateResult;
import com.perfect.bean.result.utils.v1.CheckResultUtil;
import com.perfect.bean.result.utils.v1.InsertResultUtil;
import com.perfect.bean.result.utils.v1.UpdateResultUtil;
import com.perfect.bean.vo.sys.platform.syscode.SCodeVo;
import com.perfect.common.constant.PerfectDictConstant;
import com.perfect.common.exception.BusinessException;
import com.perfect.common.utils.CodeGenerator;
import com.perfect.common.utils.DateTimeUtil;
import com.perfect.common.utils.servlet.ServletUtil;
import com.perfect.core.mapper.sys.platform.SCodeMapper;
import com.perfect.core.service.base.v1.BaseServiceImpl;
import com.perfect.core.service.sys.platform.syscode.ISCodeService;
import com.perfect.core.utils.mybatis.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxh
 * @since 2019-07-04
 */
@Service
public class SCodeServiceImpl extends BaseServiceImpl<SCodeMapper, SCodeEntity> implements ISCodeService {

    @Autowired
    private SCodeMapper mapper;

    /**
     * 获取列表，页面查询
     *
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<SCodeVo> selectPage(SCodeVo searchCondition) {
        // 分页条件
        Page<SCodeEntity> pageCondition =
            new Page(searchCondition.getPageCondition().getCurrent(), searchCondition.getPageCondition().getSize());
        // 通过page进行排序
        PageUtil.setSort(pageCondition, searchCondition.getPageCondition().getSort());
        return mapper.selectPage(pageCondition, searchCondition);
    }
    /**
     * 获取列表，查询所有数据
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<SCodeVo> select(SCodeVo searchCondition) {
        // 查询 数据
        List<SCodeVo> list = mapper.select(searchCondition);
        return list;
    }


    /**
     * 查询by id，返回结果
     *
     * @param id
     * @return
     */
    @Override
    public SCodeVo selectByid(Long id) {
        // 查询 数据
        return mapper.selectId(id);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InsertResult<Integer> insert(SCodeEntity entity) {
        // 插入前check
        CheckResult cr = checkLogic(entity, CheckResult.INSERT_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }

        // 插入逻辑保存
        return InsertResultUtil.OK(mapper.insert(entity));
    }

    /**
     * 更新一条记录（选择字段，策略更新）
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UpdateResult<Integer> update(SCodeEntity entity) {
        // 更新前check
        CheckResult cr = checkLogic(entity, CheckResult.UPDATE_CHECK_TYPE);
        if (cr.isSuccess() == false) {
            throw new BusinessException(cr.getMessage());
        }
        // 更新逻辑保存
        entity.setC_id(null);
        entity.setC_time(null);
        return UpdateResultUtil.OK(mapper.updateById(entity));
    }


    /**
     * 获取列表，查询所有数据
     *
     * @param type
     * @return
     */
    @Override
    public List<SCodeEntity> selectByType(String type, Long equal_id, Long not_equal_id) {
        // 查询 数据
        List<SCodeEntity> rtn = mapper.selectByType(type, equal_id, not_equal_id);
        return rtn;
    }

    /**
     * check逻辑
     *
     * @return
     */
    public CheckResult checkLogic(SCodeEntity entity, String moduleType) {

        switch (moduleType) {
            case CheckResult.INSERT_CHECK_TYPE:
                // 新增场合，不能重复
                List<SCodeEntity> typeList_insert = selectByType(entity.getType(), null, null);
                if (typeList_insert.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：类型出现重复", typeList_insert);
                }
                break;
            case CheckResult.UPDATE_CHECK_TYPE:
                // 新增场合，不能重复
                List<SCodeEntity> typeList_update = selectByType(entity.getType(), null, entity.getId());
                // 更新场合，不能重复设置
                if (typeList_update.size() >= 1) {
                    return CheckResultUtil.NG("新增保存出错：类型出现重复", typeList_update);
                }
                break;
            default:
        }
        return CheckResultUtil.OK();
    }


    /**
     * 获取编号
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateResult<SCodeEntity> createCode(String type) {
        // 获取数据 for update nowait;
        SCodeEntity entity = mapper.selectForUpdateNoWait(type);
        // 1、如果不存在，报错需要配置
        if(entity == null) {
            throw new BusinessException("自动生成编号发生错误，请在编码规则中维护规则");
        }
        // 获取编码方式
        createCodeProcess(entity);

        // 反写到数据库中
        mapper.updateById(entity);

        return UpdateResultUtil.OK(entity);
    }

    /**
     * 生成编号
     * @param entity
     */
    private void createCodeProcess(SCodeEntity entity) {
        String code;
        // 获取编码方式
        switch (entity.getRule()) {
            case PerfectDictConstant.DICT_SYS_CODE_RULE_TYPE_ONE:
                // YYYYMMDD??999
                // 获取系统实际 YYYYMMDD
                String first = DateTimeUtil.dateTime();
                // 获取随机码两位
                String second_radomchar = CodeGenerator.randomAlphabet(2);
                // 自增编号
                long days = ChronoUnit.DAYS.between(entity.getU_time(), LocalDateTime.now());
                if(days >= 1) {
                    entity.setAuto_create((long)1);
                } else {
                    entity.setAuto_create((entity.getAuto_create() == null ? 0 : entity.getAuto_create()) + 1);
                }
                String suffix = CodeGenerator.addLeftZeroForNum(3, entity.getAuto_create());
                // 设置更新时间和更新id
                entity.setU_time(LocalDateTime.now());
                entity.setU_id(((UserSessionBo) ServletUtil.getUserSession()).getAccountId());
                // 合并并设置到entity
                entity.setCode(entity.getPrefex() == null ? "" : entity.getPrefex().toUpperCase() + first + second_radomchar + suffix);
                break;
            case PerfectDictConstant.DICT_SYS_CODE_RULE_TYPE_TWO:
                // P9999
                entity.setAuto_create((entity.getAuto_create() == null ? 0 : entity.getAuto_create()) + 1);
                code = String.format("%04d", entity.getAuto_create());
                entity.setCode("P" + code);
                break;
            case PerfectDictConstant.DICT_SYS_CODE_RULE_TYPE_THREE:
                // PC9999
                entity.setAuto_create((entity.getAuto_create() == null ? 0 : entity.getAuto_create()) + 1);
                code = String.format("%08d", entity.getAuto_create());
                entity.setCode("P" + code);
                break;
        }
    }
}
