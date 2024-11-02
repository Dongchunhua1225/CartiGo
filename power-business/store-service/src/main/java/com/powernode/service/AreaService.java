package com.powernode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.Area;

import java.util.List;

public interface AreaService extends IService<Area>{

    //查询全国地区列表
    List<Area> queryAllAreaList();
}
