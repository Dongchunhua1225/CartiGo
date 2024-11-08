package com.powernode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.Member;

public interface MemberService extends IService<Member>{
    //更新会员的头像和昵称
    Boolean modifyMemberInfoByOpenId(Member member);
//
//    //批量删除会员
//    Boolean removeMembersByIds(List<Long> ids);
}
