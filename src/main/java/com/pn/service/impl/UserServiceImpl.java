package com.pn.service.impl;

import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.mapper.UserMapper;
import com.pn.page.Page;
import com.pn.service.UserService;
import com.pn.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ljj
 * @date 2023/8/21 20:32
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //根据用户名查找用户的方法
    @Override
    public User findUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }

    //分页查询用户的业务方法
    @Override
    public Page queryUserPage(Page page, User user) {
        //查询用户总行数
        int userCount = userMapper.selectUserCount(user);

        //分页查询用户
        List<User> userList = userMapper.selectUserPage(page, user);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(userCount);
        page.setResultList(userList);

        return page;
    }

    //添加用户的业务方法
    @Override
    public Result saveUser(User user) {
        //根据用户名查询用户
        User oldUser = userMapper.findUserByCode(user.getUserCode());
        if(oldUser!=null){//用户已存在
            return Result.err(Result.CODE_ERR_BUSINESS, "该用户已存在！");
        }

        //用户不存在,对密码加密,添加用户
        String userPwd = DigestUtil.hmacSign(user.getUserPwd());
        user.setUserPwd(userPwd);

        //执行添加
        int i = userMapper.insertUser(user);
        if (i>0){
            return Result.ok("添加用户成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"添加用户失败");

    }

    //修改用户状态的业务方法
    @Override
    public Result updateUserState(User user) {
        //根据用户id修改用户状态
        int i = userMapper.updateUserState(user);
        if(i>0){
            return Result.ok("修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改失败！");
    }

    //根据用户ids删除用户的业务方法
    @Override
    public Result setUserDeleteByIds(List<Integer> userIdList) {
        int i = userMapper.setUserDeleteByIds(userIdList);
        if (i>0){
            return Result.ok("用户删除成功");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户删除失败！");
    }


    //根据用户id修改用户昵称的业务方法
    @Override
    public Result updateUserName(User user) {
        int i = userMapper.updateNameById(user);
        if (i>0){
            return Result.ok("用户修改成功");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户修改失败！");
    }

    //重置密码的业务方法
    @Override
    public Result resetPwd(Integer userId) {

        //给定密码初始值123456
        String password= DigestUtil.hmacSign("123456");

        //执行方法
        int i = userMapper.updatePwdById(userId, password);
        if (i>0){
            return Result.ok("重置密码成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"重置密码失败！");
    }
}
