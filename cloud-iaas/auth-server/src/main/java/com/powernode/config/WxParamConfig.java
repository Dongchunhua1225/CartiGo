package com.powernode.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

//微信小程序配置类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "wx")
@RefreshScope
public class WxParamConfig {

    //微信小程序appid
    private String appid;

    //微信小程序密钥secret
    private String secret;

    //登录凭证校验接口url地址
    private String url;

    //wx小程序登陆密码（固定：“WECHAT”）
    private String pwd;
}
