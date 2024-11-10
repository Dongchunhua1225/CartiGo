package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.MemberCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.Prod;

public interface MemberCollectionService extends IService<MemberCollection>{

    //查询会员收藏商品的数量
    Long queryMemberCollectionProdCount();

    //分页查询会员收藏商品列表
    Page<Prod> queryMemberCollectionProdPageByOpenId(String openId, Long current, Long size);
}
