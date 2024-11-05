package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.BusinessEnum;
import com.powernode.domain.MemberAddr;
import com.powernode.domain.Order;
import com.powernode.domain.OrderItem;
import com.powernode.ex.handler.BusinessException;
import com.powernode.feign.OrderMemberFeign;
import com.powernode.mapper.OrderItemMapper;
import com.powernode.mapper.OrderMapper;
import com.powernode.model.Result;
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

    @Autowired
    private OrderMemberFeign orderMemberFeign;

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

    @Override
    public Order queryOrderDetailByOrderNumber(Long orderNumber) {
        //根据订单编号查询订单信息
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNumber, orderNumber));

        //根据订单编号查询order item detial info list
        List<OrderItem> orderItemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderNumber, orderNumber));

        //将拿到的item list赋值给order内部的orderItemList
        order.setOrderItems(orderItemList);

        //从订单记录中获取顶戴收货地址
        Long addrOrderId = order.getAddrOrderId();

        //远程调用 -> 根据地址查询地址
        Result<MemberAddr> result = orderMemberFeign.getMemberAddrById(addrOrderId);
        if(result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw new BusinessException("远程调用查询收货地址信息失败");
        }

        //转换result类型数据
        MemberAddr memberAddr = result.getData();
        //赋值给order
        order.setUserAddrOrder(memberAddr);

        //远程接口调用 -> 根据会员openid查询会员昵称
        Result<String> result1 = orderMemberFeign.getNickNameByOpenId(order.getOpenId());
        if(result1.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw new BusinessException("远程调用查询会员nickName失败");
        }
        String nickName = result1.getData();
        order.setNickName(nickName);

        return order;
    }
}
