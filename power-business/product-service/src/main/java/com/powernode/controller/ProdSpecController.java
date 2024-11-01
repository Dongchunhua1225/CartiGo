package com.powernode.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.powernode.model.Result;
import com.powernode.service.ProdPropService;
import com.powernode.service.ProdPropValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品规格管理控制层
 */
@Api(tags = "商品规格接口管理")
@RequestMapping("prod/spec")
@RestController
@RequiredArgsConstructor
public class ProdSpecController {
    @Autowired
    private ProdPropService prodPropService;

    @Autowired
    private ProdPropValueService prodPropValueService;

    /**
     * 多条件分页查询商品规格
     * @param current  页码
     * @param size     每页显示条数
     * @param propName 属性名称
     */
    @ApiOperation("多条件分页查询商品规格")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public Result<Page<ProdProp>> loadProdSpecPage(@RequestParam Long current,
                                                   @RequestParam Long size,
                                                   @RequestParam(required = false) String propName) {
        // 多条件分页查询商品规格
        //无法定义page 得自己来定义
        Page<ProdProp> page = prodPropService.queryProdSpecPage(current, size, propName);
        return Result.success(page);
    }

    /**
     * 新增商品规格
     * @param prodProp 商品属性对象
     */
    @ApiOperation("新增商品规格")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:spec:save')")
    public Result<String> saveProdSpec(@RequestBody ProdProp prodProp) {
        Boolean saved = prodPropService.saveProdSpec(prodProp);
        return Result.handle(saved);
    }

    /**
     * 修改商品规格信息
     * @param prodProp 商品属性对象
     */
    @ApiOperation("修改商品规格信息")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:spec:update')")
    public Result<String> modifyProdSpec(@RequestBody ProdProp prodProp) {
        Boolean updated = prodPropService.modifyProdSpec(prodProp);
        return Result.handle(updated);
    }

    /**
     * 删除商品规格
     * @param propId 属性标识
     */
    @ApiOperation("删除商品规格")
    @DeleteMapping("{propId}")
    @PreAuthorize("hasAuthority('prod:spec:delete')")
    public Result<String> removeProdSpec(@PathVariable Long propId) {
        Boolean removed = prodPropService.removeProdSpecByPropId(propId);
        return Result.handle(removed);
    }
}
