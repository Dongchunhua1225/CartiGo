package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.ProdProp;
import com.powernode.domain.ProdPropValue;
import com.powernode.mapper.ProdPropMapper;
import com.powernode.mapper.ProdPropValueMapper;
import com.powernode.service.ProdPropService;
import com.powernode.service.ProdPropValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService{

    @Autowired
    private ProdPropMapper prodPropMapper;

    @Autowired
    private ProdPropValueMapper prodPropValueMapper;

    @Autowired
    private ProdPropValueService prodPropValueService;

    //多条件分页查询商品规格
    @Override
    public Page<ProdProp> queryProdSpecPage(Long current, Long size, String propName) {
        //创建分页对象
        Page<ProdProp> page = new Page<>(current, size);

        //多条件分页查询商品属性
        prodPropMapper.selectPage(page, new LambdaQueryWrapper<ProdProp>()
                .like(StringUtils.hasText(propName), ProdProp::getPropName, propName));

        //从分页对象中获取属性记录
        List<ProdProp> prodPropList = page.getRecords();

        //判断是否有值
        if(CollectionUtil.isEmpty(prodPropList) || prodPropList.size() == 0) {
            //如果属性对象集合没有值
                //说明属性值叶为空
            return page;
        }

        //如果不为空
        //从属性对象集合中获取属性id集合
        List<Long> propIdList = prodPropList.stream().map(ProdProp::getPropId).collect(Collectors.toList());

        //属性id集合查询属性值对象集合
        List<ProdPropValue> prodPropValueList = prodPropValueMapper.selectList(new LambdaQueryWrapper<ProdPropValue>()
                .in(ProdPropValue::getPropId, propIdList));

        //循环遍历属性对象集合 --> 因为数据库存储的prodpropvalue是乱序的，
                                    //你必须要挨个根据 List<ProdProp> prodPropList = page.getRecords();的prodPropList
                                    //他的list里的prodprop的id 来去查询
                                    //因为数据库存储（in()查询）的是乱序的，你只能根据id来挨个过滤
        prodPropList.forEach(prodProp -> {
            List<ProdPropValue> propValues =
                    prodPropValueList.stream()
                            .filter(prodPropValue -> prodPropValue.getPropId().equals(prodProp.getPropId()))
                                .collect(Collectors.toList()
                            );
            prodProp.setProdPropValues(propValues);
        });

        return page;
    }

    //新增商品规格
    //1.新增商品属性对象 -> 属性id
    //2.批量添加商品属性值对象
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProdSpec(ProdProp prodProp) {
        //新增商品属性对象
        prodProp.setShopId(1L);
        prodProp.setRule(2);

        int count = prodPropMapper.insert(prodProp);

        if(count > 0) {
            //获取属性id
            Long prodId = prodProp.getPropId();
            //添加商品属性对象与属性值的记录
            //获取商品属性值集合
            List<ProdPropValue> list = prodProp.getProdPropValues();

            //判断是否有值
            if(CollectionUtil.isNotEmpty(list) && list.size() != 0) {
                //循环遍历属性值对象集合
                list.forEach(prodPropValue -> prodPropValue.setPropId(prodId));

                //批量添加属性值集合
                prodPropValueService.saveBatch(list);
            }
        }

        return count > 1;
    }
}
