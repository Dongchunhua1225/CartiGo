package com.powernode.service;

import com.powernode.domain.MemberAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MemberAddrService extends IService<MemberAddr>{

    //查询会员所有收货地址
    List<MemberAddr> queryMemberAddrListByOpenId(String openId);

    //新增会员收货地址
    Boolean saveMemberAddr(MemberAddr memberAddr, String openId);
}
