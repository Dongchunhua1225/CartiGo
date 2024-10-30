package com.powernode.controller;

import com.powernode.domain.Category;
import com.powernode.model.Result;
import com.powernode.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品类目控制层
 */
@Api(tags = "商品类目接口管理")
@RequestMapping("prod/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询系统所有商品类目
     */
    @ApiOperation("查询系统所有商品类目")
    @GetMapping("table")
    @PreAuthorize("hasAuthority('prod:category:page')")
    public Result<List<Category>> loadAllCategoryList() {
        List<Category> list = categoryService.queryAllCategoryList();
        return Result.success(list);
    }

    /**
     * 查询系统商品一级类目
     */
    @ApiOperation("查询系统商品一级类目")
    @GetMapping("listCategory")
    @PreAuthorize("hasAuthority('prod:category:page')")
    public Result<List<Category>> loadFirstCategoryList() {
        List<Category> list = categoryService.queryFirstCategoryList();
        return Result.success(list);
    }
}
