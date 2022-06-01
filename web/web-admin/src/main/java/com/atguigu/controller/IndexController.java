package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;

    private static final String PAGE_INDEX = "frame/index";

    @GetMapping
    public String index(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.getAdminByUserName(user.getUsername());
        model.addAttribute("admin",admin);

        //1.0 根据用户id查询菜单权限列表
        List<Permission> permissionList=permissionService.findMenuPermissionByAdminId(admin.getId());

        //将菜单结构数据共享到Model中
        model.addAttribute("permissionList",permissionList);

        return PAGE_INDEX;
    }
}
