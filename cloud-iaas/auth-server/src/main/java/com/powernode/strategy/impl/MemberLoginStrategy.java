package com.powernode.strategy.impl;

//商城购物系统具体实现策略

import com.powernode.constant.AuthConstants;
import com.powernode.strategy.LoginStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(AuthConstants.MEMBER_LOGIN)
public class MemberLoginStrategy implements LoginStrategy {

    @Override
    public UserDetails realLogin(String username) {
        return null;
    }
}
