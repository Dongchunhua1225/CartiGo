package com.powernode.service;

import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser>{

    //新增管理员
    Integer saveSysUser(SysUser sysUser);

    //查询系统管理员信息
    SysUser querySysUserInfoByUserId(Long id);

    //修改管理员信息
    Integer modifySysUserInfo(SysUser sysUser);

    //批量/单个删除管理员
    Boolean removeSysUserListByUserIds(List<Long> userIds);
}
