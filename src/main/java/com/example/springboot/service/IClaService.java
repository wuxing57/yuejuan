package com.example.springboot.service;

import com.example.springboot.entity.Cla;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
public interface IClaService extends IService<Cla> {

    Integer getClassByKey(String key);
}
