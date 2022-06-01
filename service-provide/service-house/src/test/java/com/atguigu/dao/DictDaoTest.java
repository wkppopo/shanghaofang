package com.atguigu.dao;

import com.atguigu.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springs/spring-service.xml", "classpath:springs/spring-core.xml"})
public class DictDaoTest {
    @Autowired
    private DictDao dictDao;

    @Test
    public void findListByParentId() {
        List<Dict> listByParentId = dictDao.findListByParentId(10000L);
        System.out.println(listByParentId);
    }

    @Test
    public void countIsParent() {
        Integer integer = dictDao.countIsParent(10000L);
        System.out.println(integer);
    }

    @Test
    public void findDictListByParentDictCode() {
        List<Dict> list = dictDao.findDictListByParentDictCode("beijing");
        System.out.println(list);
    }
}