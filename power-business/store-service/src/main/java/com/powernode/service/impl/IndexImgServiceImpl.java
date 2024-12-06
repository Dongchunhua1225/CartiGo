package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.StoreConstants;
import com.powernode.domain.IndexImg;
import com.powernode.domain.Prod;
import com.powernode.ex.handler.BusinessException;
import com.powernode.feign.StoreProdFeign;
import com.powernode.mapper.IndexImgMapper;
import com.powernode.model.Result;
import com.powernode.service.IndexImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.IndexImgServiceImpl")
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService{

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Autowired
    private StoreProdFeign storeProdFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean saveIndexImg(IndexImg indexImg) {
        indexImg.setShopId(1L);
        indexImg.setCreateTime(new Date());
        //获取关联类型
        Integer type = indexImg.getType();
        if(type == -1) {
            //说明轮播图未关联商品
            indexImg.setProdId(null);
        }

        return indexImgMapper.insert(indexImg) > 0;

    }

    //根据id查询轮播图信息
    @Override
    public IndexImg queryIndexImgInfoById(Long imgId) {
        // 根据标识查询轮播图信息
        IndexImg indexImg = getById(imgId);
        // 获取轮播图关联类型
        Integer type = indexImg.getType();
        if(type == 0) {
            //档期那轮播图关联商品
            //获取关联商品id
            Long prodId = indexImg.getProdId();

            //通过远程调度feign: 根据商品id查询商品图片和名称
            Result<List<Prod>> result = storeProdFeign.getProdListByIds(Arrays.asList(prodId));

            //判断是否正确
            if(BusinessEnum.OPERATION_FAIL.getCode().equals(result.getCode())) {
                //如果错误码相等，说明操作失败
                throw new BusinessException(result.getMsg());
            }
            //没有走哪个if，说明成功了
            List<Prod> prods = result.getData();

            //判断集合是否有值
            if(CollectionUtil.isNotEmpty(prods) && prods.size() != 0) {
                //有值，获取商品对象
                Prod prod = prods.get(0);  //因为只有一个值所以其实在这里就get一个就行，如果有很多就得循环遍历
                indexImg.setPic(prod.getPic());
                indexImg.setProdName(prod.getProdName());
            }
        }

        return indexImg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean modifyIndexImg(IndexImg indexImg) {
        return indexImgMapper.updateById(indexImg) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean removeIndexImgByIds(List<Long> imgIds) {
        return indexImgMapper.deleteBatchIds(imgIds) == imgIds.size();
    }

    //查询小程序轮播图列表
    @Override
    @Cacheable(key = StoreConstants.WX_INDEX_IMG_KEY)
    public List<IndexImg> queryWxIndexImgList() {
        return indexImgMapper.selectList(new LambdaQueryWrapper<IndexImg>()
                .eq(IndexImg::getStatus, 1)
                .orderByDesc(IndexImg::getSeq));
    }
}
