package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.ManagerConstant;
import com.powernode.domain.SysMenu;
import com.powernode.ex.handler.BusinessException;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    @Override
    @Cacheable(key = ManagerConstant.SYS_ALL_MENU_KEY)
    public List<SysMenu> queryAllSysMenuList() {
        return baseMapper.selectList(null);
    }

    //新增权限
    @Override
    @CacheEvict(key = ManagerConstant.SYS_ALL_MENU_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu) > 0;
    }

    //修改菜单权限信息
    @Override
    @CacheEvict(key = ManagerConstant.SYS_ALL_MENU_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifySysMenu(SysMenu sysMenu) {
        //获取菜单类型
        Integer type = sysMenu.getType();

        if(type == 0) {
            sysMenu.setParentId(0L);
        }
        return sysMenuMapper.updateById(sysMenu) > 0;
    }

    //删除菜单权限
    @Override
    @CacheEvict(key = ManagerConstant.SYS_ALL_MENU_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeSysMenuById(Long menuId) {
        //根据菜单id查询子菜单集合
        List<SysMenu> list = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, menuId));

        if(CollectionUtil.isNotEmpty(list) && list.size() != 0) {
            //包含子节点 --> 不允许删除
            //不包含 --> 允许删除
            throw new BusinessException("当前菜单包含子菜单，不允许删除");
        }

        return sysMenuMapper.deleteById(menuId) > 0;
    }


    /**
     * 将集合转换为树结构
     * @return
     */
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