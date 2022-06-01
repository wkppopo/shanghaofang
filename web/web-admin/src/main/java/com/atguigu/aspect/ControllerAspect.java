package com.atguigu.aspect;

import com.atguigu.util.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ControllerAspect {
    //创建日志对象
    private Logger logger=LoggerFactory.getLogger(ControllerAspect.class);

    //切入点对象
    @Pointcut("execution(* com.atguigu.controller.*.*(..))")
    public void joinPoint(){}

    //环绕通知
    @Around("joinPoint()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //1.0 判断用户是否登录，如果没有登录，不用记录日志
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(user instanceof User)) {
            //没登录
            return proceedingJoinPoint.proceed();
        }
        //1.0 获取用户名
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(user.getUsername()+"|");
        //获取httpservletrequest
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes=(ServletRequestAttributes)requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //2.0 获取url
        String url = request.getRequestURL().toString();
        stringBuilder.append(url+"|");

        //3.0获取请求方式
        String method = request.getMethod();
        stringBuilder.append(method+"|");

        //4.0 获取IP地址
        String ipAddress = IpUtil.getIpAddress(request);
        stringBuilder.append(ipAddress+"|");

        //5.0 调用方法
        Signature signature = proceedingJoinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        String name = signature.getName();
        stringBuilder.append(declaringTypeName+"."+name+"|");

        //6.0 调用方法的参数
        Object[] args = proceedingJoinPoint.getArgs();
        if (args!=null || args.length>0) {
            for (int i = 0; i < args.length; i++) {
                if (args.length-1>i) {
                    stringBuilder.append(args[i]+",");
                }else{
                    stringBuilder.append(args[i]+"|");
                }
            }
        }

        //7.0 添加执行时间
        long start=0L;
        try{
            start = System.currentTimeMillis();
            return proceedingJoinPoint.proceed();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            Long end=System.currentTimeMillis();
            Long time=end-start;
            stringBuilder.append(time);
            logger.info("logs :"+stringBuilder);
        }

    }
}
