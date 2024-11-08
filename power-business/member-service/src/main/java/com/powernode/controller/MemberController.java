package com.powernode.controller;


import com.powernode.domain.Member;
import com.powernode.model.Result;
import com.powernode.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//微信小程序会员业务管理控制层
@Api(tags = "微信小程序业务接口管理")
@RequestMapping("p/user")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 更新会员的头像和昵称
     *
     * @param member 会员对象
     * @return
     */

    @ApiOperation("更新会员的头像和昵称")
    @PutMapping("setUserInfo")
    public Result<String> modifyMemberInfo(@RequestBody Member member) {
        Boolean modified = memberService.modifyMemberInfoByOpenId(member);
        return Result.handle(modified);
    }

}
