package com.powernode.config;

//请求白名单配置类

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "gateway.white")
@RefreshScope
public class WhiteUrlsConfig {
    //放行的路径集合
    private List<String> allowUrls;

}
