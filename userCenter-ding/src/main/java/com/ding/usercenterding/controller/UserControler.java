package com.ding.usercenterding.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ding.usercenterding.mapper.UserMapper;
import com.ding.usercenterding.model.domain.User;
import com.ding.usercenterding.model.domain.request.UserLoginRequest;
import com.ding.usercenterding.model.domain.request.UserRegisterRequest;
import com.ding.usercenterding.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author ding
 */

@RestController     //适用于编写restful 风格的api，返回默认值为Json类型
@RequestMapping("/user")
public class UserControler {
    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     *  用户注册
     * @param userRegisterRequest
     * @return  用户的id
     */

    @PostMapping("/register")
   public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        //@RequestBody 代表的是让SpringMVC框架与前端传过来的参数做一个关联
        if(userRegisterRequest == null){
                return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //前端校验传过来的参数 不涉及业务逻辑校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)) {
            return null;
        }

        return  userService.userRegister(userAccount, userPassword, checkPassword);

    }

    /**
     * 用户登录
     * @param userLoginRequest  采用一个类封装所有请求参数
     * @param request   请求参数 保存登录态
     * @return
     */

    @PostMapping("/login")
    public User userLoginr(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        //@RequestBody 代表的是让SpringMVC框架与前端传过来的参数做一个关联
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //前端校验传过来的参数 不涉及业务逻辑校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }


    @GetMapping("/current")
    public User getcCurrentUser(HttpServletRequest request){
       return userService.getCurrentUser(request);
    }





    /**
     * 查找用户
     * @param username
     * @param request
     * @return
     */

    @GetMapping("/search")
    List<User> searchUsers(String username,HttpServletRequest request){
        return userService.searchUser(username,request);
    }

    /**
     *
     * 删除用户
     */

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
            System.out.println(id);
            return userService.deleteUser(id,request);
    }














}
