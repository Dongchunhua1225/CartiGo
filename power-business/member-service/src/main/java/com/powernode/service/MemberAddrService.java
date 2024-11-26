package com.powernode.service;

import com.powernode.domain.MemberAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MemberAddrService extends IService<MemberAddr>{

    //查询会员所有收货地址
    List<MemberAddr> queryMemberAddrListByOpenId(String openId);

    //新增会员收货地址
    Boolean saveMemberAddr(MemberAddr memberAddr, String openId);

    //修改会员收货地址信息
    Boolean modifyMemberAddrInfo(MemberAddr memberAddr, String openId);

    //"删除会员收货地址
    Boolean removeMemberAddrById(Long addrId, String openId);

    //更改会员默认收货地址
    Boolean modifyMemberDefaultAddrInfo(Long newAddrId, String openId);
}
