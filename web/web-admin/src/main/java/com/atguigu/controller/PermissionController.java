package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private static final String LIST_ACTION = "redirect:/permission";
    private static final String PAGE_EDIT = "permission/edit";
    @Reference
    private PermissionService permissionService;

    private static final String PAGE_CREATE = "permission/create";
    private static final String PERMISSION_INDEX = "permission/index";

    @GetMapping
    public String permission(Model model) {
        List<Permission> allPermissionList = permissionService.findAll();

        model.addAttribute("list", allPermissionList);
        return PERMISSION_INDEX;
    }

    @GetMapping("/create")
    public String create(Permission permission,Model model){
        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    @PostMapping("save")
    public String save(Model model,Permission permission){
        permissionService.save(permission);
        return successPage(model,"保存菜单成功");
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        permissionService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable ("id")Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Permission permission,Model model){
        permissionService.update(permission);
        return successPage(model,"修改菜单对象成功");
    }
}
