package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService {
    boolean isFollow(Long userId, Long houseId);

    UserFollow findByUserIdAndHouseId(Long userId,Long houseId);

    void update(UserFollow userFollow);

    void insert(UserFollow userFollow);

    PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId);
}
