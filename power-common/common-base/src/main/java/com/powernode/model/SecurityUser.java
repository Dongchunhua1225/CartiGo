package com.powernode.model;

//Security安全框架认识的安全用户对象

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    //商城后台管理系统用户的相关属性
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;
    private String loginType;
    private Set<String> perms = new HashSet<>();

    //商城购物系统会员的相关属性

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        //这个值，在Security框架中不可以重复
        return this.loginType + this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }

    //权限有可能涉及到一个用户拥有多个权限
    //这时候就要对这些权限进行分割
    //example: "Permission 1, Permission2"
    public Set<String> setPerms() {
        HashSet<String> finalPermsSet = new HashSet<>();
        //循环便利用户权限集合
        perms.forEach(perm -> {
            //判断是否包含括号
            if(perms.contains(",")) {
                //包含的话就得分割
                String[] curr = perm.split(",");

                //循环这个当前权限arr
                for(String currPerms : curr) {
                    finalPermsSet.add(currPerms);
                }

            } else {
                //不包含的话直接加
                finalPermsSet.add(perm);
            }
        });

        return finalPermsSet;
    }


//    public void setPerms(Set<String> perms) {
//        HashSet<String> finalPermsSet = new HashSet<>();
//        perms.forEach(perm -> {
//            // 判断权限记录是否包含","号
//            if (perm.contains(",")) {
//                // 根据","号分隔
//                String[] realPerms = perm.split(",");
//                // 循环遍历获取每一个权限，存放到最终权限集合中
//                for (String realPerm : realPerms) {
//                    finalPermsSet.add(realPerm);
//                }
//            } else {
//                finalPermsSet.add(perm);
//            }
//        });
//        this.perms = finalPermsSet;
//    }

}
