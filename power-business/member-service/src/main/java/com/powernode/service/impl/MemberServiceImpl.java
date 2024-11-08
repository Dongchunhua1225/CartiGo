package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Member;
import com.powernode.mapper.MemberMapper;
import com.powernode.service.MemberService;
import com.powernode.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService{

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Boolean modifyMemberInfoByOpenId(Member member) {

        //获取会员的openid
        String openid = AuthUtils.getMemberOpenId();

        //更新会员的头像和昵称
        int returnValue = memberMapper.update(member, new LambdaQueryWrapper<Member>().eq(Member::getOpenId, openid));
        return returnValue > 0;
    }

//
//    @Autowired
//    private MemberService memberService;
//
//    @Override
//    public Boolean removeMembersByIds(List<Long> ids) {
//
//        return null;
//    }
}
