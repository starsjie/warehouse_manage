package com.pn.service;

import com.pn.entity.Result;
import com.pn.entity.Store;
import com.pn.page.Page;

import java.util.List;

/**
 * @author ljj
 */
public interface StoreService {

    //查询所有仓库的业务方法
    public List<Store> queryAllStore();

    //分页查询仓库的业务方法
    public Page queryStorePage(Page page, Store store);

    public Result chenkStroeNum(String storeNum);
    //添加仓库的业务方法
    public Result saveStore(Store store);

    //修改仓库的业务方法
    public Result updateStore(Store store);

    //删除仓库的业务方法
    public Result deleteStore(Integer storeId);
}
