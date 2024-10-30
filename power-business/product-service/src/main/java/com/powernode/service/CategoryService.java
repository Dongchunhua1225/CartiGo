package com.powernode.service;

import com.powernode.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category>{

    //查询系统所有商品类目
    List<Category> queryAllCategoryList();

    //查询系统商品一级类目
    List<Category> queryFirstCategoryList();

    //新增商品类目
    Boolean saveCategory(Category category);

    //修改商品类目信息
    Boolean modifyCategory(Category category);
}
