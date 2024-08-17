package com.ding.usercenterding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ding.usercenterding.model.domain.User;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author DiangF
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-07-26 20:32:02
*/
public interface UserService extends IService<User> {

    /**
     *
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return  用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
    /**
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return  用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 查找用户
     * @param username
     * @param request
     * @return
     */
    List<User> searchUser(String username, HttpServletRequest request);



    /**
     * 删除用户
     * @param id
     * @param request
     */

    boolean deleteUser(long id, HttpServletRequest request);

    /**
     *
     * 用户脱敏
     * @param orginUser
     * @return
     */
    User getSafetyUser(User orginUser);

    /**
     *
     * @return  获取当前登录用户信息
      */
    User getCurrentUser(HttpServletRequest request);
}
