package com.danli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danli.common.lang.vo.UserInfo;
import com.danli.entity.User;
import com.danli.mapper.UserMapper;
import com.danli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author fanfanli
 * @date 2021-04-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 查询所有用户（只含有部分信息）
     *
     * @return 用户（只含有部分信息）list
     */
    @Override
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> userInfos = userMapper.getUserInfo();
        return userInfos;
    }


    public User Login(User user) {
        //验证用户信息是否合法
        User user1 = userMapper.selectByName(user.getUsername());
        if (user1 == null) {
            throw new SecurityException("用户名或者密码错误");
        }
        if (!user.getPassword().equals(user1.getPassword())) {
            throw new SecurityException("用户名或者密码错误");
        }
        return user1;
    }

    @Override
    public boolean isUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        if (isUsernameExists(user.getUsername())) {
            return false;
        }
        return save(user);
    }
}
