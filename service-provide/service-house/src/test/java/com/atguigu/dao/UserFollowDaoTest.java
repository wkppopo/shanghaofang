package com.atguigu.dao;

import com.atguigu.entity.UserFollow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springs/spring-service.xml", "classpath:springs/spring-core.xml"})
public class UserFollowDaoTest {
    @Autowired
    private UserFollowDao userFollowDao;
    @Test
    public void isFollow() {
        int follow = userFollowDao.isFollow(1L, 1L);
        System.out.println(follow);
        int follow2 = userFollowDao.isFollow(1L, 5L);
        System.out.println(follow2);

    }
}