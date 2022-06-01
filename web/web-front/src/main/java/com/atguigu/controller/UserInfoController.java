package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone")String phone, HttpSession httpSession){
        //1.0 本来要通过阿里云发送短信给手机的，现在通过httpsession模拟
        String CODE="1111";
        httpSession.setAttribute("CODE",CODE);
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo,HttpSession httpSession){
        //1.0 校验验证码是否正确
        String code = (String) httpSession.getAttribute("CODE");
        if (!registerBo.getCode().equalsIgnoreCase(code)) {
            // 验证码错误，返回错误信息到前端
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }

        //1.0 判断手机号是否已经被注册
        UserInfo userInfo = userInfoService.getByPhone(registerBo.getPhone());

        //2.0 如果手机号已经被注册
        if (userInfo!=null) {
            //告诉前端手机号已经被注册
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //1.0 将密码重新加密
        String password = registerBo.getPassword();
        String encrypt = MD5.encrypt(password);

        //2.0 将携带的信息拷贝到UserInfo中
        UserInfo userInfo1 = new UserInfo();

        BeanUtils.copyProperties(registerBo,userInfo1);

        //3.0 重新设置密码
        userInfo1.setPassword(encrypt);

        //4.0 设置status
        userInfo1.setStatus(1);

        //5.0 插入
        userInfoService.insert(userInfo1);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo,HttpSession httpSession){
        //1.0 判断手机号是否存在，
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        //2.0 判断手机号是否被锁定
        if (userInfo==null) {
            // 用户名不存在
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (userInfo.getStatus()==0) {
            //账号被锁定
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        //3.0 判断密码是否正确
        if (!userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword()))) {
            //密码不正确
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //4.0 如果走到这一步说明登录成功
        httpSession.setAttribute("USER",userInfo);

        //4.1 将用户信息在后台共享
        HashMap<String, Object> map = new HashMap<>();
        map.put("nickName",userInfo.getNickName());
        map.put("phone",userInfo.getPhone());

        //4.2 将用户信息昵称和手机号在前端共享
        return Result.ok(map);
    }

    //推出登录
    @GetMapping("/logout")
    public Result logOut(HttpSession httpSession){
        //在后台销毁用户信息
        httpSession.invalidate();
        return Result.ok();
    }
}
