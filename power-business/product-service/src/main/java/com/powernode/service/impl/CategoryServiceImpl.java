package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.ProductConstants;
import com.powernode.domain.Category;
import com.powernode.mapper.CategoryMapper;
import com.powernode.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.CategoryServiceImpl")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    //查询系统所有商品类目
    @Override
    @Cacheable(key = ProductConstants.ALL_CATEGORY_LIST_KEY)
    public List<Category> queryAllCategoryList(){
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getSeq));
    }


    //查询系统商品一级类目
    @Override
    @Cacheable(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    public List<Category> queryFirstCategoryList() {
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, 0) //一级类目就是parent id为0
                .eq(Category::getStatus, 1)    //status == 1说明这个一级类目可以正常用；
                                                    // == 0 说明这个一级类目此时不能用了
                .orderByDesc(Category::getSeq));
    }
}
