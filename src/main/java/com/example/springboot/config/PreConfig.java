package com.example.springboot.config;

import cn.hutool.http.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreConfig implements ApplicationRunner {
    @Value("${server.port}")
    private Integer port;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        String s = HttpUtil.get("http://localhost:"+port+"/user/version");
        System.out.println("启动了,接口文档地址："+"http://localhost:"+port+"/doc.html");
    }
}
