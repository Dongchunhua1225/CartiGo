package com.powernode.feign;


import com.powernode.domain.MemberAddr;
import com.powernode.feign.sentinel.OrderMemberFeignSentinel;
import com.powernode.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//订单业务远程调用会员业务模块 feign接口 远程调度
@FeignClient(value = "member-service", fallback = OrderMemberFeignSentinel.class)
public interface OrderMemberFeign {

    @GetMapping("p/address/getMemberAddrById")
    public Result<MemberAddr> getMemberAddrById(@RequestParam Long addrId);

    @GetMapping("admin/user/getNickNameByOpenId")
    public Result<String> getNickNameByOpenId(@RequestParam String openId);
}
