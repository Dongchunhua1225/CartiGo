package com.powernode.factory;

//登录策略工厂类

import com.powernode.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginStrategyFactory {
    @Autowired
    private Map<String, LoginStrategy> loginStrategyMap = new HashMap<>();


    //根据用户登录类型类获取具体的登录策略
    public LoginStrategy getInstance(String loginType) {
        return loginStrategyMap.get(loginType);
    }
}
