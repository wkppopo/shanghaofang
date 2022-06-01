package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {
    @Reference
    private AdminService adminService;
    @Reference
    private HouseBrokerService houseBrokerService;

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";

    @GetMapping("/create")
    public String create(HouseBroker houseBroker, Model model) {
        //查询管理员列表
        saveAdminListToModel(model);
        model.addAttribute("houseBroker", houseBroker);
        return PAGE_CREATE;
    }

    private void saveAdminListToModel(Model model) {
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
    }

    @PostMapping("/save")
    public String save(HouseBroker houseBroker, Model model) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);

        return successPage(model, "添加经纪人成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        //根据经纪人id查询一条记录，并共享
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker", houseBroker);
        saveAdminListToModel(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(HouseBroker houseBroker, Model model) {
        //根据id查询经纪人信息
        HouseBroker originHouseBroker = houseBrokerService.getById(houseBroker.getBrokerId());

        //更新originHouseBroker的信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());

        originHouseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        originHouseBroker.setBrokerName(admin.getName());

        //修改经纪人信息
        houseBrokerService.update(originHouseBroker);
        return successPage(model, "修改经纪人成功");
    }

    private static final String SHOW_ACTION = "redirect:/house/";

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("id") Long id) {
        houseBrokerService.delete(id);

        return SHOW_ACTION + houseId;
    }
}
