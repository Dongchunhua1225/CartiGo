package com.powernode.impl;


//项目自己的认证流程

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.powernode.constant.AuthConstants;
import com.powernode.factory.LoginStrategyFactory;
import com.powernode.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //从请求头中获取登录类型
        String loginType = request.getHeader(AuthConstants.LOGIN_TYPE);

        //判断请求来自那个系统
//        if(AuthConstants.SYS_USER_LOGIN.equals(loginType)) {
//            //商城后台管理系统流程
//        } else {
//            //商城用户购物系统
//        }
        //用了策略设计模式之后就不需要if-else这么写了

        //判断请求头中的LoginType是不是我们能识别的
        if(!StringUtils.hasText(loginType)) {
            throw new InternalAuthenticationServiceException("非法登录，登录类型不匹配");
        }

        //通过登录策略工厂获取具体的登录策略对象
        LoginStrategy instance = loginStrategyFactory.getInstance(loginType);

        if (ObjectUtils.isEmpty(instance)) {
            throw new InternalAuthenticationServiceException("非法登录，登录类型不匹配");
        }


        return instance.realLogin(username);

    }
}
