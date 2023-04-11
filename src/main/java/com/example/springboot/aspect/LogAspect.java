package com.example.springboot.aspect;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.example.springboot.controller..*.*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        long startTime = SystemClock.now();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时长(毫秒)
        long time = SystemClock.now() - startTime;
//        String className = joinPoint.getTarget().getClass().getName();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();  // 请求方法
        String param;   //请求的参数
        Object[] args = joinPoint.getArgs();
        if ("POST".equals(method)) {
            param = JSONUtil.toJsonStr(args[0]);
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            param = MapUtil.join(parameterMap, "&", "=");
        }
        log.info("{} {} {} 参数：{} 耗时：{}ms", method, url, response.getStatus(), param, time);
        return result;
    }

}
