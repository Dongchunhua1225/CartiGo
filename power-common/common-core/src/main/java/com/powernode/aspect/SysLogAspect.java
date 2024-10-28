package com.powernode.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

//记录系统操作日志aop
@Component
@Aspect
@Slf4j
public class SysLogAspect {
    /**
     * 切点表达式
     */
    public static final String POINT_CUT = "execution(* com.powernode.controller.*.*(..))";

    @Around(POINT_CUT)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;

        //获取ip地址和请求路径
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //获取ip地址
        String remoteHost = request.getRemoteHost();

        //获取请求路径
        String path = request.getRequestURI();

        //获取请求参数
        Object[] args = joinPoint.getArgs();

        //获取请求方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.toString();

        //获取目标方法上的ApiOperation注解
        ApiOperation apiOperation = method.getDeclaredAnnotation(ApiOperation.class);
        //判断注解对象是否为空
        if(ObjectUtil.isNotNull(apiOperation)) {
            //获取其描述
            String operation = apiOperation.value();
        }

        String finalArgs = "";
        //判断参数类型
        if(ObjectUtil.isNotNull(args) && args.length != 0 && args[0] instanceof MultipartFile) {
            //说明档期那参数为文件对象
            finalArgs = "files";
        } else {
            //将参数对象转换为json格式字符串
            finalArgs = JSONObject.toJSONString(apiOperation);
        }

        //获取执行时长
        long startTime = System.currentTimeMillis();

        //执行方法
        try{
            result = joinPoint.proceed(args);
        }catch (Throwable e){
            throw new RuntimeException(e);
        }

        //结束时间
        long endTime = System.currentTimeMillis();

        //执行时长
        long execTime = endTime - startTime;

        //输出日志
        log.info("调用时间：{}，请求接口路径：{}，请求参数：{}，请求方法：{}，ip：{}，请求耗时：{}ms",
                new Date(), path, finalArgs, methodName, remoteHost, execTime);

        return result;
    }
}
