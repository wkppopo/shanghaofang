package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;


    private static final String PAGE_INDEX = "role/index";
    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_EDIT = "role/edit";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";


    /**
     *  根据角色Id 保存role_id permission_id 表中的数据
     */
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @PostMapping("/assignPermission")
    public String assignPermission(Long roleId,@RequestParam("permissionIds") List<Long> permissionIds,Model model){
        //1.0 调用权限服务 保存
        permissionService.saveRolePermission(roleId,permissionIds);
        return successPage(model,"保存角色成功");
    }

    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @GetMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable("roleId") Long roleId,Model model){
        List<Map<String, Object>> permissionByRoleId = permissionService.findPermissionByRoleId(roleId);

        model.addAttribute("zNodes", JSON.toJSONString(permissionByRoleId));
        model.addAttribute("roleId",roleId);
        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(Map filters, Model model){
        if(!filters.containsKey("pageNum")){
            filters.put("pageNum",1);
        }
        if(!filters.containsKey("pageSize")){
            filters.put("pageSize",10);
        }
        PageInfo pageInfo = roleService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role, Model model) {
        roleService.insert(role);
        return successPage(model,"新增角色成功");
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(Model model,@PathVariable Long id) {
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping(value="/update")
    public String update(Role role,Model model) {
        roleService.update(role);
        return successPage(model,"新增角色成功");
    }

    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_ACTION;
    }
}
