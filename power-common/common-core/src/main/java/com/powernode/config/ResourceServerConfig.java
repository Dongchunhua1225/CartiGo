package com.powernode.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.constant.ResourceConstants;
import com.powernode.filter.TokenTranslationFilter;
import com.powernode.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

/**
 * Spring Security安全框架的资源服务器配置类
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //调用资源之前去验证
@RequiredArgsConstructor
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenTranslationFilter tokenTranslationFilter;

    protected void configure(HttpSecurity http) throws Exception {
        // 禁用跨站请求伪造（CSRF）保护
        http.csrf().disable();
        // 禁用CORS（跨源资源共享）功能
        http.cors().disable();
        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //编写一个token解析过滤器，将这个token转换为security安全框架能够认证的用户信息
        //再存放到当前资源股武器的容器中
        http.addFilterBefore(tokenTranslationFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置处理携带token但权限不足的请求
        http.exceptionHandling()
                // 处理没有携带token的请求
                .authenticationEntryPoint(authenticationEntryPoint())
                // 处理携带token，但是权限不足的请求
                 .accessDeniedHandler(accessDeniedHandler());

        // 配置其它请求
        http.authorizeHttpRequests()
                .antMatchers(ResourceConstants.RESOURCE_ALLOW_URLS)
                .permitAll()
                .anyRequest().authenticated();  // 除了需要放行的请求，都得需要进行身份的认证
    }

    //处理：请求没有携带token
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            //设置一个响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            //创建项目统一响应结果对象
            Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }


    //处理：请求中携带token但是权限不足的情况
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            //设置一个响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            //创建项目统一响应结果对象
            Result<Object> result = Result.fail(BusinessEnum.ACCESS_DENY_FAIL);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }
}
