package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    /**
     * 根据角色获取权限数据集合
     */
    List<Map<String ,Object>> findPermissionByRoleId(Long roleId);

    /**
     * 根据roleId 和 permissionId 保存 角色权限数据
     */
    void saveRolePermission(Long roleId,List<Long> permissionIds);

    List<Permission> findMenuPermissionByAdminId(Long adminId);

    List<Permission> findAll();

    List<String> findCodePermissionListByAdminId(Long adminId);

    void save(Permission permission);
}
