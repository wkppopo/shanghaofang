package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AdminDao extends BaseDao<Admin> {

    List<Admin> findAll();

    Admin getAdminByUserName(String userName);
}
