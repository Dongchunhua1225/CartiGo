package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysRole;
import com.powernode.model.Result;
import com.powernode.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//系统角色管理层
@Api(tags = "系统角色接口管理")
@RestController
@RequestMapping("sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("查询系统所有角色")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<List<SysRole>> loadSysRoleList() {
        List<SysRole> roleList = sysRoleService.querySysRoleList();
        return Result.success(roleList);
    }


    @ApiOperation("多条件分页查询角色列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:role:page')")
    public Result<Page<SysRole>> loadSysRolePage(@RequestParam Long current, @RequestParam Long size,
                                                 @RequestParam(required = false) String roleName) {
        Page<SysRole> page = new Page<>(current, size);
        page = sysRoleService.page(page, new LambdaQueryWrapper<SysRole>()
                .like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName) // 只要是用户输入的 就是模糊查询
                .orderByDesc(SysRole::getCreateTime));
        return Result.success(page);
    }

    //新增角色
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        Boolean saved = sysRoleService.saveSysRole(sysRole);
        return Result.success(saved);
    }

    @ApiOperation("根据标识查询角色信息")
    @GetMapping("info/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result<SysRole> loadSysRoleInfo(@PathVariable Long roleId) {
        SysRole sysRole = sysRoleService.querySysRoleInfoByRoleId(roleId);
        return Result.success(sysRole);
    }


}
