package com.kevin.ods.service;

import java.util.List;
import com.kevin.ods.domain.OdsSqlPack;

/**
 * odsService接口
 *
 * @author kevin
 * @date 2024-05-29
 */
public interface IOdsSqlPackService
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
     * 批量删除ods
     *
     * @param Ids 需要删除的ods主键集合
     * @return 结果
     */
    public int deleteOdsSqlPackByIds(Long[] Ids);

    /**
     * 删除ods信息
     *
     * @param Id ods主键
     * @return 结果
     */
    public int deleteOdsSqlPackById(Long Id);

    /**
     * 查询sqlGrop列表
     * @return
     */
    public List<String> selectSqlGropList();




    /**
     * 更新有效状态
     */
    public int updateOdsSqlStatusById(Long Id);


    /**
     * 批量更新有效状态
     */
    public int updateOdsSqlStatusByIds(Long[] Ids);

}
