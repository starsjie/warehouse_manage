package com.pn.service;

import com.pn.entity.Auth;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/22 20:32
 */

public interface AuthService {

    //根据用户id查询用户权限(菜单)树的业务方法
    public List<Auth> findAuthTreeById(int userId);

    //查询所有权限(菜单)的树方法
    public List<Auth> findAuthTree();

}
