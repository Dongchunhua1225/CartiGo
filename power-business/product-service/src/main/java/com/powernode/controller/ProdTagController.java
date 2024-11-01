package com.powernode.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdTag;
import com.powernode.model.Result;
import com.powernode.service.ProdTagReferenceService;
import com.powernode.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分组标签管理控制层
 */
@Api(tags = "分组标签接口管理")
@RequestMapping("prod/prodTag")
@RestController
@RequiredArgsConstructor
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    @ApiOperation("多条件分页查询分组标签")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    public Result<Page<ProdTag>> loadProdTagPage(@RequestParam Long current,
                                                 @RequestParam Long size,
                                                 @RequestParam(required = false) String title,
                                                 @RequestParam(required = false) Integer status) {
        // 创建分页对象
        Page<ProdTag> page = new Page<>(current, size);
        // 多条件分页查询分组标签
        page = prodTagService.page(page, new LambdaQueryWrapper<ProdTag>()
                .eq(ObjectUtil.isNotNull(status), ProdTag::getStatus, status)
                .like(StringUtils.hasText(title), ProdTag::getTitle, title)
                .orderByDesc(ProdTag::getSeq)
        );
        return Result.success(page);
    }

    /**
     * 新增商品分组标签
     * @param prodTag 商品分组标签对象
     */
    @ApiOperation("新增商品分组标签")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:prodTag:save')")
    public Result saveProdTag(@RequestBody ProdTag prodTag) {
        Boolean saved = prodTagService.saveProdTag(prodTag);
        return Result.success(saved);
    }

    /**
     * 根据标识查询分组标签详情
     * @param tagId 分组标签标识
     */
    @ApiOperation("根据标识查询分组标签详情")
    @GetMapping("info/{tagId}")
    @PreAuthorize("hasAuthority('prod:prodTag:info')")
    public Result<ProdTag> loadProdTagInfo(@PathVariable Long tagId) {
        ProdTag prodTag = prodTagService.getById(tagId);
        return Result.success(prodTag);
    }

    /**
     * 修改商品分组标签信息
     * @param prodTag 商品分组标签对象
     */
    @ApiOperation("修改商品分组标签信息")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:prodTag:update')")
    public Result<String> modifyProdTag(@RequestBody ProdTag prodTag) {
        Boolean updated = prodTagService.modifyProdTag(prodTag);
        return Result.handle(updated);
    }

    /**
     * 根据标识删除商品分组标签
     * @param tagId 分组标签标识
     */
    @ApiOperation("根据标识删除商品分组标签")
    @DeleteMapping("{tagId}")
    @PreAuthorize("hasAuthority('prod:prodTag:delete')")
    public Result<String> removeProdTag(@PathVariable Long tagId) {
        Boolean removed = prodTagService.removeById(tagId);
        return Result.handle(removed);
    }

    //查询状态正常的商品分组标签集合
    /**
     * 查询状态正常的商品分组标签集合
     */
    @ApiOperation("查询状态正常的商品分组标签集合")
    @GetMapping("listTagList")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    public Result<List<ProdTag>> loadProdTagList() {
        List<ProdTag> list = prodTagService.queryProdTagList();
        return Result.success(list);
    }
}
