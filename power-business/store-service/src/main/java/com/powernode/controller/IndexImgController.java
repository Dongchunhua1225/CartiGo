package com.powernode.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.IndexImg;
import com.powernode.model.Result;
import com.powernode.service.IndexImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图业务控制层
 */
@Api(tags = "轮播图接口管理")
@RequestMapping("admin/indexImg")
@RestController
@RequiredArgsConstructor
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    /**
     * 多条件分页查询轮播图
     *
     * @param current 页码
     * @param size    每页显示条件
     * @param status  状态
     * @return
     */
    @ApiOperation("多条件分页查询轮播图")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('admin:indexImg:page')")
    public Result<Page<IndexImg>> loadIndexImgPage( @RequestParam Long current,
                                                    @RequestParam Long size,
                                                   @RequestParam(required = false) Integer status) {
        // 创建轮播图分页对象
        Page<IndexImg> page = new Page<>(current, size);
        // 多条件分页查询轮播图
        page = indexImgService.page(page, new LambdaQueryWrapper<IndexImg>()
                .eq(ObjectUtil.isNotNull(status), IndexImg::getStatus, status)
                .orderByDesc(IndexImg::getSeq)
        );
        return Result.success(page);
    }

    /**
     * 新增轮播图
     *
     * @param indexImg 轮播图对象
     * @return
     */
    @ApiOperation("新增轮播图")
    @PostMapping
    @PreAuthorize("hasAuthority('admin:indexImg:save')")
    public Result<String> saveIndexImg(@RequestBody IndexImg indexImg) {
        Boolean saved = indexImgService.saveIndexImg(indexImg);
        return Result.handle(saved);
    }

    /**
     * 根据id查询轮播图信息
     *
     * @param imgId
     * @return
     */
    @ApiOperation("根据标识查询轮播图信息")
    @GetMapping("info/{imgId}")
    @PreAuthorize("hasAuthority('admin:indexImg:info')")
    public Result<IndexImg> loadIndexImgInfo(@PathVariable Long imgId) {
        IndexImg indexImg = indexImgService.queryIndexImgInfoById(imgId);
        return Result.success(indexImg);
    }

    /**
     * 修改轮播图内容
     *
     * @param indexImg 轮播图对象
     * @return
     */
    @ApiOperation("修改轮播图内容")
    @PutMapping
    @PreAuthorize("hasAuthority('admin:indexImg:update')")
    public Result<String> modifyIndexImg(@RequestBody IndexImg indexImg) {
        Boolean modified = indexImgService.modifyIndexImg(indexImg);
        return Result.handle(modified);
    }

    /**
     * 批量删除轮播图
     *
     * @param imgIds 轮播图id集合
     * @return
     */
    @ApiOperation("批量删除轮播图")
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:indexImg:delete')")
    public Result<String> removeIndexImg(@RequestBody List<Long> imgIds) {
        Boolean removed = indexImgService.removeIndexImgByIds(imgIds);
        return Result.handle(removed);
    }

    ///////////////////////微信小程序//////////////////////
    /**
     * 查询小程序轮播图列表
     * @return
     */
    @ApiOperation("查询小程序轮播图列表")
    @GetMapping("indexImgs")
    public Result<List<IndexImg>> loadWxIndexImgList() {
        List<IndexImg> indexImgList = indexImgService.queryWxIndexImgList();
        return Result.success(indexImgList);
    }

}
