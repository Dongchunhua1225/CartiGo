package com.powernode.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Notice;
import com.powernode.model.Result;
import com.powernode.service.NoticeService;
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

/**
 * 公告业务管理控制层
 */
@Api(tags = "公告业务接口管理")
@RequestMapping("shop/notice")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 多条件分页查询公告列表
     *
     * @param current 页码
     * @param size    每页显示条件
     * @param title   标题
     * @param status  状态
     * @param isTop   是否置顶
     * @return
     */
    @ApiOperation("多条件分页查询公告列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('shop:notice:page')")
    public Result<Page<Notice>> loadNoticePage(@RequestParam Long current,
                                               @RequestParam Long size,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(required = false) Integer isTop) {
        // 创建公告分页对象
        Page<Notice> page = new Page<>(current, size);
        // 多条件分页查询公告列表
        page = noticeService.page(page, new LambdaQueryWrapper<Notice>()
                .eq(ObjectUtil.isNotNull(status), Notice::getStatus, status)
                .eq(ObjectUtil.isNotNull(isTop), Notice::getIsTop, isTop)
                .like(StringUtils.hasText(title), Notice::getTitle, title)
                .orderByDesc(Notice::getCreateTime)
        );
        return Result.success(page);
    }
}
