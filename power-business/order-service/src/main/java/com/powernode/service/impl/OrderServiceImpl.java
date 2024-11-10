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
import com.powernode.util.AuthUtils;
import com.powernode.vo.OrderStatusCount;
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


    //查询会员订单各状态数量
    @Override
    public OrderStatusCount queryMemberOrderStatusCount() {
        //获取会员openid
        String openid = AuthUtils.getMemberOpenId();

        //根据会员openid查询会员待支付订单数量
        Long unPay = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 1)); //在数据库就规定好了1代表没支付

        //根据会员openid查询会员待发货订单数量
        Long payed = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 2)); //在数据库就规定好了2代表支付没发货

        //根据会员openid查询会员待收获订单数
        Long consignment = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 3)); //在数据库就规定好了3代表发货了但没收货

        return OrderStatusCount.builder()
                .unPay(unPay)
                .payed(payed)
                .consignment(consignment)
                .build();
    }

    //分页查询会员订单列表
    @Override
    public Page<Order> queryMemberOrderPage(Long current, Long size, Long status) {
        //获取会员openid
        String openid = AuthUtils.getMemberOpenId();

        Page<Order> page = new Page<>(current, size);

        //分页查询会员订单列表
        page = orderMapper.selectPage(page, new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(status != 0, Order::getStatus, status)
                .orderByDesc(Order::getCreateTime));

        //从订单分页对象中获取订单记录集合
        List<Order> orderList = page.getRecords();
        //判断是否有值
        if(CollectionUtil.isEmpty(orderList) || orderList.size() == 0) {
            //咩有值
            return page;
        }
        //有值 -> 从订单list中获取订单number list
        List<String> numList = orderList.stream().map(Order::getOrderNumber).collect(Collectors.toList());

        //根据numList的orderNumber查询OrderItem集合
        List<OrderItem> itemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .in(OrderItem::getOrderNumber, numList));

        //循环遍历订单集合
        orderList.forEach(order -> {
            //从所有商品条目对象集合中过滤出和当前订单一致的
            List<OrderItem> finalList = itemList.stream()
                    .filter(item ->
                            item.getOrderNumber()
                            .equals(order.getOrderNumber())
                    )
                    .collect(Collectors.toList());

            //将收集来的finalList传回order中
            order.setOrderItemDtos(finalList);
        });

        return page;
    }

    //根据Order NUmber 查询订单信息
    @Override
    public Order queryMemberOrderDetailByOrderNumber(String orderNumber) {
        //根据ordernumber查询订单信息
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, orderNumber));

        //验证该订单是否存在
        if(ObjectUtil.isNull(order)) {
            //理应是存在的，如果走到这不存在那就说明有问题了
            throw new BusinessException("订单编号有误");
        }

        //远程调用 -> 查询订单收获地址
        Result<MemberAddr> addrResult = orderMemberFeign.getMemberAddrById(order.getAddrOrderId());
        //判断是否有问题
        if(addrResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())){
            throw new BusinessException("远程调用查询收货地址信息失败");
        }

        //订单收货地址
        MemberAddr addr = addrResult.getData();
        order.setUserAddrDto(addr);

        //根据ordernumber查询订单商品条目对象集合
        List<OrderItem> itemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNumber, orderNumber));
        order.setOrderItemDtos(itemList);

        return order;
    }

    //会员确认收货
    @Override
    public Boolean receiptMemberOrder(String orderNumber) {
        //创建一个新的订单对象
        Order order = new Order();
        order.setUpdateTime(new Date());
        order.setFinallyTime(new Date());
        order.setStatus(5);

        return orderMapper.update(order, new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, orderNumber)) > 0;
    }
}
