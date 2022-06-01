package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;

    /**
     *  关注房源功能    拦截器会拦截这个方法
     */
    @GetMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpSession httpSession) {
        //1.0 能进入这个方法说明 用户一定登录成功了
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("USER");

        //2.0 根据用户id和houseId 查看关注表中是否有数据
        UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), houseId);
        if (userFollow != null) {
            //表示关注过 被删了
            userFollow.setIsDeleted(0);
            userFollowService.update(userFollow);
        }else{
            userFollow = new UserFollow();
            userFollow.setHouseId(houseId);
            userFollow.setUserId(userInfo.getId());

            userFollowService.insert(userFollow);
        }

        return Result.ok();
    }

    /**
     *  查看关注列表功能  拦截器会拦截这个方法
     */
    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum, pageSize, userInfo.getId());
        return Result.ok(pageInfo);
    }

    /**
     *  取消关注功能  拦截器会拦截这个方法
     *      id :关注表的id
     */
    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        //创建UserFollow对象
        UserFollow userFollow = new UserFollow();
        userFollow.setId(id);
        userFollow.setIsDeleted(1);
        //调用业务层的方法修改
        userFollowService.update(userFollow);

        return Result.ok();
    }
}
