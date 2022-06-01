package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserInfo;

import java.util.List;

public interface UserInfoDao extends BaseDao<UserInfo> {

    List<UserInfo> selectAll()throws Exception;
    /**
     * 根据用户手机号查询用户信息，如果手机号已存在，则不能注册
     *
     */
    UserInfo getByPhone(String phone);
}
