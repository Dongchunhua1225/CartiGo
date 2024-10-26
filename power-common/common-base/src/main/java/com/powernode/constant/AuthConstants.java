package com.powernode.constant;

//认证授权常量类
//用接口定义常量的好处：可以直接用，不需要通过接口名
public interface AuthConstants {
    //请求头中的token前缀KEY
    String AUTHORIZATION = "Authorization";

    //token值的前缀
    String BEARER = "bearer ";

    //token值存放在redis中的前缀
    String LOGIN_TOKEN_PREFIX = "login_token";

    //登录url
    String LOGIN_URL = "/doLogin";

    //登出url
    String LOGOUT_URL = "/doLogout";

    //登录类型
    String LOGIN_TYPE = "loginType";

    //登录类型值：商城后台管理系统用户
    String SYS_USER_LOGIN = "sysUserLogin";

    //登录类型值：商城用户购物系统
    String MEMBER_LOGIN = "memberLogin";

    //token的有效时间，单位“秒”，4个小时
    Long TOKEN_TIME = 14400L;

    //token的阈值：3600s，一个小时
    Long TOKEN_EXPIRE_THRESHOLD_TIME = 60*60L;
}
