package com.powernode.strategy;

import org.springframework.security.core.userdetails.UserDetails;

//登陆策略接口
//在这里面定义真正实现登录策略的方法
public interface LoginStrategy {

    //真正处理登录的方法
    UserDetails realLogin(String username);
}
