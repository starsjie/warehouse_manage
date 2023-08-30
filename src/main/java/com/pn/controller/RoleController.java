package com.pn.controller;

import com.pn.dto.AssignAuthDto;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import com.pn.service.AuthService;
import com.pn.service.RoleService;
import com.pn.utils.CurrentUser;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author ljj
 * @date 2023/8/23 20:31
 */

@RequestMapping("/role")
@RestController
public class RoleController {
    //注入RoleService
    @Autowired
    private RoleService roleService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //注入AuthService
    @Autowired
    private AuthService authService;

    /**
     * 查询所有角色的url接口role/role-list
     */
    @RequestMapping("/role-list")
    public Result queryAllRole() {
        //执行业务
        List<Role> roleList = roleService.getAllRole();
        //响应
        return Result.ok(roleList);
    }

    /**
     * 分页查询角色的url接口/role/role-page-list
     * <p>
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数Role对象用于接收请求参数角色名roleName、角色代码roleCode、角色状态roleState;
     * <p>
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/role-page-list")
    public Result roleListPage(Page page, Role role) {
        //执行业务
        page = roleService.queryRolePage(page, role);

        //响应
        return Result.ok(page);
    }

    /**
     * 添加角色的url接口/role/role-add
     *
     * @RequestBody Role role将添加的角色信息的json串数据封装到参数Role对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/role-add")
    public Result addRole(@RequestBody Role role,
                          @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即创建新角色的用户id
        int createBy = currentUser.getUserId();
        role.setCreateBy(createBy);

        //执行业务
        Result result = roleService.saveRole(role);
        return result;
    }

    /**
     * 修改角色状态的url接口/role/role-state-update
     *
     * @RequestBody Role role将客户端传递的json数据封装到参数Role对象中;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role,
                                  @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改角色的用户id
        int updateBy = currentUser.getUserId();

        //设置修改角色的用户id和修改时间
        role.setUpdateBy(updateBy);
        role.setUpdateTime(new Date());

        //执行业务
        Result result = roleService.updateRoleState(role);

        //响应
        return result;
    }

    /**
     * 删除角色的url接口/role/role-delete/{roleId}
     */
    @RequestMapping("/role-delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId){
        //执行业务
        Result result = roleService.deleteRole(roleId);
        //响应
        return result;
    }

    /**
     * 查询角色已分配的权限(菜单)的url接口/role/role-auth
     *
     * Integer roleId将请求参数roleId赋值给请求处理方法参数roleId;
     *
     * 返回值Result对象向客户端响应组装了给角色分配的所有权限(菜单)id的List<Integer>;
     */
    @RequestMapping("/role-auth")
    public Result queryRoleAuth(Integer roleId){
        //执行业务
        List<Integer> authIdList = roleService.queryRoleAuthIds(roleId);
        //响应
        return Result.ok(authIdList);
    }

    /**
     * 给角色分配权限(菜单)的url接口/role/auth-grant
     *
     * @RequestBody AssignAuthDto assignAuthDto将请求传递的json数据
     * 封装到参数AssignAuthDto对象中;
     */
    @RequestMapping("/auth-grant")
    public Result assignAuth(@RequestBody AssignAuthDto assignAuthDto){
        //执行业务
        roleService.saveRoleAuth(assignAuthDto);
        //响应
        return Result.ok("权限分配成功！");
    }

    /**
     * 修改角色的url接口/role/role-update
     *
     * @RequestBody Role roler将请求传递的json数据封装到参数Role对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/role-update")
    public Result updateRole(@RequestBody Role role,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id -- 修改角色的用户id
        int updateBy = currentUser.getUserId();

        role.setUpdateBy(updateBy);

        //执行业务
        Result result = roleService.updateRoleDesc(role);

        //响应
        return result;
    }

    /**
     * 导出数据的url接口/role/exportTable
     *
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     *
     * 返回值Result对象向客户端响应组装了当前页数据的List;
     */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page, Role role){
        //分页查询仓库
        page = roleService.queryRolePage(page, role);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}
