package com.atguigu.dao;

import com.atguigu.entity.UserFollow;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;


public interface UserFollowDao {
    int isFollow(@Param("userId") Long userId, @Param("houseId") Long houseId);

    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    void update(UserFollow userFollow);

    void insert(UserFollow userFollow);

    /**
     * 分页查询用户关注列表信息
     */
    Page<UserFollow> findListPage(@Param("userId") Long userId);
}
