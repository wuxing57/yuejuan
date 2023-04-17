package com.example.springboot.config;

import cn.hutool.http.HttpUtil;
import com.example.springboot.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PreConfig implements ApplicationRunner {
    @Value("${server.port}")
    private Integer port;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        String s = HttpUtil.get("http://localhost:"+port+"/user/version");
        log.info("初始化tomcat连接：version={}",s);
        RedisUtils.setCacheObject("test", 1);
        Object test = RedisUtils.getCacheObject("test");
        log.info("初始化redis连接：test={}",test);
        System.out.println("启动了,接口文档地址："+"http://localhost:"+port+"/doc.html");
    }
}
