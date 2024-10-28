package com.powernode.controller;

import com.powernode.domain.SysMenu;
import com.powernode.model.Result;
import com.powernode.service.SysMenuService;
import com.powernode.service.SysRoleService;
import com.powernode.util.AuthUtils;
import com.powernode.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 系统权限控制接口
 */

@Api(tags = "系统权限接口管理")
@RequestMapping("sys/menu")
@RestController
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;

    //sys/menu/nav
    @ApiOperation("查询用户的菜单权限和操作权限")
    @GetMapping("nav")
    public Result<MenuAndAuth> loadUserMenuAndAuth() { //不需要传参数了 因为gateway和auth已经校验过了
        //获取当前登录用户的标识
//        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        Long userId = securityUser.getUserId();
        //封装到common-core了
        //这叫面向对象编程OOP
        Long loginUserId = AuthUtils.getLoginUserId();

        //根据用户表示查询操作权限集合
        Set<String> perms = AuthUtils.getLoginUserPerms();

       //根据用户表示查询菜单集合权限
        Set<SysMenu> menus = sysMenuService.queryUserMenuListByUserId(loginUserId);

        //创建菜单和操作权限对象
        MenuAndAuth menuAndAuth = new MenuAndAuth(menus, perms);

        return Result.success(menuAndAuth);
    }


    @ApiOperation("查询系统所有权限集合")
    @GetMapping("table")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<SysMenu>> loadAllSysMenuList() {
        List<SysMenu> sysMenus = sysMenuService.queryAllSysMenuList();
        return Result.success(sysMenus);
    }

    @ApiOperation("新增权限")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result<String> saveSysMenu(@RequestBody SysMenu sysMenu) {
        Boolean saved = sysMenuService.saveSysMenu(sysMenu);
        return Result.handle(saved);
    }

    //根据标识查询菜单权限信息
    @ApiOperation("根据id查询菜单权限信息")
    @GetMapping("info/{menuId}")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public Result<SysMenu> loadSysMenuInfo(@PathVariable Long menuId) {
        SysMenu sysMenu = sysMenuService.getById(menuId);
        return Result.success(sysMenu);
    }

    @ApiOperation("修改菜单权限信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result modifySysMenu(@RequestBody SysMenu sysMenu) {
        Boolean updated = sysMenuService.modifySysMenu(sysMenu);
        return Result.handle(updated);
    }
}
