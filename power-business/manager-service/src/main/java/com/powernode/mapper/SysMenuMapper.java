package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.SysMenu;

import java.util.Set;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //根据用户标识查询菜单权限集合
    Set<SysMenu> selectUserMenuListByUserId(Long loginUserId);
}