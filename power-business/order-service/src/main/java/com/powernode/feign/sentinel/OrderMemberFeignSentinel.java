package com.powernode.feign.sentinel;

import com.powernode.domain.MemberAddr;
import com.powernode.feign.OrderMemberFeign;
import com.powernode.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//orderMemberFeign的熔断
@Component
@Slf4j
public class OrderMemberFeignSentinel implements OrderMemberFeign {


    @Override
    public Result<MemberAddr> getMemberAddrById(Long addrId) {
        log.error("远程调用接口失败： 根据id查询用户收货地址信息");
        //throw new BusinessException("远程调用接口失败： 根据id查询用户收货地址信息");

        return null;
    }

    @Override
    public Result<String> getNickNameByOpenId(String openId) {
        log.error("远程调用接口失败： 根据openid查询nickName信息");

        return null;
    }
}
