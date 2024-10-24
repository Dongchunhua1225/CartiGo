package com.powernode.config;

//security安全框架配置类

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.constant.AuthConstants;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.impl.UserDetailsServiceImpl;
import com.powernode.model.LoginResult;
import com.powernode.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //设置Security安全框架自己的认证流程
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨站请求伪造
        http.cors().disable();
        //关闭跨域请求
        http.csrf().disable();
        //关闭session使用策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //配置登录信息
        http.formLogin().
                loginProcessingUrl(AuthConstants.LOGIN_URL) //设置登录url
                .successHandler(authenticationSuccessHandler()) //登录成功处理器
                .failureHandler(authenticationFailureHandler()); //登陆失败处理器

        //配置登出信息
        http.logout().logoutUrl(AuthConstants.LOGOUT_URL)//设置登出url
            .logoutSuccessHandler(logoutSuccessHandler());

        //要求所有请求都需要进行身份的额认证
        http.authorizeHttpRequests().anyRequest().authenticated();
    }

    //登陆成功处理器
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (AuthenticationSuccessHandler) (request, response, authentication) -> {
            //设置一个响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            //使用UUID来当作token
            String token = UUID.randomUUID().toString(); //这个就是我们的token值

            //从security框架中获取认证对象(SecurityUser)斌转换为json格式字符串
            String userJson = JSON.toJSONString(authentication.getPrincipal());
            //将token当作key，认证用户对象的json格式的字符串，存放到redis中
            stringRedisTemplate.opsForValue().set(AuthConstants.LOGIN_TOKEN_PREFIX + token, userJson, Duration.ofSeconds(AuthConstants.TOKEN_TIME));

            //封装一个登录统一响应的结果
            LoginResult loginResult = new LoginResult(token, AuthConstants.TOKEN_TIME);

            //创建一个响应结果对象
            Result<LoginResult> result = Result.success(loginResult);

            //返回结果
            //这种响应方式是手动相应方式
            //在其余事务层可以用@RestController 或 @ResponseBody
            //但是spring security中不会自动将对象转为JSON，所以需要手动做着一切
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    //登陆失败处理器
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            //设置响应头信息
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            //创建统一响应结果对象
            Result<Object> result = new Result<>();
            result.setCode(BusinessEnum.OPERATION_FAIL.getCode());

            if (exception instanceof BadCredentialsException) {
                result.setMsg("用户名或密码错误");
            } else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("用户不存在");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("账号时限已过期");
            } else if (exception instanceof AccountStatusException) {
                result.setMsg("账号异常，请联系管理员");
            } else if (exception instanceof InternalAuthenticationServiceException) {
                result.setMsg(exception.getMessage());
            } else {
                result.setMsg(BusinessEnum.OPERATION_FAIL.getDesc());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    //登出成功处理器
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            //本质就是把相应的token从redis池里去掉

            //设置相应头
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            //从请求头中获取token
            String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
            String token = authorization.replaceFirst(AuthConstants.BEARER, "");
            //将当前token从redis删除
            stringRedisTemplate.delete(AuthConstants.LOGIN_TOKEN_PREFIX + token);

            //创建统一响应对象
            Result<Object> result = Result.success(null);

            //返回结果给前端
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
