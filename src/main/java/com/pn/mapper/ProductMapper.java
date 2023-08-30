package com.pn.mapper;

import com.pn.entity.Product;
import com.pn.entity.ProductType;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljj
 */
@Mapper
public interface ProductMapper {

    //查询商品总行数的方法
    public int selectProductCount(Product product);

    //分页查询商品的方法
    public List<Product> selectProductPage(@Param("page") Page page,
                                           @Param("product") Product product);

    //根据型号查询商品的方法
    public Product findProductByNum(String productNum);

    //添加商品的方法
    public int insertProduct(Product product);

    //根据商品id修改商品的上下架状态
    public int updateStateById(Product product);

    //根据商品id删除商品的方法
    public int deleteProductById(List<Integer> productIdList);

    //根据商品id修改商品的方法
    public int updateProductById(Product product);

    //根据商品id增加商品库存的方法
    public int addInventById(Integer productId, Integer invent);

    //根据商品id查询商品的方法
    public Product selectProductById(Integer productId);
}
