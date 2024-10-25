package com.powernode.service;

import com.powernode.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

public interface SysMenuService extends IService<SysMenu>{

    //根据用户表示查询菜单权限集合
    Set<SysMenu> queryUserMenuListByUserId(Long loginUserId);
}
