package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-05-17  14:13
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();


    /**
     * 根据管理员id查询roles集合 ->return map
     */
    Map<String ,List<Role>> findRolesByAdminId(Long adminId);

    /**
     * 保存用户角色
     */
    void saveAdminRole(Long adminId,List<Long> roleIdList);

}
