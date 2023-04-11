package com.example.springboot.service;

import com.example.springboot.entity.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
public interface IPaperService extends IService<Paper> {
    void getPaperScore(Integer paperId);
}
