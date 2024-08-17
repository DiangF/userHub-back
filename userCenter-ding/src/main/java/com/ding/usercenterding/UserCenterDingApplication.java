package com.ding.usercenterding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ding.usercenterding.mapper")
    public class UserCenterDingApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterDingApplication.class, args);
    }

}
