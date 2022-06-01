package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserInfo;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.REQUIRED)
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<UserInfo> selectAll() throws Exception {
        return userInfoDao.selectAll();
    }

    /**
     *  根据用户手机号查询用户信息
     */
    @Override
    public UserInfo getByPhone(String phone) {

        return userInfoDao.getByPhone(phone);
    }

    @Override
    public void insert(UserInfo userInfo1) {
        userInfoDao.insert(userInfo1);
    }

}
