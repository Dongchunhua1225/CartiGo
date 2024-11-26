package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @CacheEvict(key = "#openId")
    public Boolean modifyMemberAddrInfo(MemberAddr memberAddr, String openId) {
         memberAddr.setUpdateTime(new Date());
         return memberAddrMapper.updateById(memberAddr) > 0;
    }

    @Override
    @CacheEvict(key = "#openId")
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMemberAddrById(Long addrId, String openId) {
        MemberAddr memberAddr = memberAddrMapper.selectById(addrId);

        //首先先判断当前是不是默认收货地址
        if(memberAddr.getCommonAddr().equals(1)) {
            //说明当前收货地址是用户默认收获地址
                //应该重新选一个作为默认收货地址 --> 最近新增的就行
            List<MemberAddr> addrList = memberAddrMapper.selectList(new LambdaQueryWrapper<MemberAddr>()
                    .eq(MemberAddr::getOpenId, openId)
                    .eq(MemberAddr::getCommonAddr, 0)
                    .orderByDesc(MemberAddr::getCreateTime));

            if(CollectionUtil.isNotEmpty(addrList) && addrList.size() != 0) {
                //说明会员有其他的地址
                //获取第一个地址，将其设置为新的
                MemberAddr memberAddrNew = addrList.get(0);
                memberAddrNew.setUpdateTime(new Date());
                memberAddrNew.setCommonAddr(1);
                memberAddrMapper.updateById(memberAddrNew); //更新这个default
            }

            //如果没有的话 那就无所谓
        }
        //说明当前收获地址不是默认收货地址，直接删除
        return memberAddrMapper.deleteById(addrId) > 0;
    }
}
