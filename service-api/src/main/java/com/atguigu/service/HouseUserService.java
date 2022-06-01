package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseUser;
import com.atguigu.entity.UserInfo;

import java.util.List;

public interface HouseUserService extends BaseService<HouseUser> {
    List<HouseUser> findHouseUserListByHouseId(Long houseId);

}
