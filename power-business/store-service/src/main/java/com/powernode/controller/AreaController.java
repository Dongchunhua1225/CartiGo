package com.powernode.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.Area;
import com.powernode.model.Result;
import com.powernode.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区业务模块控制层
 */
@Api(tags = "地区业务接口管理")
@RequestMapping("admin/area")
@RestController
@RequiredArgsConstructor
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 查询全国地区列表
     *
     * @return
     */
    @ApiOperation("查询全国地区列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('admin:area:list')")
    public Result<List<Area>> loadAllAreaList() {
        List<Area> list = areaService.queryAllAreaList();
        return Result.success(list);
    }

    //////////////////////////// 微信小程序 接口 //////////////////////////////

    /**
     * 根据地区父节点标识查询子节点集合
     *
     * @param pid 地区父节点id
     * @return
     */
    @ApiOperation("根据地区父节点标识查询子节点集合")
    @GetMapping("mall/listByPid")
    public Result<List<Area>> loadMallAreaListByPid(@RequestParam Long pid) {
        List<Area> list = areaService.list(new LambdaQueryWrapper<Area>().eq(Area::getParentId, pid));

        return Result.success(list);
    }
}
