package com.kevin.ods.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kevin.ods.mapper.OdsSqlPackMapper;
import com.kevin.ods.domain.OdsSqlPack;
import com.kevin.ods.service.IOdsSqlPackService;

/**
 * odsService业务层处理
 *
 * @author kevin
 * @date 2024-05-29
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class OdsSqlPackServiceImpl implements IOdsSqlPackService
{
    @Autowired
    private OdsSqlPackMapper odsSqlPackMapper;

    /**
     * 查询ods
     *
     * @param Id ods主键
     * @return ods
     */
    @Override
    public OdsSqlPack selectOdsSqlPackById(Long Id)
    {
        return odsSqlPackMapper.selectOdsSqlPackById(Id);
    }

    /**
     * 查询ods列表
     *
     * @param odsSqlPack ods
     * @return ods
     */
    @Override
    public List<OdsSqlPack> selectOdsSqlPackList(OdsSqlPack odsSqlPack)
    {
        return odsSqlPackMapper.selectOdsSqlPackList(odsSqlPack);
    }

    /**
     * 新增ods
     *
     * @param odsSqlPack ods
     * @return 结果
     */
    @Override
    public int insertOdsSqlPack(OdsSqlPack odsSqlPack)
    {
        int maxId = odsSqlPackMapper.selectOdsSqlMaxId();
        OdsSqlPack newOdsSqlPack = new OdsSqlPack();
        newOdsSqlPack.setSqlGroup(odsSqlPack.getSqlGroup());
        newOdsSqlPack.setSqlGroupCode(odsSqlPack.getSqlGroupCode());
        newOdsSqlPack.setProcedureGroup(odsSqlPack.getProcedureGroup());


        List<OdsSqlPack> odsSqlPacks = odsSqlPackMapper.selectOdsSqlPackList(newOdsSqlPack);
        OdsSqlPack maxSqlPriorityOdsSqlPack = odsSqlPacks.stream()
                .max(Comparator.comparing(OdsSqlPack::getSqlPriority))
                .orElse(null);
        odsSqlPack.setSqlPriority(maxSqlPriorityOdsSqlPack.getSqlPriority()+1);
        odsSqlPack.setId((long) (maxId+1));
        odsSqlPack.setStatus("EFFECTIVE");
        return odsSqlPackMapper.insertOdsSqlPack(odsSqlPack);
    }

    /**
     * 修改ods
     *
     * @param odsSqlPack ods
     * @return 结果
     */
    @Override
    public int updateOdsSqlPack(OdsSqlPack odsSqlPack)
    {
        return odsSqlPackMapper.updateOdsSqlPack(odsSqlPack);
    }

    /**
     * 批量删除ods
     *
     * @param Ids 需要删除的ods主键
     * @return 结果
     */
    @Override
    public int deleteOdsSqlPackByIds(Long[] Ids)
    {
        return odsSqlPackMapper.deleteOdsSqlPackByIds(Ids);
    }

    /**
     * 删除ods信息
     *
     * @param Id ods主键
     * @return 结果
     */
    @Override
    public int deleteOdsSqlPackById(Long Id)
    {
        return odsSqlPackMapper.deleteOdsSqlPackById(Id);
    }

    /**
     * 查询sqlGrop列表
     * @return
     */
    @Override
    public List<OdsSqlPack> selectSqlGropList() {
        return odsSqlPackMapper.selectSqlGropList();
    }

    @Override
    public List<OdsSqlPack> selectprocedureGroupList() {
        return odsSqlPackMapper.selectprocedureGroupList();
    }

    @Override
    public List<OdsSqlPack> selectsqlGroupCodeList() {
        return odsSqlPackMapper.selectSqlGropCodeList();
    }

    /**
     * 更新有效状态
     */
    @Override
    public int updateOdsSqlStatusById(String Id) {
        return odsSqlPackMapper.updateOdsSqlStatusById(Id);
    }

    @Override
    public int updateOdsSqlStatusByIds(String[] Ids) {
        return odsSqlPackMapper.updateOdsSqlStatusByIds(Ids);
    }
}
