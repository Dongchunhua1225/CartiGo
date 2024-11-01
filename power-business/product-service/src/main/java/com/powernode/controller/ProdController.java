package com.powernode.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.model.Result;
import com.powernode.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//商品管理控制层
@Api(tags = "商品管理")
@RequestMapping("prod/prod")
@RestController
@RequiredArgsConstructor
public class ProdController {

    @Autowired
    private ProdService prodService;

    /**
     * 多条件分页查询商品
     *
     * @param current  页码
     * @param size     每页显示条数
     * @param prodName 商品名称
     * @param status   商品状态
     */
    @ApiOperation("多条件分页查询商品")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prod:page')")
    public Result<Page<Prod>> loadProdPage(@RequestParam Long current,
                                           @RequestParam Long size,
                                           @RequestParam(required = false) String prodName,
                                           @RequestParam(required = false) Long status) {
        // 创建商品分页对象
        Page<Prod> page = new Page<>(current, size);
        // 多条件分页查询商品
        page = prodService.page(page, new LambdaQueryWrapper<Prod>()
                .eq(ObjectUtil.isNotNull(status), Prod::getStatus, status)
                .like(StringUtils.hasText(prodName), Prod::getProdName, prodName)
                .orderByDesc(Prod::getCreateTime)
        );
        return Result.success(page);
    }
}
