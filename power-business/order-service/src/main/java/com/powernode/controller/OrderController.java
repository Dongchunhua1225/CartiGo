package com.powernode.controller;


import com.powernode.model.Result;
import com.powernode.service.OrderService;
import com.powernode.vo.OrderStatusCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
