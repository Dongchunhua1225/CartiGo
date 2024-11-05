package com.powernode.controller;

import com.powernode.domain.MemberAddr;
import com.powernode.model.Result;
import com.powernode.service.MemberAddrService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//会员收货地址管理控制层
@Api(tags = "会员收获地址接口管理")
@RestController
@AllArgsConstructor
@RequestMapping("p/address/")
public class MemberAddrController {

    @Autowired
    private MemberAddrService memberAddrService;

    ////////////////////////////Feign 接口////////////////////////////////////////////////////
    @GetMapping("getMemberAddrById")
    public Result<MemberAddr> getMemberAddrById(@RequestParam Long addrId){
        MemberAddr memberAddr = memberAddrService.getById(addrId);

        return Result.success(memberAddr);
    }
}
