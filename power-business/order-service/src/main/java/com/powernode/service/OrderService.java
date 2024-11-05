package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

public interface OrderService extends IService<Order>{

    //多条件分页查询订单
    Page<Order> queryOrderPage(Page<Order> page, String orderNumber, Integer status, Date startTime, Date endTime);

    //根据订单编号查询订单详情
    Order queryOrderDetailByOrderNumber(Long orderNumber);
}
