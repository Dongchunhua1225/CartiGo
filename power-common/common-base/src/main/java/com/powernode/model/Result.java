package com.powernode.model;

//项目同意响应结果对象

import com.powernode.constant.BusinessEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("项目统一响应结果对象")
public class Result<T> implements Serializable {

    @ApiModelProperty("状态码")
    private Integer code = 200;

    @ApiModelProperty("消息")
    private String msg = "ok";

    @ApiModelProperty("数据")
    private T data;

    //操作成功
    public static <T> Result<T> success(T data) {
        Result result = new Result<>();
        result.setData(data);

        return result;
    }

    //操作失败
    public static <T> Result<T> fail(Integer code, String msg) {
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);

        return result;
    }

    //操作失败
    public static <T> Result<T> fail(BusinessEnum businessEnum) {
        Result result = new Result();
        result.setCode(businessEnum.getCode());
        result.setMsg(businessEnum.getDesc());
        result.setData(null);

        return result;
    }


    //处理用户的操作
    public static Result<String> handle(Boolean flag) {
        if(flag) {
            return Result.success(null);
        }

        return Result.fail(BusinessEnum.OPERATION_FAIL);
    }
}
