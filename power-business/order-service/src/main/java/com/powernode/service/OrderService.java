package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.vo.OrderStatusCount;

import java.util.Date;

public interface OrderService extends IService<Order>{

    //多条件分页查询订单
    Page<Order> queryOrderPage(Page<Order> page, String orderNumber, Integer status, Date startTime, Date endTime);

    //根据订单编号查询订单详情
    Order queryOrderDetailByOrderNumber(Long orderNumber);

    //查询会员订单各状态数量
    OrderStatusCount queryMemberOrderStatusCount();

    //分页查询会员订单列表
    Page<Order> queryMemberOrderPage(Long current, Long size, Long status);

    //根据Order NUmber 查询订单信息
    Order queryMemberOrderDetailByOrderNumber(String orderNumber);

    //会员确认收货
    Boolean receiptMemberOrder(String orderNumber);
}
