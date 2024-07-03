package com.kevin.ods.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kevin.ods.mapper.OdsBusinessfeeMapper;
import com.kevin.ods.domain.OdsBusinessfee;
import com.kevin.ods.service.IOdsBusinessfeeService;

/**
 * 业财Service业务层处理
 *
 * @author kevin
 * @date 2024-07-03
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class OdsBusinessfeeServiceImpl implements IOdsBusinessfeeService
{
    @Autowired
    private OdsBusinessfeeMapper odsBusinessfeeMapper;

    /**
     * 查询业财
     *
     * @param indicatorCode 业财主键
     * @return 业财
     */
    @Override
    public OdsBusinessfee selectOdsBusinessfeeByIndicatorCode(String indicatorCode)
    {
        return odsBusinessfeeMapper.selectOdsBusinessfeeByIndicatorCode(indicatorCode);
    }

    /**
     * 查询业财列表
     *
     * @param odsBusinessfee 业财
     * @return 业财
     */
    @Override
    public List<OdsBusinessfee> selectOdsBusinessfeeList(OdsBusinessfee odsBusinessfee)
    {
        return odsBusinessfeeMapper.selectOdsBusinessfeeList(odsBusinessfee);
    }

    /**
     * 新增业财
     *
     * @param odsBusinessfee 业财
     * @return 结果
     */
    @Override
    public int insertOdsBusinessfee(OdsBusinessfee odsBusinessfee)
    {
        return odsBusinessfeeMapper.insertOdsBusinessfee(odsBusinessfee);
    }

    /**
     * 修改业财
     *
     * @param odsBusinessfee 业财
     * @return 结果
     */
    @Override
    public int updateOdsBusinessfee(OdsBusinessfee odsBusinessfee)
    {
        return odsBusinessfeeMapper.updateOdsBusinessfee(odsBusinessfee);
    }

    /**
     * 批量删除业财
     *
     * @param indicatorCodes 需要删除的业财主键
     * @return 结果
     */
    @Override
    public int deleteOdsBusinessfeeByIndicatorCodes(String[] indicatorCodes)
    {
        return odsBusinessfeeMapper.deleteOdsBusinessfeeByIndicatorCodes(indicatorCodes);
    }

    /**
     * 删除业财信息
     *
     * @param indicatorCode 业财主键
     * @return 结果
     */
    @Override
    public int deleteOdsBusinessfeeByIndicatorCode(String indicatorCode)
    {
        return odsBusinessfeeMapper.deleteOdsBusinessfeeByIndicatorCode(indicatorCode);
    }
}
