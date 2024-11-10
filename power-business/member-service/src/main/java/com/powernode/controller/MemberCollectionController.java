package com.powernode.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.model.Result;
import com.powernode.service.MemberCollectionService;
import com.powernode.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员收藏商品业务控制层
 */
@Api(tags = "会员收藏商品接口管理")
@RequestMapping("p/collection")
@RestController
@RequiredArgsConstructor
public class MemberCollectionController {

    @Autowired
    private MemberCollectionService memberCollectionService;

    /**
     * 查询会员收藏商品的数量
     *
     * @return
     */
    @ApiOperation("查询会员收藏商品的数量")
    @GetMapping("count")
    public Result<Long> loadMemberCollectionProdCount() {
        Long count = memberCollectionService.queryMemberCollectionProdCount();
        return Result.success(count);
    }

    /**
     * 分页查询会员收藏商品列表
     *
     * @param current 页码
     * @param size    每页显示条数
     * @return
     */
    @ApiOperation("分页查询会员收藏商品列表")
    @GetMapping("prods")
    public Result<Page<Prod>> loadMemberCollectionProdPage(@RequestParam Long current,
                                                           @RequestParam Long size) {
        // 根据会员openid分页查询会员收藏商品列表
        Page<Prod> page = memberCollectionService
                .queryMemberCollectionProdPageByOpenId(AuthUtils.getMemberOpenId(), current, size);
        return Result.success(page);
    }

}
