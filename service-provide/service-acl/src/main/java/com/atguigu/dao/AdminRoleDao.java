package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleDao extends BaseDao<AdminRole> {
    /**
     * 根据管理员Id查看角色id列表
     */
    List<Long> findRoleIdListByAdminId(Long adminId);

    /**
     * 查看admin是否包含这条角色信息
     */
    AdminRole findByAdminIdAndRoleId(@Param("adminId")Long adminId,@Param("roleId") Long roleId);


    void removeAdminRole(@Param("adminId") Long adminId,@Param("roleIds") List<Long> roleIds);
}
