package com.powernode.service;

import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser>{

    //新增管理员
    Integer saveSysUser(SysUser sysUser);
}
