package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Order;
import com.powernode.domain.OrderItem;
import com.powernode.mapper.OrderItemMapper;
import com.powernode.mapper.OrderMapper;
import com.powernode.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    //多条件分页查询订单
    @Override
    public Page<Order> queryOrderPage(Page<Order> page, String orderNumber, Integer status, Date startTime, Date endTime) {
        //多条件分页查询订单列表
        orderMapper.selectPage(page, new LambdaQueryWrapper<Order>()
                .eq(ObjectUtil.isNotNull(status), Order::getStatus, status)
                .between(ObjectUtil.isAllNotEmpty(startTime, endTime), Order::getCreateTime, startTime, endTime)
                .eq(StringUtils.hasText(orderNumber),Order::getOrderNumber, orderNumber)
                .orderByDesc(Order::getCreateTime)
        );

        //从订单分页中获取订单记录list
        List<Order> orderList = page.getRecords();
        //判断是否有值
        if(CollectionUtil.isEmpty(orderList) && orderList.size() == 0) {
            //意味着没有值 为空
            //直接返回page
            return page;
        }
        //有值 -> 从orderList中获取订单id集合
        List<String> orderNumberList = orderList.stream().map(Order::getOrderNumber).collect(Collectors.toList());

        //根据订单编号查询订单商品detail
        List<OrderItem>orderItemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .in(OrderItem::getOrderNumber, orderNumberList));

        //循环遍历orderList
        orderList.forEach( order -> {
            //从orderItemList中过滤出与当前订单订单记录订单编号一致的orderitem
            List<OrderItem> itemList = orderItemList.stream()
                    .filter(orderItem -> orderItem.getOrderNumber().equals(order.getOrderNumber()))
                    .collect(Collectors.toList());

            //set Order的orderItems
            order.setOrderItems(itemList);
        });

        return page;
    }
}
