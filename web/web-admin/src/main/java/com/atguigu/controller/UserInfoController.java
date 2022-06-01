package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/rest/selectAll")
    public String SelectAll(Model model) throws Exception {
        List<UserInfo> list = userInfoService.selectAll();
        model.addAttribute("lists",list);

        return "index";
    }


}
