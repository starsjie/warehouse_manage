package com.pn.mapper;

import com.pn.entity.Store;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljj
 */
@Mapper
public interface StoreMapper {

    //查询所有仓库的方法
    public List<Store> findAllStore();

    //查询仓库总行数的方法
    public int selectStoreCount(Store store);

    //分页查询仓库的方法
    public List<Store> selectStorePage(@Param("page") Page page,
                                       @Param("store") Store store);


    //根据仓库名称或者仓库编码查询的方法
    public Store findStoreByNum(String storeNum);
    //添加仓库的方法
    public Integer insertStore(Store store);

    //根据仓库id修改仓库的方法
    public int updateStoreById(Store store);

    //根据仓库id删除仓库的方法
    public int deleteStoreById(Integer storeId);
}
