package com.atguigu.dao;

import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao {
    //根据父节点ID查询子节点列表
    List<Dict> findListByParentId(Long parentId);

    //判断是否是父节点
    Integer countIsParent(Long id);

    //根据父节点的dictCode查询子节点列表
    List<Dict> findDictListByParentDictCode(String parentDictCode);
}
