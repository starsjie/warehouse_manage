package com.pn.mapper;

import com.pn.entity.RoleAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/24 20:16
 */
@Mapper
public interface RoleAuthMapper {

    //根据角色id删除角色权限的关系
    public int removeRoleAuthById(Integer roleId);

    //根据角色id查询角色已分配的所有权限(菜单)的id
    public List<Integer> findAuthIdsByRid(Integer roleId);

    //添加角色权限关系
    public int insertRoleAuth(RoleAuth roleAuth);
}
