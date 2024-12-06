package com.powernode.service;

import com.powernode.domain.IndexImg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IndexImgService extends IService<IndexImg>{

    //新增轮播图
    Boolean saveIndexImg(IndexImg indexImg);

    //根据id查询轮播图信息
    IndexImg queryIndexImgInfoById(Long imgId);

    //修改轮播图内容
    Boolean modifyIndexImg(IndexImg indexImg);

    //批量删除轮播图
    Boolean removeIndexImgByIds(List<Long> imgIds);

    //查询小程序轮播图列表
    List<IndexImg> queryWxIndexImgList();
}
