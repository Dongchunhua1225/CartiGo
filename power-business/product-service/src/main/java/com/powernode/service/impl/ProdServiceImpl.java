package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Prod;
import com.powernode.domain.ProdTagReference;
import com.powernode.domain.Sku;
import com.powernode.mapper.ProdMapper;
import com.powernode.mapper.ProdTagReferenceMapper;
import com.powernode.mapper.SkuMapper;
import com.powernode.service.ProdService;
import com.powernode.service.ProdTagReferenceService;
import com.powernode.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    @Autowired
    private ProdTagReferenceMapper prodTagReferenceMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuMapper skuMapper;

    //新增商品
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProd(Prod prod) {
        //新增商品
        prod.setShopId(1L);
        prod.setSoldNum(0);
        prod.setCreateTime(new Date());
        prod.setUpdateTime(new Date());
        prod.setPutawayTime(new Date());
        prod.setVersion(0);
        //设置配送方式
        Prod.DeliveryModeVo mode = prod.getDeliveryModeVo();
        prod.setDeliveryMode(JSONObject.toJSONString(mode));

        int count = prodMapper.insert(prod);
        //如果count > 0，说明商品添加成功了
        //成功的花就要去处理别的表的关系
        //因为这是一个多表操作

        if (count > 0) {
            Long prodId = prod.getProdId();
            //处理商品与分组标签的关系
            List<Long> tagIdList = prod.getTagList();
            //判断有没有值
            if (CollectionUtil.isNotEmpty(tagIdList) && tagIdList.size() != 0) {
                //创建商品与分组标签集合
                List<ProdTagReference> prodTagReferenceList = new ArrayList<>();
                //循环遍历分组标签id
                tagIdList.forEach(tagId -> {
                    //每循环一次，创建商品和分组标签的关系记录
                    ProdTagReference prodTagReference = new ProdTagReference();
                    prodTagReference.setProdId(prodId);
                    prodTagReference.setTagId(tagId);
                    prodTagReference.setCreateTime(new Date());
                    prodTagReference.setShopId(1L);
                    prodTagReference.setStatus(1);
                    prodTagReferenceList.add(prodTagReference);
                });
                //批量添加商品与分组标签的关系记录
                prodTagReferenceService.saveBatch(prodTagReferenceList);
            }

            // 处理商品与商品sku的关系
            // 获取商品sku对象集合
            List<Sku> skuList = prod.getSkuList();
            // 判断是否有值
            if (CollectionUtil.isNotEmpty(skuList) && skuList.size() != 0) {
                // 循环遍历商品sku对象集合
                skuList.forEach(sku -> {
                    sku.setProdId(prodId);
                    sku.setCreateTime(new Date());
                    sku.setUpdateTime(new Date());
                    sku.setVersion(0);
                    sku.setActualStocks(sku.getStocks());
                });
                // 批量添加商品sku对象集合
                skuService.saveBatch(skuList);
            }
        }

        return count > 0;
    }

    //根据标识查询商品详情
    @Override
    public Prod queryProdInfoById(Long prodId) {
        //先查商品详情
        Prod prod = prodMapper.selectById(prodId);

        //查询他和分组标签关系的集合（bridge table）
        List<ProdTagReference> prodTagReferenceList = prodTagReferenceMapper.selectList(new LambdaQueryWrapper<ProdTagReference>()
                .eq(ProdTagReference::getProdId, prodId));
        //判断是否有值
        if(CollectionUtil.isNotEmpty(prodTagReferenceList) && prodTagReferenceList.size() != 0) {
            //从集合中获取分组标签id集合
            List<Long> tagIdList = prodTagReferenceList.stream()
                    .map(ProdTagReference::getTagId).collect(Collectors.toList());
            prod.setTagList(tagIdList);
        }

        //查询sku
        List<Sku> skuList = skuMapper.selectList(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getProdId, prodId));
        prod.setSkuList(skuList);

        return prod;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyProdInfo(Prod prod) {
        prod.setUpdateTime(new Date());

        // 获取商品标识
        Long prodId = prod.getProdId();
        // 删除商品原有的与分组标签的关系
        prodTagReferenceMapper.delete(new LambdaQueryWrapper<ProdTagReference>()
                .eq(ProdTagReference::getProdId,prodId));

        //处理商品与分组标签的关系
        List<Long> tagIdList = prod.getTagList();
        //判断有没有值
        if (CollectionUtil.isNotEmpty(tagIdList) && tagIdList.size() != 0) {
            //创建商品与分组标签集合
            List<ProdTagReference> prodTagReferenceList = new ArrayList<>();
            //循环遍历分组标签id
            tagIdList.forEach(tagId -> {
                //每循环一次，创建商品和分组标签的关系记录
                ProdTagReference prodTagReference = new ProdTagReference();
                prodTagReference.setProdId(prodId);
                prodTagReference.setTagId(tagId);
                prodTagReference.setCreateTime(new Date());
                prodTagReference.setShopId(1L);
                prodTagReference.setStatus(1);
                prodTagReferenceList.add(prodTagReference);
            });
            //批量添加商品与分组标签的关系记录
            prodTagReferenceService.saveBatch(prodTagReferenceList);
        }

        //批量修改商品sku对象集合
        //获取商品sku对象集合
        List<Sku> skuList = prod.getSkuList();
        //寻呼那遍历商品sku集合
        skuList.forEach(sku -> {
            sku.setUpdateTime(new Date());
            sku.setActualStocks(sku.getStocks());
        });
        //批量修改sku
        skuService.updateBatchById(skuList);

        return prodMapper.updateById(prod) > 0;
    }
}
