package com.powernode.service;

import com.powernode.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdService extends IService<Prod>{

    //新增商品
    Boolean saveProd(Prod prod);
}
