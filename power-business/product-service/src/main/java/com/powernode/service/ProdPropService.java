package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdPropService extends IService<ProdProp>{

    //多条件分页查询商品规格
    Page<ProdProp> queryProdSpecPage(Long current, Long size, String propName);

    //新增商品规格
    Boolean saveProdSpec(ProdProp prodProp);
}
