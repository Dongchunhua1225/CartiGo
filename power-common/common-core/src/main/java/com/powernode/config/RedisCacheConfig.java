package com.powernode.config;

//Redis缓存配置类

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        //创建一个redis缓存配置类
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                // 统一设置redis中值的默认过期时间（7天）
                .entryTtl(Duration.ofDays(7))
                // redis的value值禁止使用空值
                .disableCachingNullValues();
//                // 变双冒号为单冒号
//                .computePrefixWith(name -> name + ":")
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
//                .disableCachingNullValues();
//        return RedisCacheManager.RedisCacheManagerBuilder
//                .fromConnectionFactory(lettuceConnectionFactory)
//                .cacheDefaults(config)
//                .transactionAware()
//                .build();

        return redisCacheConfiguration;
    }
}
