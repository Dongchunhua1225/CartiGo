package com.powernode.ex.handler;

//全局异常处理类

import com.powernode.constant.BusinessEnum;
import com.powernode.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    //自定义业务异常类
    @ExceptionHandler(BusinessException.class)
    public Result<String> businessException(BusinessException e) {
        log.error("全局异常：{}", e.getMessage());
        return Result.fail(BusinessEnum.OPERATION_FAIL.getCode(), e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeException(RuntimeException e) {
        log.error("全局异常：{}", e.getMessage());
        return Result.fail(BusinessEnum.SERVER_INNER_ERROR);
    }

    /**
     * 权限不足
     * <p>
     * 这里捕捉了就无法让SpringSecurity处理，需要抛出交给SpringSecurity处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        log.error("全局异常：{}", e.getMessage());
        throw e;
    }
}
