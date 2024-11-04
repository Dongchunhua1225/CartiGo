package com.powernode.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Order;
import com.powernode.model.Result;
import com.powernode.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 订单业务模块控制层
 */
@Api(tags = "订单业务接口管理")
@RequestMapping("order/order")
@RestController
@RequiredArgsConstructor
public class SysOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 多条件分页查询订单
     *
     * @param current     页码
     * @param size        每页显示条件
     * @param orderNumber 订单编号
     * @param status      订单状态
     * @param startTime   订单开始时间
     * @param endTime     订单结束时间
     * @return
     */
    @ApiOperation("多条件分页查询订单")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('order:order:page')")
    public Result<Page<Order>> loadOrderPage(@RequestParam Long current, @RequestParam Long size,
                                             @RequestParam(required = false) String orderNumber,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) Date startTime,
                                             @RequestParam(required = false) Date endTime) {
        // 创建订单分页对象
        Page<Order> page = new Page<>(current, size);
        // 多条件分页查询订单
        page = orderService.queryOrderPage(page, orderNumber, status, startTime, endTime);
        return Result.success(page);
    }

}
