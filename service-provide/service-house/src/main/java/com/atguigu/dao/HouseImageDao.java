package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageDao extends BaseDao<HouseImage> {
    List<HouseImage> findHouseImageList(@Param("houseId") Long houseId,@Param("type") Integer type);

}
