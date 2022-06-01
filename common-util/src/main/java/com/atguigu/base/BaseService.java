package com.atguigu.base;


import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface BaseService<T> {
    /**
     * 保存角色信息
     * @param role
     */
    void insert(T role);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改角色信息
     * @param role
     */
    void update(T role);

    void delete(Long id);

    /**
     * 分页查询
     */
    PageInfo<T> findPage(Map<String,Object> filters);
}
