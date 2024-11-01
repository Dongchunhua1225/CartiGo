package com.powernode.service;

import com.powernode.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdService extends IService<Prod>{

    //新增商品
    Boolean saveProd(Prod prod);

    //根据标识查询商品详情
    Prod queryProdInfoById(Long prodId);

    //修改商品信息
    Boolean modifyProdInfo(Prod prod);
}
