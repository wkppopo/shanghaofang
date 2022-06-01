package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.DictCode;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.entity.House;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;


    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        //处理pageNum和pageSize为空的情况
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }

        //分页搜索房源列表信息
        PageInfo<House> pageInfo = houseService.findPage(filters);
        model.addAttribute("page", pageInfo);

        model.addAttribute("filters", filters);

        //查询所有小区，以及字典里的各种列表存储请求域
        saveAllDictToRequestScope(model);

        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create(Model model){
        //查询房屋初始化信息存储到model中
        saveAllDictToRequestScope(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(House house,Model model){
        //未发布
        house.setStatus(HouseStatus.UNPUBLISHED.getCode());

        houseService.insert(house);

        return successPage(model,"保存信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id , Model model){
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        saveAllDictToRequestScope(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(House house,Model model){
        houseService.update(house);
       return successPage(model,"修改房源信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,Model model){
        houseService.delete(id);
        return LIST_ACTION;
    }

    //发布房源
    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        houseService.publish(id,status);
        return LIST_ACTION;
    }

    //查询房源详情
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,Model model){
        //1.0 查询房源详情
        House house=houseService.getById(id);
        model.addAttribute("house",house);

        //2.0查询小区详情
        model.addAttribute("community",communityService.getById(house.getCommunityId()));

        //3.0 查询房源图片列表
        model.addAttribute("houseImage1List",houseImageService.findHouseImageList(id,1));

        //4.0 查询房产图片列表
        model.addAttribute("houseImage2List",houseImageService.findHouseImageList(id,2));

        //5.0 查询经纪人列表
        model.addAttribute("houseBrokerList",houseBrokerService.findHouseBrokerListByHouseId(id));

        //6.0 查询房东列表
        model.addAttribute("houseUserList",houseUserService.findHouseUserListByHouseId(id));

        return PAGE_SHOW;
    }




    /**
     * 查询所有小区以及字典里的各种列表存储到请求域
     *
     * @param model
     */
    private void saveAllDictToRequestScope(Model model) {
        //2. 查询所有小区
        List<Community> communityList = communityService.findAll();
        //3. 查询各种初始化列表:户型列表、楼层列表、装修情况列表....
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(DictCode.HOUSETYPE.getMessage());
        List<Dict> floorList = dictService.findDictListByParentDictCode(DictCode.FLOOR.getMessage());
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(DictCode.BUILDSTRUCTURE.getMessage());

        List<Dict> directionList = dictService.findDictListByParentDictCode(DictCode.DIRECTION.getMessage());
        List<Dict> decorationList = dictService.findDictListByParentDictCode(DictCode.DECORATION.getMessage());
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(DictCode.HOUSEUSE.getMessage());
        //5. 将所有小区存储到请求域
        model.addAttribute("communityList", communityList);
        //6. 将各种列表存储到请求域
        model.addAttribute("houseTypeList", houseTypeList);
        model.addAttribute("floorList", floorList);
        model.addAttribute("buildStructureList", buildStructureList);
        model.addAttribute("directionList", directionList);
        model.addAttribute("decorationList", decorationList);
        model.addAttribute("houseUseList", houseUseList);
    }


}


