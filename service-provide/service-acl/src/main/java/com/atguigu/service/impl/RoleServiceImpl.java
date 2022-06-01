package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;

import com.atguigu.dao.AdminDao;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.unbescape.properties.PropertiesKeyEscapeLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-05-17  14:13
 */
@Transactional(propagation = Propagation.REQUIRED)
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    protected BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    //将所有的角色分装成两个list集合，一个为admin已赋予的角色，另一个为未赋予的角色集合
    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long adminId) {
        //1.0 从角色表查询出所有角色对象
        List<Role> all = roleDao.findAll();
        //2.0 根据adminId查询出所有已赋予的角色
        List<Long> roleIdListByAdminId = adminRoleDao.findRoleIdListByAdminId(adminId);

        //创建两个list集合，一个用来装已分配的，另一个用来装未分配的
        ArrayList<Role> assignRole = new ArrayList<>();
        ArrayList<Role> unAssignRole = new ArrayList<>();

        //4.0 遍历all集合
        for (Role role : all) {
            if (roleIdListByAdminId.contains(role.getId())) {
                //已包含的角色id
                assignRole.add(role);
            }else{
                //未包含的角色id
                unAssignRole.add(role);
            }
        }
        //将两种list集合打包成map集合
        HashMap<String, List<Role>> map = new HashMap<>();
        map.put("unAssignRoleList",unAssignRole);
        map.put("assignRoleList",assignRole);

        return map;
    }

    /**
     * 保存用户角色信息
     * @param adminId
     * @param roleIdList
     */
    @Override
    public void saveAdminRole(Long adminId, List<Long> roleIdList) {
        //根据adminId 查找出已拥有的所有权限id
        List<Long> roleIdListByAdminId = adminRoleDao.findRoleIdListByAdminId(adminId);

        //以roleIdList 为参考，将不需要的角色id排除
        List<Long> removeRoles = roleIdListByAdminId.stream().filter(item -> !roleIdList.contains(item)).collect(Collectors.toList());
        if (removeRoles.size()!=0) {
            adminRoleDao.removeAdminRole(adminId,removeRoles);
        }

        //遍历要存入的集合,
        for (Long roleId : roleIdList) {
            AdminRole byAdminIdAndRoleId = adminRoleDao.findByAdminIdAndRoleId(adminId, roleId);
            if (byAdminIdAndRoleId ==null) {
                //这条数据不存在，创建一个新的对象并insert
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                adminRoleDao.insert(adminRole);
            }else{
                if (byAdminIdAndRoleId.getIsDeleted()==1) {
                    byAdminIdAndRoleId.setIsDeleted(0);
                    adminRoleDao.update(byAdminIdAndRoleId);
                }
            }
        }

    }


}
