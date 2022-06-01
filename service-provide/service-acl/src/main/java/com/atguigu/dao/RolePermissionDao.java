package com.atguigu.dao;


import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    /**
     * 根据role_id 查询出所有已分配的权限列表
     *
     * @param roleId
     * @return
     */
    List<Long> findPermissionIdListByRoleId(Long roleId);

    void removeRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("removeCollects") List<Long> removeCollects);
}
