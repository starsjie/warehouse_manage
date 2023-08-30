package com.pn.mapper;

import com.pn.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljj
 */
@Mapper
public interface StatisticsMapper {
    //统计各个仓库商品库存数量的方法
    public List<Statistics> statisticsStoreInvent();
}
