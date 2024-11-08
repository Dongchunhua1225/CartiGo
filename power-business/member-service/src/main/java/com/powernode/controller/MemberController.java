package com.powernode.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.Member;
import com.powernode.model.Result;
import com.powernode.service.MemberService;
import com.powernode.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查询会员是否绑定手机号码")
    @GetMapping("isBindPhone")
    public Result<Boolean> loadMemberIsBindPhone() {
        //获取会员的openid
        String openid = AuthUtils.getMemberOpenId();

        //根据会员openid查询会员详情
        Member member = memberService.getOne(new LambdaQueryWrapper<Member>().eq(Member::getOpenId, openid));

        return Result.success(StringUtils.hasText(member.getUserMobile()));
    }

}
