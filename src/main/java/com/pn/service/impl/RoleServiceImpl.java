package com.pn.service.impl;

import com.pn.dto.AssignAuthDto;
import com.pn.dto.AssignRoleDto;
import com.pn.entity.*;
import com.pn.mapper.AuthMapper;
import com.pn.mapper.RoleAuthMapper;
import com.pn.mapper.RoleMapper;
import com.pn.mapper.UserRoleMapper;
import com.pn.page.Page;
import com.pn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/23 20:38
 */
//2.指定redis缓存名称（全路径）
@CacheConfig(cacheNames = "com.pn.service.impl.RoleServiceImpl")
@Service
public class RoleServiceImpl implements RoleService {

    //注入RoleMapper
    @Autowired
    private RoleMapper roleMapper;

    //注入UserRoleMapper
    @Autowired
    private UserRoleMapper userRoleMapper;

    //注入RoleAuthMapper
    @Autowired
    private RoleAuthMapper roleAuthMapper;

    //注入AuthMapper
    @Autowired
    private AuthMapper authMapper;

    //查询所有角色的业务方法
    // 3.在方法上指定key   @Cacheable
    @Cacheable(key = "'all:role'")
    @Override
    public List<Role> getAllRole() {
        //查询状态正常的所有角色
        return roleMapper.findAllRole();
    }

    //查询用户已分配的角色的业务方法
    @Override
    public List<Role> queryRolesByUserId(Integer userId) {
        return roleMapper.findRolesByUserId(userId);
    }


    //给用户分配角色的业务方法
    @Transactional//事务处理
    @Override
    public void assignRole(AssignRoleDto assignRoleDto) {
        //拿到用户id
        userRoleMapper.removeUserRoleById(assignRoleDto.getUserId());

        //拿到给用户分配的所有角色名
        List<String> roleNameList = assignRoleDto.getRoleCheckList();
        //循环添加用户角色关系
        for (String roleName : roleNameList) {
            int roleId = roleMapper.findRoleIdByName(roleName);
            UserRole userRole=new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(assignRoleDto.getUserId());
            userRoleMapper.insertUserRole(userRole);
        }
    }

    //分页查询角色的业务方法
    @Override
    public Page queryRolePage(Page page, Role role) {
        //查询角色总行数
        int roleCount = roleMapper.selectRoleCount(role);

        //分页查询角色
        List<Role> roleList = roleMapper.selectRolePage(page, role);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(roleCount);
        page.setResultList(roleList);

        return page;
    }

    //添加角色的业务方法
    @CacheEvict(key = "'all:role'") //操作事务成功清除redis缓存
    @Override
    public Result saveRole(Role role) {
        //根据角色名或角色代码查询角色
        Role oldRole = roleMapper.findRoleByNameOrCode(role.getRoleName(), role.getRoleCode());
        //角色已存在
        if(oldRole!=null){
            return Result.err(Result.CODE_ERR_BUSINESS, "该角色已存在！");
        }
        //角色不存在,添加角色
        int i = roleMapper.insertRole(role);
        if (i>0){
            return Result.ok("添加角色成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"添加角色失败！");
    }

    //修改角色状态的业务方法
    @CacheEvict(key = "'all:role'") //操作事务成功清除redis缓存
    @Override
    public Result updateRoleState(Role role) {
        //根据角色id修改角色状态
        int i = roleMapper.updateRoleState(role);
        if(i>0){
            return Result.ok("角色修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色修改失败！");
    }

    //删除角色的业务方法
    @CacheEvict(key = "'all:role'")
    @Transactional
    @Override
    public Result deleteRole(Integer roleId) {
        //根据角色id删除角色
        int i = roleMapper.deleteRoleById(roleId);
        if(i>0){
            //根据角色id删除给角色已分配的所有权限(菜单)
            roleAuthMapper.removeRoleAuthById(roleId);
            return Result.ok("角色删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色删除失败！");
    }

    //查询角色已分配的权限(菜单)id的业务方法
    @Override
    public List<Integer> queryRoleAuthIds(Integer roleId) {
        //根据角色id查询角色已分配的所有权限(菜单)的id
      return roleAuthMapper.findAuthIdsByRid(roleId);
    }

    //给角色分配权限的业务方法
    @Override
    public void saveRoleAuth(AssignAuthDto assignAuthDto) {

        //删除角色之前的权限
        roleAuthMapper.removeRoleAuthById(assignAuthDto.getRoleId());

        //添加角色权限关系
        List<Integer> authIdsList = assignAuthDto.getAuthIds();
        for (Integer authId : authIdsList) {
            RoleAuth roleAuth =new RoleAuth();
            roleAuth.setRoleId(assignAuthDto.getRoleId());
            roleAuth.setAuthId(authId);
            roleAuthMapper.insertRoleAuth(roleAuth);
        }
    }

    //修改角色描述的业务方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result updateRoleDesc(Role role) {

        //根据角色id修改角色描述
        int i = roleMapper.updateDescById(role);
        if(i>0){
            return Result.ok("角色修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色修改失败！");
    }
}
