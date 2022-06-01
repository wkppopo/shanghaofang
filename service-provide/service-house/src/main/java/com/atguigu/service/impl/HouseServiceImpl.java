package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House>  implements HouseService {
    @Autowired
    private HouseDao houseDao;

    @Override
    protected BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseDao.update(house);
    }

    /**
     * 前台页面的首页分页显示
     * @param pageNum
     * @param pageSize
     * @param houseQueryBo
     * @return
     */
    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo) {

        PageHelper.startPage(pageNum,pageSize);
        Page<HouseVo> page = houseDao.findListPage(houseQueryBo);

        return new PageInfo<>(page);
    }
}
