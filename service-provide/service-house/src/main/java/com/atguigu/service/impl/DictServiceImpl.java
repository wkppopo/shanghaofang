package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        //1.0 调用dao根据父id查询所有数据
        List<Dict> listByParentId = dictDao.findListByParentId(id);

        //2.0 将List<Dict> 转换成list<map<String,Object>>
        List<Map<String, Object>> collect = listByParentId.stream().map(dict -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", dict.getId());
            map.put("name", dict.getName());
            map.put("isParent", dictDao.countIsParent(dict.getId())>0);
            return map;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     *  根据父节点dictCode查询子节点列表
     */
    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        //1.0 集成redis 从redis中取数据如果有返回
        Jedis jedis =null;
        try{
            jedis= jedisPool.getResource();
            String value = jedis.get("zfw:dict:parentDictCode:" + parentDictCode);
            if (!StringUtils.isEmpty(value)) {
                return JSON.parseArray(value,Dict.class);
            }
             //2.0 如果没有数据从数据库中获取
            List<Dict> list = dictDao.findDictListByParentDictCode(parentDictCode);
            if (!CollectionUtils.isEmpty(list)) {
                jedis.set("zfw:dict:parentDictCode:"+parentDictCode,JSON.toJSONString(list));
            }
            return list;
        }finally {
            if (jedis!=null){
                jedis.close();
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        //1.0 从redis中读取数据
        Jedis jedis=null;
        try{
            jedis= jedisPool.getResource();
            String value = jedis.get("zfw:dict:parentId:" + parentId);
            if (!StringUtils.isEmpty(value)) {
                return JSON.parseArray(value,Dict.class);
            }

            //2.0 从数据库中读取
            List<Dict> dict = dictDao.findListByParentId(parentId);
            if (!CollectionUtils.isEmpty(dict)) {
                jedis.set("zfw:dict:parentId:"+parentId,JSON.toJSONString(dict));
            }
            return dict;
        }finally {
            if (jedis!=null) {
                jedis.close();
                if (jedis.isConnected()) {
                    jedis.disconnect();
                }
            }
        }

    }


}
