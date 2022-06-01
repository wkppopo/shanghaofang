package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.util.FileUtil;
import com.atguigu.util.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String PAGE_INDEX="admin/index";
    private final static String PAGE_ASSIGN_SHOW="admin/assignShow";


    //保存admin的新的角色信息
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,@RequestParam("roleIds") List<Long> roleIds,Model model){
        roleService.saveAdminRole(adminId,roleIds);
        return successPage(model,"保存角色信息成功!");
    }

    //访问分配角色列表和未分配列表
    @GetMapping("/assignShow/{adminId}")
    public String assignShow(@PathVariable("adminId") Long adminId,Model model){
        Map<String, List<Role>> roleMap = roleService.findRolesByAdminId(adminId);

        model.addAttribute("assignRoleList",roleMap.get("assignRoleList"));
        model.addAttribute("unAssignRoleList",roleMap.get("unAssignRoleList"));
        return PAGE_ASSIGN_SHOW;
    }


    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters , Model model){
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);

        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Admin admin, Model model){
        //1.0 加密用户的密码
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.insert(admin);
        return successPage(model,"新增用户成功");
    }

    private final static String PAGE_EDIT = "admin/edit";
    private final static String PAGE_UPLOAD_SHOW = "admin/upload";


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Admin admin,Model model){
        adminService.update(admin);

        return successPage(model,"编辑用户成功");
    }

    private final static String LIST_ACTION= "redirect:/admin";
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        adminService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id ,
                             Model model){
        model.addAttribute("id",id);

        return PAGE_UPLOAD_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file") MultipartFile multipartFile,
                         Model model) throws IOException {

        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());

        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uuidName);

        String url = QiniuUtils.getUrl(uuidName);

        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);

        adminService.update(admin);

        return successPage(model,"头像上传成功");
    }
}
