package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Member;
import com.powernode.mapper.MemberMapper;
import com.powernode.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService{
//
//    @Autowired
//    private MemberMapper memberMapper;
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
