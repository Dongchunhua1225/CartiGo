package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.domain.SysUserRole;
import com.powernode.mapper.SysUserMapper;
import com.powernode.mapper.SysUserRoleMapper;
import com.powernode.service.SysUserRoleService;
import com.powernode.service.SysUserService;
import com.powernode.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    //新增管理员
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveSysUser(SysUser sysUser) {
        //1.新增管理员
        sysUser.setCreateUserId(AuthUtils.getLoginUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setShopId(1L);

        //2.新增管理员与角色的关系
        int count = sysUserMapper.insert(sysUser);
        if (count > 0) {
            //获取管理员标识
            Long userId = sysUser.getUserId();

            //获取管理员的角色id集合
            List<Long> roleIdList = sysUser.getRoleIdList();
            //判断是否有值
            if(CollectionUtil.isNotEmpty(roleIdList) && roleIdList.size() != 0) {
                //船舰一个管理员与角色关系的集合
                List<SysUserRole> sysUserRoleList = new ArrayList<>();

                //循环遍历角色id集合
                roleIdList.forEach(roleId -> {
//                    创建管理员与角色的关系
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(userId);
                    sysUserRole.setRoleId(roleId);
                    //新增管理员与角色的关系
                    //sysUserRoleMapper.insert(sysUserRole);
                    //BUTTTTTTTTTTTTTTTTTTTTT:不建议在循环中操作数据库
                    //尽量需要避免在循环中操作数据库

                    sysUserRoleList.add(sysUserRole);
                });

                //批量操作
                sysUserRoleService.saveBatch(sysUserRoleList);
            }
        }
        return count;
    }
}
