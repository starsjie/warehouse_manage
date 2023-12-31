package com.pn.mapper;

import com.pn.entity.ProductType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljj
 */
@Mapper
public interface ProductTypeMapper {

    //查询所有商品分类的方法
    public List<ProductType> findAllProductType();

    //根据分类编码查询商品分类的方法
    public ProductType findTypeByCode(ProductType productType);

    //添加商品分类的方法
    public int insertProductType(ProductType productType);

    //根据分类id删除分类及其所有子级分类的方法
    public int deleteProductType(Integer typeId);

    //根据分类id修改分类的方法
    public int updateTypeById(ProductType productType);
}
