package com.atguigu.config;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //1.0 根据从登录页面传过来的用户名 去admin表中查询用户对象，如果不存在直接抛异常
        Admin admin = adminService.getAdminByUserName(userName);
        if (admin == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        //2.0 用户存在，然后根据用户id查询所有跟此用户相关的权限的code
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        List<String> codes = permissionService.findCodePermissionListByAdminId(admin.getId());
        if (codes!=null && codes.size()>0) {
            for (String code : codes) {
                if (StringUtils.isEmpty(code)) {
                    continue;
                }
                grantedAuthorities.add(new SimpleGrantedAuthority(code));
            }
        }

        //2.0 使用security中的user对象校验密码如果成功构建一个用户对象到容器中
        return new User(userName,admin.getPassword(), grantedAuthorities);
    }
}
