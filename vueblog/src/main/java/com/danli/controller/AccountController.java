package com.danli.controller;



import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.danli.common.lang.Result;
import com.danli.common.lang.dto.LoginDto;
import com.danli.entity.User;
import com.danli.service.UserService;
import com.danli.service.impl.UserServiceImpl;
import com.danli.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;


    /**
     * 登录请求处理
     */
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        if (user.getUsername().equals(" ") || user.getPassword().equals(" ")){
            return Result.fail("用户信息不合法");
        }
        user =  userService.Login(user);
        return Result.succ(200,"登录成功",user);
    }

    @CrossOrigin
    @PostMapping("/register")
    public Result insertUser(@RequestBody User user){
        // 注册用户
        boolean b = userService.registerUser(user);
        if (b) {
            return Result.succ(200,"注册成功",b);
        } else {
            return Result.fail("用户名已存在");
        }

    }



//    /**
//     * 登出请求处理
//     */
//    @GetMapping("/logout")
//    @RequiresAuthentication
//    public Result logout() {
//        SecurityUtils.getSubject().logout();
//        return Result.succ("退出成功");
//    }
}
