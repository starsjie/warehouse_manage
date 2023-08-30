package com.pn.mapper;

import com.pn.entity.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljj
 */
@Mapper
public interface BrandMapper {

    //查询所有品牌的方法
    public List<Brand> findAllBrand();
}
