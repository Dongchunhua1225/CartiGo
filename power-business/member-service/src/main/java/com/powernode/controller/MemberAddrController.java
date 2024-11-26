package com.powernode.controller;

import com.powernode.domain.MemberAddr;
import com.powernode.model.Result;
import com.powernode.service.MemberAddrService;
import com.powernode.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//会员收货地址管理控制层
@Api(tags = "会员收获地址接口管理")
@RestController
@AllArgsConstructor
@RequestMapping("p/address")
public class MemberAddrController {

    @Autowired
    private MemberAddrService memberAddrService;

    /**
     * 查询会员所有收货地址
     *
     * @return
     */
    @ApiOperation("查询会员所有收货地址")
    @GetMapping("list")
    public Result<List<MemberAddr>> loadMemberAddrList() {
        String openId = AuthUtils.getMemberOpenId();
        List<MemberAddr> memberAddrs = memberAddrService.queryMemberAddrListByOpenId(openId);
        return Result.success(memberAddrs);
    }

    /**
     * 新增会员收货地址
     *
     * @param memberAddr 会员收货地址对象
     * @return
     */
    @ApiOperation("新增会员收货地址")
    @PostMapping
    public Result saveMemberAddr(@RequestBody MemberAddr memberAddr) {
        String openId = AuthUtils.getMemberOpenId();
        Boolean saved = memberAddrService.saveMemberAddr(memberAddr, openId);
        return Result.handle(saved);
    }

    ////////////////////////////Feign 接口////////////////////////////////////////////////////


    @GetMapping("getMemberAddrById")
    public Result<MemberAddr> getMemberAddrById(@RequestParam Long addrId){
        MemberAddr memberAddr = memberAddrService.getById(addrId);

        return Result.success(memberAddr);
    }
}
