package com.powernode.service;

import com.powernode.domain.ProdTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProdTagService extends IService<ProdTag>{

    //新增商品分组标签
    Boolean saveProdTag(ProdTag prodTag);

    //修改商品分组标签信息
    Boolean modifyProdTag(ProdTag prodTag);

    //查询状态正常的商品分组标签集合
    List<ProdTag> queryProdTagList();
}
