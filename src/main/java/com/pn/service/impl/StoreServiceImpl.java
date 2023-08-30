package com.pn.service.impl;

import com.pn.entity.Result;
import com.pn.entity.Store;
import com.pn.mapper.StoreMapper;
import com.pn.page.Page;
import com.pn.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ljj
 * @create 2023/8/26 9:07
 */
//指定缓存的名称即键的前缀,一般是@CacheConfig标注的类的全类名
@CacheConfig(cacheNames = "com.pn.service.impl.StoreServiceImpl")
@Service
public class StoreServiceImpl implements StoreService {

    //注入StoreMapper
    @Autowired
    private StoreMapper storeMapper;

    /*
     查询所有仓库的业务方法
    */
    //对查询到的所有仓库进行缓存,缓存到redis的键为all:store
    @Cacheable(key = "'all:store'")
    @Override
    public List<Store> queryAllStore() {
        //查询所有仓库
        return storeMapper.findAllStore();
    }

    //分页查询仓库的业务方法
    @Override
    public Page queryStorePage(Page page, Store store) {
        //查询仓库总行数
        int storeCount = storeMapper.selectStoreCount(store);

        //分页查询仓库
        List<Store> storeList = storeMapper.selectStorePage(page, store);

        //将查到的总行数和当前页数据封装到Page对象
        page.setTotalNum(storeCount);
        page.setResultList(storeList);

        return page;
    }

    @Override
    public Result chenkStroeNum(String storeNum) {
        //根据仓库名称或者仓库编码查询是否存在
        Store storeByNum = storeMapper.findStoreByNum(storeNum);

        return Result.ok(storeByNum==null);

    }


    //添加仓库的业务方法
    @Override
    public Result saveStore(Store store) {
        int i = storeMapper.insertStore(store);
        if(i>0){
            return Result.ok("仓库添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库添加失败！");
    }

    //修改仓库的业务方法
    @Override
    public Result updateStore(Store store) {
        //根据仓库id修改仓库
        int i = storeMapper.updateStoreById(store);
        if(i>0){
            return Result.ok("仓库修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库修改失败！");
    }

    //删除仓库的业务方法
    @Override
    public Result deleteStore(Integer storeId) {
        //根据仓库id删除仓库
        int i = storeMapper.deleteStoreById(storeId);
        if(i>0){
            return Result.ok("仓库删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库删除失败！");
    }


}
