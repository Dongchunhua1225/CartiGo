package com.powernode.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@RefreshScope
public class AliyunOSSConfig {
    //访问地址
    private String endpoint;

    //bucket名称
    private String bucketName;

    //访问id
    private String accessKeyId;

    //访问密钥
    private String accessKeySecret;
}