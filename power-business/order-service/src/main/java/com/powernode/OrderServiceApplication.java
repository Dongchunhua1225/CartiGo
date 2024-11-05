package com.powernode;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//订单业务模块启动类
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients //远程调用 开启feign客户端
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

//    @Bean
//    public Snowflake snowflake() {
//        return new Snowflake(0, 0);
//    }
}
