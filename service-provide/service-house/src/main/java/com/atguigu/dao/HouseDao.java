package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HouseDao extends BaseDao<House> {

    List<House> findAll();

    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
