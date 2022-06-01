package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionDao extends BaseDao<Permission> {

    /**
     *  查找所有权限对象
     * @return
     */
    List<Permission> findAll();

    List<Permission> findPermissionListByAdminId(Long adminId);

    List<String> findCodePermissionListByAdminId(Long adminId);

    void save(Permission permission);

    List<String> findCodeAll();
}
