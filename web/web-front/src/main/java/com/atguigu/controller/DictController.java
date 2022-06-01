package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Reference
    private DictService dictService;

    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> dictListByParentDictCode = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictListByParentDictCode);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long id){
        List<Dict> dictListByParentId = dictService.findDictListByParentId(id);
        return Result.ok(dictListByParentId);
    }

}
