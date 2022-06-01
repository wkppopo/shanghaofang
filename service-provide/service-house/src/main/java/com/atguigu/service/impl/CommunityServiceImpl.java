package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommunityDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityDao communityDao;

    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    /**
     *  查找所有房源
     */
    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    }
}
