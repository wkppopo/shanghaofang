package com.atguigu.service.impl;

import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springs/spring-service.xml", "classpath:springs/spring-core.xml"})
public class UserInfoServiceImplTest {
    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void getByPhone() {
        UserInfo byPhone = userInfoDao.getByPhone("18989898989");
        System.out.println(byPhone);
    }
}