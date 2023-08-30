package com.pn.service.impl;

import com.alibaba.fastjson.JSON;
import com.pn.entity.Auth;
import com.pn.mapper.AuthMapper;
import com.pn.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljj
 * @date 2023/8/22 20:33
 */
@CacheConfig(cacheNames = "com.pn.service.impl.AuthServiceImpl")
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    //注入Redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    //根据用户id查询用户权限(菜单)树的业务方法
    @Override
    public List<Auth> findAuthTreeById(int userId) {
    //先从redis中查询缓存,查到的是权限(菜单)树List<Auth>转的json串
        String authTreeListJson = redisTemplate.opsForValue().get(userId + ":authTree");
        if (StringUtils.hasText(authTreeListJson)) {//redis中查到缓存
            //将json串转回权限(菜单)树List<Auth>并返回
            List<Auth> authTreeList = JSON.parseArray(authTreeListJson, Auth.class);
            return authTreeList;
        }

        //redis中没有查到缓存,从数据库表中查询所有权限(菜单)
        List<Auth> allAuthList = authMapper.findAuthById(userId);
        //将所有权限(菜单)List<Auth>转成权限(菜单)树List<Auth>
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);
        //将权限(菜单)树List<Auth>转成json串并保存到redis
        redisTemplate.opsForValue().set(userId + ":authTree", JSON.toJSONString(authTreeList));
        //返回权限(菜单)树List<Auth>
        return authTreeList;
    }
    //将所有权限(菜单)转成权限(菜单)树的递归算法
    private List<Auth> allAuthToAuthTree(List<Auth> allAuthList, int parentId) {
        //获取父权限(菜单)id为参数parentId的所有权限(菜单)
        //【parentId最初为0,即最初查的是所有一级权限(菜单)】
        List<Auth> authList = new ArrayList<>();
        for (Auth auth : allAuthList) {
            if (auth.getParentId() == parentId) {
                authList.add(auth);
            }
        }
        //查询List<Auth> authList中每个权限(菜单)的所有子级权限(菜单)
        for (Auth auth : authList) {
            List<Auth> childAuthList = allAuthToAuthTree(allAuthList, auth.getAuthId());
            auth.setChildAuth(childAuthList);
        }
        return authList;
    }

    //查询所有权限(菜单)的方法
    @Cacheable(key = "'all:authTree'")
    @Override
    public List<Auth> findAuthTree() {

        //查询所有菜单
        List<Auth> allAuthList = authMapper.findAllAuth();

        //将所有菜单List<Auth>转换为菜单树List<Auth>
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);

        return authTreeList;
    }
}
