package com.ding.usercenterding.model.domain.request;

import lombok.Data;
import org.springframework.dao.DataAccessException;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -3973058459574665071L;

    private String userAccount;
    private String   userPassword;


}
