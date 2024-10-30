package com.powernode.service;

import com.powernode.domain.ProdTag;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ProdTagService extends IService<ProdTag>{

    //新增商品分组标签
    Boolean saveProdTag(ProdTag prodTag);
}
