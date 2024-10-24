package com.powernode.strategy.impl;

//商城后台管理系统登录策略具体实现类

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.constant.AuthConstants;
import com.powernode.domain.LoginSysUser;
import com.powernode.mapper.LoginSysUserMapper;
import com.powernode.model.SecurityUser;
import com.powernode.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service(AuthConstants.SYS_USER_LOGIN)
public class SysUserLoginStrategy implements LoginStrategy {
    @Autowired
    private LoginSysUserMapper loginSysUserMapper;
    @Override
    public UserDetails realLogin(String username) {
        //根据用户名称查询用户对象
        //LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryChainWrapper<LoginSysUser>().eq(LoginSysUser::getUsername, username)); //用lambda的好处就是可以“LoginSysUser::getUsername” 可以使用泛型
//        LambdaQueryWrapper<LoginSysUser> eq = Wrappers.<LoginSysUser>lambdaQuery()
//                .eq(LoginSysUser::getUsername, username);
//        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(eq);
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>()
                .eq(LoginSysUser::getUsername, username)
        );

        //这样就不用手动去数据库看具体的字段名称

        //判断这个对象是否存在
        if(ObjectUtil.isNotNull(loginSysUser)) {
            //根据用户表示查询用户的权限集合
            Set<String> perms = loginSysUserMapper.selectPermsByUserId(loginSysUser.getUserId());
            //创建安全用户对象SecurityUser
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(loginSysUser.getUserId());
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setShopId(loginSysUser.getShopId());
            securityUser.setStatus(loginSysUser.getStatus());
            securityUser.setLoginType(AuthConstants.SYS_USER_LOGIN);
            //判断用户权限是否有值
            if(CollectionUtil.isNotEmpty(perms) && perms.size() != 0) {
                securityUser.setPerms(perms);
            }
            return securityUser;
        }

        return null;
    }
}
