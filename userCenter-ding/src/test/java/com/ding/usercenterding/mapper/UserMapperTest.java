package com.ding.usercenterding.mapper;

import com.ding.usercenterding.model.domain.User;
import com.ding.usercenterding.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Resource
    public UserMapper userMapper;
    @Resource
    public UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("ding");
        user.setUserAccount("123");
        user.setAvatarUrl("https://pic.code-nav.cn/user_avatar/1802608880942690305/thumbnail/seYSzUXS-006HYX0kgy1hcy2t4q0xaj335s1s0kjl.jpg");
        user.setGender(0);
        user.setUserPassword("xxxx");
        user.setPhone("12313");
        user.setEmail("123");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setPlaneCode("132");
        int result = userMapper.insert(user);
        Assertions.assertEquals(1,result);
    }


    @Test
    void testUserRegister(){
        String userAccount = "ding";
        String userPassword = "";
        String checkPassword = "12345678";
        Long res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);
        userAccount ="di";
        userPassword ="1234";
        res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);
        userAccount = "ding@fei";
        userPassword ="12345678";
        checkPassword = "12345678";
        res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);
        userAccount ="ding";
        userPassword = "12345678";
        checkPassword = "123456789";
        res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);
        userAccount = "123";
        userPassword = "12345678";
        res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);
        userAccount = "zhangsan";
        userPassword = "12345678";
        res = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,res);









    }



}