package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseImageDao;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.HouseImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {
    @Autowired
    private HouseImageDao houseImageDao;

    @Override
    protected BaseDao<HouseImage> getEntityDao() {
        return houseImageDao;
    }

    @Override
    public List<HouseImage> findHouseImageList(Long id, Integer type) {
        //1.0 house_id >image_id -->list
        List<HouseImage> houseImageList = houseImageDao.findHouseImageList(id, type);
        return houseImageList;
    }

}
