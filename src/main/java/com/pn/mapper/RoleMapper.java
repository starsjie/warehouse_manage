package com.pn.mapper;

import com.pn.entity.Role;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/23 20:35
 */
@Mapper
public interface RoleMapper {

    //查询状态正常的所有角色的方法
    public List<Role> findAllRole();

    //根据用户id查询用户已分配的角色
    public List<Role> findRolesByUserId(Integer userId);

    //根据角色名称查询角色id
    public int findRoleIdByName(String roleName);

    //查询角色总行数的方法
    public int selectRoleCount(Role role);

    //分页查询角色的方法
    public List<Role> selectRolePage(@Param("page") Page page, @Param("role") Role role);

    //根据角色名称或者角色代码查询角色的方法
    public Role findRoleByNameOrCode(String roleName, String roleCode);

    //添加角色的方法
    public int insertRole(Role role);

    //根据角色id修改角色状态的方法
    public int updateRoleState(Role role);

    //根据角色id删除角色的方法
    public int deleteRoleById(Integer roleId);

    //根据角色id修改角色描述的方法
    public int updateDescById(Role role);
}
