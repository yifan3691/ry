package com.kevin.ods.mapper;

import java.util.List;
import com.kevin.ods.domain.OdsBusinessfee;

/**
 * 业财Mapper接口
 * 
 * @author kevin
 * @date 2024-07-03
 */
public interface OdsBusinessfeeMapper 
{
    /**
     * 查询业财
     * 
     * @param indicatorCode 业财主键
     * @return 业财
     */
    public OdsBusinessfee selectOdsBusinessfeeByIndicatorCode(String indicatorCode);

    /**
     * 查询业财列表
     * 
     * @param odsBusinessfee 业财
     * @return 业财集合
     */
    public List<OdsBusinessfee> selectOdsBusinessfeeList(OdsBusinessfee odsBusinessfee);

    /**
     * 新增业财
     * 
     * @param odsBusinessfee 业财
     * @return 结果
     */
    public int insertOdsBusinessfee(OdsBusinessfee odsBusinessfee);

    /**
     * 修改业财
     * 
     * @param odsBusinessfee 业财
     * @return 结果
     */
    public int updateOdsBusinessfee(OdsBusinessfee odsBusinessfee);

    /**
     * 删除业财
     * 
     * @param indicatorCode 业财主键
     * @return 结果
     */
    public int deleteOdsBusinessfeeByIndicatorCode(String indicatorCode);

    /**
     * 批量删除业财
     * 
     * @param indicatorCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOdsBusinessfeeByIndicatorCodes(String[] indicatorCodes);
}
