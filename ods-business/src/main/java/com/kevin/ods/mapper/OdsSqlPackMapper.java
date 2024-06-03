package com.kevin.ods.mapper;

import java.util.List;
import com.kevin.ods.domain.OdsSqlPack;

/**
 * odsMapper接口
 *
 * @author kevin
 * @date 2024-05-29
 */
public interface OdsSqlPackMapper
{
    /**
     * 查询ods
     *
     * @param Id ods主键
     * @return ods
     */
    public OdsSqlPack selectOdsSqlPackById(Long Id);

    /**
     * 查询ods列表
     *
     * @param odsSqlPack ods
     * @return ods集合
     */
    public List<OdsSqlPack> selectOdsSqlPackList(OdsSqlPack odsSqlPack);

    /**
     * 新增ods
     *
     * @param odsSqlPack ods
     * @return 结果
     */
    public int insertOdsSqlPack(OdsSqlPack odsSqlPack);

    /**
     * 修改ods
     *
     * @param odsSqlPack ods
     * @return 结果
     */
    public int updateOdsSqlPack(OdsSqlPack odsSqlPack);

    /**
     * 删除ods
     *
     * @param Id ods主键
     * @return 结果
     */
    public int deleteOdsSqlPackById(Long Id);

    /**
     * 批量删除ods
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOdsSqlPackByIds(Long[] Ids);


    /**
     * 查询最大的id
     * @return
     */
    public int selectOdsSqlMaxId();

    /**
     * 查询sqlGrop列表
     * @return
     */
    public List<OdsSqlPack> selectSqlGropList();

    /**
     * 查询sqlGropCode列表
     * @return
     */
    public List<OdsSqlPack> selectSqlGropCodeList();
    /**
     * 查询procedureGroup列表
     * @return
     */
    public List<OdsSqlPack> selectprocedureGroupList();

    /**
     * 更新有效状态
     */
    public int updateOdsSqlStatusById(String Id);


    /**
     * 批量更新有效状态
     */
    public int updateOdsSqlStatusByIds(String[] Ids);

}
