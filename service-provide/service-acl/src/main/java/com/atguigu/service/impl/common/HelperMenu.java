package com.atguigu.service.impl.common;

import com.atguigu.entity.Permission;

import java.util.ArrayList;
import java.util.List;

public class HelperMenu {
    public static List<Permission> build(List<Permission> originalPermissionList){
        //原始的权限集合中获取父权限对象
        List<Permission> permissions = new ArrayList<>();
        for (Permission parentPermission : originalPermissionList) {
            if (parentPermission.getParentId()==0) {
                //表示这个权限对象是根对象，需要设置level和child
                parentPermission.setLevel(1);
                parentPermission.setChildren(getChild(parentPermission,originalPermissionList));
                permissions.add(parentPermission);
            }
        }
        return permissions;
    }

    /**
     *  子菜单跟父菜单不一样多出来一个字段就是 parentName;
     */
    private static List<Permission> getChild(Permission parentPermission, List<Permission> originalPermissionList) {
        //1.0 以parentPermission对象的id为参考，将能对上的，遍历出来的所有子对象的parent_id 的对象设置为子对象
        List<Permission> child=new ArrayList<>();
        for (Permission permission : originalPermissionList) {
            if (parentPermission.getId().equals(permission.getParentId())) {
                //1.0 父 id == parent_id 的所有permission对象
                permission.setLevel(parentPermission.getLevel()+1);
                permission.setParentName(parentPermission.getName());
                permission.setChildren(getChild(permission,originalPermissionList));

                child.add(permission);
            }
        }
        return child;
    }
}
