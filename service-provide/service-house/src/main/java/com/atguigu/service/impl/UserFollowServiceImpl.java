package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowDao userFollowDao;

    @Override
    public boolean isFollow(Long userId, Long houseId) {
        return userFollowDao.isFollow(userId,houseId)>0;
    }

    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {
        return userFollowDao.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public void update(UserFollow userFollow) {
        userFollowDao.update(userFollow);
    }

    @Override
    public void insert(UserFollow userFollow) {
        userFollowDao.insert(userFollow);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId) {
        //在业务层开启分页
        PageHelper.startPage(pageNum,pageSize);

        Page<UserFollow> listPage = userFollowDao.findListPage(userId);

        return new PageInfo(listPage,10);
    }

}
