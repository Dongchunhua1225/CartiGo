package com.powernode.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysUser;
import com.powernode.model.Result;
import com.powernode.service.SysUserService;
import com.powernode.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//系统管理员控制层
@Api("系统用户接口管理")
@RestController
@RequestMapping("sys/user")
@RequiredArgsConstructor
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("查询登录用户的信息")
    @GetMapping("info")
    public Result<SysUser> loadSysUserInfo() {
        //获取登录用户标识
        Long userId = AuthUtils.getLoginUserId();

        //根据用户标识查询登录用户信息
        SysUser sysUser = sysUserService.getById(userId);

        return Result.success(sysUser);
    }


    @ApiOperation("多条件分页查询系统管理员")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:user:page')") // --> 通过之前存放在SimpleGrantedAuthority的Set<> perms
    public Result<Page<SysUser>> loadSysUserPage(@RequestParam Long current, //分页查询，就是需要分几页的就用Page<>
                                                 @RequestParam Long size,
                                                 @RequestParam(required = false) String username) {
        //创建一个MyBatisPlus的分页对象
        Page<SysUser> page = new Page<>(current, size);

        //多条件分页查询系统管理员
        page = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
                ////                <if Test = "username != null and username != ''">
                ////                    username LIKE xxxx
                ////                </if>
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreateTime, SysUser::getUserId)
        );

        return Result.success(page);
    }


    //新增管理员
    @ApiOperation("新增管理员")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<String> saveSysUser(@RequestBody SysUser sysUser) {
        Integer count = sysUserService.saveSysUser(sysUser);
        return Result.handle(count > 0);
    }

    //查询系统管理员信息
    @ApiOperation("根据标识查询系统管理员信息")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result<SysUser> loadSysUserInfo(@PathVariable Long id) {
        SysUser sysUser = sysUserService.querySysUserInfoByUserId(id);

        return Result.success(sysUser);
    }


    @ApiOperation("修改管理员信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result modifySysUserInfo(@RequestBody SysUser sysUser) {
        Integer count = sysUserService.modifySysUserInfo(sysUser);
        return Result.handle(count > 0);
    }

    @ApiOperation("批量/单个删除管理员")
    @DeleteMapping("{userIds}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<String> removeSysUsers(@PathVariable List<Long> userIds) {
        Boolean removed = sysUserService.removeSysUserListByUserIds(userIds);
        return Result.handle(removed);
    }
}
