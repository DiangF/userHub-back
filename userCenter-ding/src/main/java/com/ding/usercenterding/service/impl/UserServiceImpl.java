package com.ding.usercenterding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ding.usercenterding.model.domain.User;
import com.ding.usercenterding.service.UserService;
import com.ding.usercenterding.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ding.usercenterding.constant.UserConstant.ADMIN_ROLR;
import static com.ding.usercenterding.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author DiangF
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-07-26 20:32:02
 *
*/


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * Session记录用户登陆状态
     */

    /**
     * 盐 混淆密码加密
     */

    @Resource
    public UserMapper userMapper;
    private static final String SALT = "ding";

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 用户注册账户
     */

    @Override
    public long userRegister(String userAccount,String userPassword,String checkPassword) {
        //用户注册逻辑
        /**1. 校验用户账户，密码、校验密码 是否符合要求**/
        //1.1非空校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        //1.2账户长度不小于4位 ，密码长度不小于8位
        if(userAccount.length() <4 ){
            return -1;
        }
        //1.3 密码长度不小于8位
        if(userPassword.length() <8 && checkPassword.length() < 8 ){
            return -1;
        }

        //1.4 账户不包含特殊字符
        String Regex = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(Regex).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }
        // 1.5 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        //1.6 账户中不能重复注册
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //2. 对密码进行加密操作（）
        String entryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        // 3. 插入数据
        user.setUserAccount(userAccount);
        user.setUserPassword(entryPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }
        System.out.println("新注册用户的id为"+user.getId());
        return user.getId();
    }

    /**
     *
     * 用户登录
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request   进行登录态保存
     * @return
     */

    public User userLogin(String userAccount, String userPassword, HttpServletRequest request){
        //1.1非空校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        //1.2账户长度不小于4位 ，密码长度不小于8位
        if(userAccount.length() <4 ){
            return null;
        }
        //1.3 密码长度不小于8位
        if(userPassword.length() <8 ){
            return null;
        }

        //1.4 账户不包含特殊字符
        String Regex = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(Regex).matcher(userAccount);
        if(matcher.find()){
            return null;
        }

        //2. 校验数据库密码是否正确，和数据库中加密密文进行对比
        String entryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //1.6 账户中不能重复注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",entryPassword);
        User  user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed,userPassword Cannot match userAccount or userAccount is inCorrect");
        }
        // 3. 前端返回信息进行脱敏处理 隐藏敏感信息
        User safetyUser = getSafetyUser(user);
//        4. 记录用户登录状态，将其存储到服务器上
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;

    }


    @Override
    public List<User> searchUser(String username, HttpServletRequest request) {
        //鉴权
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
           queryWrapper.like("userAccount",username);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(user->
                getSafetyUser(user)).collect(Collectors.toList());
    }



    /**
     * 删除用户
     * @param id
     * @param request
     */
    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        if(!isAdmin(request) || id<0){
            return false;
        }
        return userMapper.deleteById(id)>0;

    }



    /**
     * 权限管理  鉴权
     * @return
     */

    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user!=null &&user.getUserRole() == ADMIN_ROLR;
    }

    /**
     * 数据脱敏处理
     * @param orginUser
     * @return
     */
    @Override
    public User getSafetyUser(User orginUser) {

        /*Sercvice  也需要进行用户校验*/
        if(orginUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(orginUser.getId());
        safetyUser.setUsername(orginUser.getUserAccount());
        safetyUser.setUserAccount(orginUser.getUserAccount());
        safetyUser.setAvatarUrl(orginUser.getAvatarUrl());
        safetyUser.setGender(orginUser.getGender());
        safetyUser.setPhone(orginUser.getPhone());
        safetyUser.setEmail(orginUser.getEmail());
        safetyUser.setUserStatus(orginUser.getUserStatus());
        safetyUser.setCreateTime(orginUser.getCreateTime());
        safetyUser.setUserRole(orginUser.getUserRole());
        return safetyUser;
    }


    /**
     *
     *
     * @param request
     * @return  获取当前用户信息
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Object userObject   = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null) {
            return null;
        }
        //TODO 校验用户是否合法
        Long userId = user.getId();
        User currentUser = userMapper.selectById(userId);
        return getSafetyUser(currentUser);
    }


}




