package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.ProductConstants;
import com.powernode.domain.Category;
import com.powernode.ex.handler.BusinessException;
import com.powernode.mapper.CategoryMapper;
import com.powernode.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    //新增商品类目
    @Override
    @Caching(evict = {
            @CacheEvict(key = ProductConstants.ALL_CATEGORY_LIST_KEY),
            @CacheEvict(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    }) //如果你要清楚多个缓存的话，不能两个evict直接叠加
        //加一个caching用它的一个prop ‘evict’ = {将你要叠加的语句放里}
    public Boolean saveCategory(Category category) {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());

        return categoryMapper.insert(category) > 0;
    }

    //修改商品类目信息
    @Override
    @Caching(evict = {
            @CacheEvict(key = ProductConstants.ALL_CATEGORY_LIST_KEY),
            @CacheEvict(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    })
    public Boolean modifyCategory(Category category) {
        //根据id查询商品详情
        Category beforeCategory = categoryMapper.selectById(category.getCategoryId());

        //获取商品类目之前的级别
            //如果parent为0则为1级类目
            //部位0为2级类目
        Long beforeParent = beforeCategory.getParentId();

        //判断商品类目的修改详情
        //1 -> 2 : 之前pid为0 且 修改后的pid不为0
        if(beforeParent == 0 && category.getParentId() != null && category.getParentId() != 0) {
            //查看当前类目是否包含子类目
                //包含则不允许修改
            List<Category> childList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .eq(Category::getParentId, category.getCategoryId()));

            if(CollectionUtil.isNotEmpty(childList) && childList.size() != 0) {
                throw new BusinessException("有子类目，不可以对其进行修改");
            }
            // 1->2 除了判断限制其子类目之外 无需任何其余操作
        }

        //2 -> 1 :  之前pid不为0 且 当前pid为null
        if(beforeParent != 0 && category.getParentId() == null) {
            category.setParentId(0L); //修改他的上级id
        }

        return categoryMapper.updateById(category) > 0;
    }

    //
}
