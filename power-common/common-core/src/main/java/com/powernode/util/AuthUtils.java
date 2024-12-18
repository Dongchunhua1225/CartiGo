package com.powernode.util;

import com.powernode.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

//认证授权工具类
public class AuthUtils {

    //获取Security容器中的认证用户对象
    public static SecurityUser getLoginUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    //获取Security容器中认证用户对象标识
    public static Long getLoginUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取Security容器中认证用户对象的操作权限集合
     * @return
     */
    public static Set<String> getLoginUserPerms() {
        return getLoginUser().getPerms();
    }

    //获取security容器中认证用户对象的openid
    public static String getMemberOpenId(){
        return getLoginUser().getOpenid();
    }
}
