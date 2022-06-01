package com.atguigu.service;


import com.atguigu.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> selectAll()throws Exception;

    UserInfo getByPhone(String phone);

    void insert(UserInfo userInfo1);
}
