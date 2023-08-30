package com.pn.mapper;

import com.pn.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ljj
 * @date 2023/8/24 11:10
 */
@Mapper
public interface UserRoleMapper {

    //根据用户id删除给用户已分配的所有角色
    public int removeUserRoleById(Integer userId);

    //添加用户角色关系的方法
    public int insertUserRole(UserRole userRole);


}
