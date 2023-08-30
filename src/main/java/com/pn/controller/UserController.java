package com.pn.controller;

import com.pn.dto.AssignRoleDto;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import com.pn.service.AuthService;
import com.pn.service.RoleService;
import com.pn.service.UserService;
import com.pn.utils.CurrentUser;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ljj
 * @date 2023/8/23 19:11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //注入AuthService
    @Autowired
    private AuthService authService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //注入UserService
    @Autowired
    private UserService userService;

    //注入RoleService
    @Autowired
    private RoleService roleService;

    /**
     * 分页查询用户的url接口/user/user-list
     *
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     * 参数User对象用于接收请求参数用户名userCode、用户类型userType、用户状态userState;
     *
     * 返回值Result对象向客户端响应组装了所有分页信息的Page对象;
     */
    @RequestMapping("/user-list")
    public Result userListPage(Page page, User user){
        //执行业务
        page = userService.queryUserPage(page, user);
        //响应
        return Result.ok(page);
    }
    /**
     * 添加用户的url接口/user/addUser
     *
     * @RequestBody User user将添加的用户信息的json串数据封装到参数User对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user,
                          @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);

        //获取当前登录的用户id,即创建新用户的用户id
        int createBy = currentUser.getUserId();
        user.setCreateBy(createBy);

        //执行业务
        Result result = userService.saveUser(user);
        return result;
    }

    /**
     * 修改用户状态的url接口/user/updateState
     *
     * @RequestBody User user将客户端传递的json数据封装到参数User对象中;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/updateState")
    public Result updateUserState(@RequestBody User user,
                                  @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即修改用户的用户id
        int updateBy = currentUser.getUserId();

        //设置修改用户的用户id和修改时间
        user.setUpdateBy(updateBy);
        user.setUpdateTime(new Date());

        //执行业务
        Result result = userService.updateUserState(user);

        //响应
        return result;
    }

    /**
     * 查询用户已分配的角色的url接口/user/user-role-list/{userId}
     */
    @RequestMapping("/user-role-list/{userId}")
    public Result userRoleList(@PathVariable Integer userId){
        //执行业务
        List<Role> roleList = roleService.queryRolesByUserId(userId);
        //响应
        return Result.ok(roleList);
    }

    /**
     * 给用户分配角色的url接口/user/assignRole
     *
     * @RequestBody AssignRoleDto assignRoleDto将请求传递的json数据
     * 封装到参数AssignRoleDto对象中;
     */
    @RequestMapping("/assignRole")
    public Result assignRole(@RequestBody AssignRoleDto assignRoleDto){
        //执行业务
        roleService.assignRole(assignRoleDto);
        //响应
        return Result.ok("分配角色成功！");
    }

    /**
     * 删除用户的url接口/user/deleteUser/{userId}
     */
    @RequestMapping("/deleteUser/{userId}")
    public Result deleteUserById(@PathVariable Integer userId){
        //执行业务
        Result result = userService.setUserDeleteByIds(Arrays.asList(userId));
        //响应
        return result;
    }

    /**
     * 根据ids批量删除用户的url接口/user/deleteUserList
     */
    @RequestMapping("/deleteUserList")
    public Result deleteUserByIds(@RequestBody List<Integer> userIdList){
        //执行业务
        Result result = userService.setUserDeleteByIds(userIdList);
        //响应
        return result;
    }

    /**
     * 修改用户的url接口/user/updateUser
     *
     * @RequestBody User user将请求传递的json数据封装到参数User对象;
     * @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
     * 将请求头Token的值即客户端归还的token赋值给参数变量token;
     */
    @RequestMapping("/updateUser")
    public Result updateUser(@RequestBody User user,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id -- 修改用户的用户id
        int updateBy = currentUser.getUserId();

        user.setUpdateBy(updateBy);

        //执行业务
        Result result = userService.updateUserName(user);

        //响应
        return result;
    }

    /**
     * 重置密码的url接口/user/updatePwd/{userId}
     */
    @RequestMapping("/updatePwd/{userId}")
    public Result resetPassWord(@PathVariable Integer userId){
        //执行业务
        Result result = userService.resetPwd(userId);
        //响应
        return result;
    }

    /**
     * 导出数据的url接口/user/exportTable
     *
     * 参数Page对象用于接收请求参数页码pageNum、每页行数pageSize;
     *
     * 返回值Result对象向客户端响应组装了当前页数据的List;
     */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page, User user){
        //分页查询仓库
        page = userService.queryUserPage(page, user);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}
