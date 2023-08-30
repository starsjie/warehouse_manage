package com.pn.service.impl;

import com.pn.entity.ProductType;
import com.pn.entity.Result;
import com.pn.mapper.ProductTypeMapper;
import com.pn.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljj
 * @create 2023/8/26 9:08
 */
//指定缓存的名称即键的前缀,一般是@CacheConfig标注的类的全类名
@CacheConfig(cacheNames = "com.pn.service.impl.ProductTypeServiceImpl")
@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    //注入ProductTypeMapper
    @Autowired
    private ProductTypeMapper productTypeMapper;

    /*
         查询所有商品分类树的业务方法
        */
    //对查询到的所有商品分类树进行缓存,缓存到redis的键为all:typeTree
    @Cacheable(key = "'all:typeTree'")
    @Override
    public List<ProductType> allProductTypeTree() {

        //查询所有商品分类
        List<ProductType> allProductTypeList = productTypeMapper.findAllProductType();
        //将所有商品分类List<ProductType>转成商品分类树List<ProductType>
        List<ProductType> typeTreeList = allTypeToTypeTree(allProductTypeList, 0);
        //返回商品分类树List<ProductType>
        return typeTreeList;
    }
    //将查询到的所有商品分类List<ProductType>转成商品分类树List<ProductType>的算法
    private List<ProductType> allTypeToTypeTree(List<ProductType> allTypeList, Integer parentId){
        //拿的所有一级分类
        List<ProductType> firstList = new ArrayList<>();
        for (ProductType productType : allTypeList) {
            if(productType.getParentId().equals(parentId)){
                firstList.add(productType);
            }
        }
        //递归  查出每个一级分类下的二级分类
        for (ProductType productType : firstList) {
            List<ProductType> childTypeList = allTypeToTypeTree(allTypeList, productType.getTypeId());
            productType.setChildProductCategory(childTypeList);
        }

        return firstList;
    }

    //校验分类编码是否已存在的业务方法
    @Override
    public Result queryTypeByCode(String typeCode) {

        ProductType productType= new ProductType();
        productType.setTypeCode(typeCode);
        //根据分类编码查询商品分类
        ProductType prodType = productTypeMapper.findTypeByCode(productType);

        return Result.ok(prodType==null);
    }

    /**
     * 添加商品分类的业务方法
     *@CacheEvict(key = "'all:typeTree'")清除所有商品分类树的缓存;
     */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result saveProductType(ProductType productType) {
        ProductType proType=new ProductType();
        proType.setTypeName(productType.getTypeName());
        ProductType prod = productTypeMapper.findTypeByCode(proType);
        if (prod !=null){
            return Result.err(Result.CODE_ERR_BUSINESS,"商品分类名称已存在");
        }
        //添加商品分类
        int i = productTypeMapper.insertProductType(productType);
        if(i>0){
            return Result.ok("商品分类添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品分类添加失败！");
    }

    /**
      *删除商品分类的业务方法
      *@CacheEvict(key = "'all:typeTree'")清除所有商品分类树的缓存;
     */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result removeProductType(Integer typeId) {
        //根据分类id删除分类及其所有子级分类
        int i = productTypeMapper.deleteProductType(typeId);
        if(i>0){
            return Result.ok("商品分类删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品分类删除失败！");
    }

    /**
     *修改商品分类的业务方法
     * @CacheEvict(key = "'all:typeTree'")清除所有商品分类树的缓存;
     */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result updateProductType(ProductType productType) {

        ProductType proType=new ProductType();
        proType.setTypeName(productType.getTypeName());
        ProductType prod = productTypeMapper.findTypeByCode(proType);
        if (prod !=null&&!prod.getTypeId().equals(prod.getTypeId())){
            return Result.err(Result.CODE_ERR_BUSINESS,"商品分类名称已存在");
        }
        //根据分类id修改分类
        int i = productTypeMapper.updateTypeById(productType);
        if(i>0){
            return Result.ok("商品分类修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品分类修改失败！");
    }
}
