package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.atguigu.service.impl.common.HelperMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.REQUIRED)
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;


    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    /**
     * 根据角色查询出所有的匹配的权限集合
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //1.0 查询出所有的权限对象
        List<Permission> permissions = permissionDao.findAll();

        //2.0 根据角色查询已分配的角色权限id列表
        List<Long> permissionIdListByRoleId = rolePermissionDao.findPermissionIdListByRoleId(roleId);

        //2.5 创建一个list集合用来返回map对象
        ArrayList<Map<String, Object>> maps = new ArrayList<>();

        //3.0 在工厂方法中创建map对象
        List<Map<String, Object>> collect = permissions.stream().map(permission -> {
            //1.0 创建一个hashMap
            HashMap<String, Object> map = new HashMap<>();
            if (permissionIdListByRoleId.contains(permission.getId())) {
                map.put("checked", true);
            } else {
                map.put("checked", false);
            }
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            map.put("open", true);
            return map;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 根据roleId 和 permissionId 保存 角色权限数据
     */
    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //1.0 根据roleId 查询出所有已经分配的permissionId集合
        List<Long> permissionIdListByRoleId = rolePermissionDao.findPermissionIdListByRoleId(roleId);

        //2.0 根据新的permissionId集合为参考，将需要排除的permissionId集合删除
        List<Long> removeCollects = permissionIdListByRoleId.stream().filter(permissionId -> !permissionIds.contains(permissionId)).collect(Collectors.toList());
        if (removeCollects.size() != 0) {
            rolePermissionDao.removeRoleIdAndPermissionId(roleId, removeCollects);
        }

        //3.0 将旧集合中的数据不存在的insert 已存在的update
        permissionIds.forEach(permissionId -> {

            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);

            if (!permissionIdListByRoleId.contains(permissionId)) {
                //1.0 以旧集合为参考，新的id如果不包括 insert
                rolePermissionDao.insert(rolePermission);
            } else {
                //2.0 如果旧集合中已存在但是is_deleted=1;则更新
                rolePermission.setIsDeleted(0);
                rolePermissionDao.update(rolePermission);
            }
        });

    }

    /**
     * 根据用户ID查询菜单权限列表
     *
     * @param adminId
     */
    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> all = null;
        List<Permission> originalPermission=null;
        if (adminId == 1) {
            originalPermission = permissionDao.findAll();
        } else {
            //当不是
            //2.0 当adminId为其他用户时查部分
            originalPermission = permissionDao.findPermissionListByAdminId(adminId);
        }
        return HelperMenu.build(originalPermission);
    }

    /**
     * 将权限对象保存到permission表中
     *
     * @param permission
     */
    @Override
    public void save(Permission permission) {
        permissionDao.save(permission);
    }

    /**
     *  根据 用户id查询所拥有的所有权限的code集合
     * @return
     */
    @Override
    public List<String> findCodePermissionListByAdminId(Long adminId) {
        // 当用户为超级用户时查询所有code
        List<String> permission = new ArrayList<>();
        if (adminId==1) {
            permission=permissionDao.findCodeAll();
        }else{
            permission=permissionDao.findCodePermissionListByAdminId(adminId);
        }
        return permission;
    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

}
