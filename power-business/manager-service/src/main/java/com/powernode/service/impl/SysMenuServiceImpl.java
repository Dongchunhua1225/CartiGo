package com.powernode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysMenu;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.SysMenuServiceImpl")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    @Cacheable(key = "#loginUserId")
    public Set<SysMenu> queryUserMenuListByUserId(Long loginUserId) {
        Set<SysMenu> menus = sysMenuMapper.selectUserMenuListByUserId(loginUserId);

        //将拿到的菜单权限集合转换为树结构（既数据结构应该转换为层级关系）
        return transformTree(menus, 0L);
    }


    /**
     * 将集合转换为树结构
     * @return
     */
        //1. 已知菜单深度 <= 2
//    private Set<SysMenu> transformTree(Set<SysMenu> menus, long pid) {
//        //从菜单集合中获取根节点集合
//        Set<SysMenu> roots = menus.stream()
//                .filter(m -> m.getParentId().equals(pid))
//                .collect(Collectors.toSet());
//
//        //循环遍历根节点集合
//        roots.forEach(root -> {
//            //从菜单集合中过滤出 其父节点val和当前根节点的id值一直的菜单集合
//           Set<SysMenu> child = menus.stream()
//                   .filter(m -> m.getParentId().equals(root.getMenuId()))
//                   .collect(Collectors.toSet());
//
//           root.setList(child);
//        });
//
//        return roots;
//    }

    //2. 未知菜单深度
    private Set<SysMenu> transformTree(Set<SysMenu> menus, long pid) {
        //从菜单集合中获取根节点集合
        Set<SysMenu> roots = menus.stream()
                .filter(m -> m.getParentId().equals(pid))
                .collect(Collectors.toSet());

        //循环节点集合
        //小小递归拿下
        roots.forEach(r -> r.setList(transformTree(menus, r.getMenuId())));

        return roots;
    }
}
