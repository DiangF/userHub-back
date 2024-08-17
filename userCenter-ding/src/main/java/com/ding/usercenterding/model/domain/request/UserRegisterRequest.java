package com.ding.usercenterding.model.domain.request;

import lombok.Data;

import java.io.Serializable;


/**
 *
 * 用户的请求体
 * @author ding
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 134527220073569129L;
    private String userAccount;
    private String   userPassword;
    private String  checkPassword;


}
