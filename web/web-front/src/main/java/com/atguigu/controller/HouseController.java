package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private UserFollowService userFollowService;
    @Reference
    private HouseImageService houseImageService;

    @ResponseBody
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable("pageNum") Integer pageNum,
                       @PathVariable("pageSize") Integer pageSize,
                       @RequestBody HouseQueryBo houseQueryBo){
        PageInfo<HouseVo
                > listPage = houseService.findListPage(pageNum, pageSize, houseQueryBo);
        return Result.ok(listPage);
    }

    @ResponseBody
    @GetMapping("/info/{houseId}")
    public Result info(@PathVariable("houseId") Long houseId, HttpSession httpSession){
        //1.0 根据房源id查询房源信息
        House house = houseService.getById(houseId);
        //2.0 根据房源中的小区id查询小区信息
        Community community = communityService.getById(house.getCommunityId());
        //3.0 根据房源id查询经纪人信息
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        //4.0 根据房源id房源image信息列表
        List<HouseImage> houseImageList = houseImageService.findHouseImageList(houseId, 1);
        //5.0 默认不关注
        //从httpsession中获取用户信息
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("USER");
        boolean isFollow=false;
        if (userInfo!=null) {
            //1.0 获取用户id
            Long userId = userInfo.getId();
            isFollow = userFollowService.isFollow(userId, houseId);

        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImageList);
        map.put("isFollow",isFollow);

        return Result.ok(map);
    }
}
