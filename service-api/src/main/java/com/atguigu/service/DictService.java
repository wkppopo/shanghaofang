package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    List<Map<String,Object>> findZnodes(Long id);

    /**
     * 根据dictCode查询dict集合
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);

    List<Dict> findDictListByParentId(Long parentId);
}
