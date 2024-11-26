package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.MemberAddr;
import com.powernode.mapper.MemberAddrMapper;
import com.powernode.service.MemberAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.MemberAddrServiceImpl")
public class MemberAddrServiceImpl extends ServiceImpl<MemberAddrMapper, MemberAddr> implements MemberAddrService{

    @Autowired
    private MemberAddrMapper memberAddrMapper;

    @Override
    @Cacheable(key = "#openId")
    public List<MemberAddr> queryMemberAddrListByOpenId(String openId) {
        return memberAddrMapper.selectList(new LambdaQueryWrapper<MemberAddr>()
                .eq(MemberAddr::getStatus, 1)
                .eq(MemberAddr::getOpenId, openId)
                .orderByDesc(MemberAddr::getCommonAddr, MemberAddr::getCreateTime)); //最先展示默认地址，然后才是createTime
    }

    //新增收货地址逻辑：
        //1. 会员必须要有一个默认收货地址，所以如果会员新增的第一个收获地址应该为默认收获地址
    @Override
    @CacheEvict(key = "#openId")
    public Boolean saveMemberAddr(MemberAddr memberAddr, String openId) {
        //先将默认设置为0 --> 这样万一不是新增的第一条呢？逻辑通畅
        memberAddr.setCommonAddr(0);

        //其余收货地址信息
        memberAddr.setStatus(1);
        memberAddr.setCreateTime(new Date());
        memberAddr.setUpdateTime(new Date());
        memberAddr.setOpenId(openId);

        //根据会员openid查询会员收货地址数量
        Long count = memberAddrMapper.selectCount(new LambdaQueryWrapper<MemberAddr>()
                .eq(MemberAddr::getOpenId, openId));

        if(count == 0) {
            //说明当前会员新增的收货地址为第一个
            //所以要将其变成默认
            memberAddr.setCommonAddr(1);
        }

        return memberAddrMapper.insert(memberAddr) > 0;
    }
}
