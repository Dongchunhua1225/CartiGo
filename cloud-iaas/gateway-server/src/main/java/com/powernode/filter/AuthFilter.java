package com.powernode.filter;

//全局token过滤器
//前后端约定好的令牌存放位置：请求头的Authorization Bearer token

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.config.WhiteUrlsConfig;
import com.powernode.constant.AuthConstants;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

//import org.springframework.http.server.ServerHttpResponse;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    //校验token
//        1.获取请求路径
//        2.判断请求路径是否可以放行
//            - 若不需要身份验证 --> 放行
//            - 若需要延伸分 --> 不放行
    @Autowired
    private WhiteUrlsConfig whiteUrlsConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求对象
         ServerHttpRequest req = exchange.getRequest();

         //获取请求路径
        String path = req.getPath().toString();

        //判断当前的path是否需要放行
            //在配置文件bootstrap.yml制作一个白名单
        if(whiteUrlsConfig.getAllowUrls().contains(path)) {
            return chain.filter(exchange);
        }

        //如果path不在白名单中的话, 进行身份验证
        //在约定好的位置获取Auth的值，值的格式为：bearer token
        String authorizationValue = req.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        if(StringUtils.hasText(authorizationValue)) {
            //从Authorization中获取token
            String tokenValue = authorizationValue.replaceFirst(AuthConstants.BEARER, "");

            //判断token是否有值，并且这个token还得在redis存在
            if(StringUtils.hasText(tokenValue) && stringRedisTemplate.hasKey(AuthConstants.LOGIN_TOKEN_PREFIX + tokenValue)) {
                //身份验证没问题
                return chain.filter(exchange);
            }
        }

        //流程如果走到这里，说明token验证失败或请求不合法，etc
        log.error("拦截非法请求，时间{}， 请求API路径{}", new Date(), path);

        //获取相应对象
        ServerHttpResponse res = exchange.getResponse();

        //设置响应头信息：返回的res的格式信息
        res.getHeaders().set(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);

        //设置返回响应的消息，告诉前端有什么问题就得了
        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);

        //创建一个Object Mapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
            try {
                bytes = objectMapper.writeValueAsBytes(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        DataBuffer dataBuffer = res.bufferFactory().wrap(bytes);

        return res.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -5; //getOrder()的返回值越小越优先，-5就说明顶了天的高
    }
}
