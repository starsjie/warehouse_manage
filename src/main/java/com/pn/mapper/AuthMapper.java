package com.pn.mapper;

import com.pn.entity.Auth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/22 20:20
 */
@Mapper
public interface AuthMapper {

    //根据用户userId查询用户所有权限(菜单)的方法
    public List<Auth> findAuthById(int userId);

    //查询所有权限(菜单)的方法
    public List<Auth> findAllAuth();
    

}
