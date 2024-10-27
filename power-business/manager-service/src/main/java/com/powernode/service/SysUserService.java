package com.powernode.service;

import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser>{

    //新增管理员
    Integer saveSysUser(SysUser sysUser);

    //查询系统管理员信息
    SysUser querySysUserInfoByUserId(Long id);

    //修改管理员信息
    Integer modifySysUserInfo(SysUser sysUser);
}
