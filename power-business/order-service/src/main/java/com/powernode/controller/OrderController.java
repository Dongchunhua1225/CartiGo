package com.powernode.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Order;
import com.powernode.model.Result;
import com.powernode.service.OrderService;
import com.powernode.vo.OrderStatusCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序订单业务控制层
 */
@Api(tags = "微信小程序订单接口管理")
@RequestMapping("p/myOrder")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("查询会员订单各状态数量")
    @GetMapping("orderCount")
    public Result<OrderStatusCount> loadMemberOrderStatusCount() {
        OrderStatusCount orderStatusCount = orderService.queryMemberOrderStatusCount();
        return Result.success(orderStatusCount);
    }

    /**
     * 分页查询会员订单列表
     *
     * @param current 页码
     * @param size    每页显示条数
     * @param status  订单状态(0全部，1待支付，2待发货，3待收货)
     * @return
     */
    @ApiOperation("分页查询会员订单列表")
    @GetMapping("myOrder")
    public Result<Page<Order>> loadMemberOrderPage(@RequestParam Long current,
                                                   @RequestParam Long size,
                                                   @RequestParam Long status) {
        Page<Order> page = orderService.queryMemberOrderPage(current, size, status);
        return Result.success(page);
    }

    /**
     * 根据Order NUmber 查询订单信息
     *
     */
    @ApiOperation("根据Order NUmber 查询订单信息")
    @GetMapping("orderDetail")
    public Result<Order> loadMemberOrderDetail(@RequestParam String orderNumber) {
        Order order = orderService.queryMemberOrderDetailByOrderNumber(orderNumber);
        return Result.success(order);
    }

    /**
     * 会员确认收货
     *
     */
    @ApiOperation("会员确认收货")
    @PutMapping("receipt/{orderNumber}")
    public Result<String> receiptMemberOrder(@PathVariable String orderNumber) {
        Boolean receipted = orderService.receiptMemberOrder(orderNumber);
        return Result.handle(receipted);
    }

    /**
     * 会员删除订单
     *
     */
    @ApiOperation("会员删除订单")
    @DeleteMapping("{orderNumber}")
    public  Result<String> removeMemberOrder(@PathVariable String orderNumber) {
        Boolean removed = orderService.removeMemberOrderByOrderNumber(orderNumber);
        return Result.handle(removed);
    }
}
