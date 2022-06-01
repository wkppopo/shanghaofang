package com.atguigu.base;

import com.atguigu.util.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public abstract class BaseServiceImpl<T> {
    //获取mapper对象
    protected abstract BaseDao<T> getEntityDao();


    public void insert(T t) {
        getEntityDao().insert(t);
    }

    public T getById(Long id) {
        return getEntityDao().getById(id);
    }

    public void update(T role) {
        getEntityDao().update(role);
    }

    public void delete(Long id) {
        getEntityDao().delete(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<T> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);
        //开启分页
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<T>(getEntityDao().findPage(filters), 10);

    }



}
