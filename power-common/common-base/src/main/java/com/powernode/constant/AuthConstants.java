package com.powernode.constant;

//认证授权常量类
//用接口定义常量的好处：可以直接用，不需要通过接口名
public interface AuthConstants {
    //请求头中的token前缀KEY
    String AUTHORIZATION = "Authorization";

    //token值的前缀
    String BEARER = "bearer";

    //token值存放在redis中的前缀
    String LOGIN_TOKEN_PREFIX = "login_token";
}
