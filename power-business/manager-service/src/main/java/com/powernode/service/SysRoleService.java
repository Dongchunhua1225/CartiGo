package com.powernode.service;

import com.powernode.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole>{

    //查询系统所有角色
    List<SysRole> querySysRoleList();

    //新增角色
    Boolean saveSysRole(SysRole sysRole);

    //根据标识查询角色信息
    SysRole querySysRoleInfoByRoleId(Long roleId);
}
