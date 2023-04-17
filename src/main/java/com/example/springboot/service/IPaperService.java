package com.example.springboot.service;

import com.example.springboot.controller.vo.PaperVo;
import com.example.springboot.entity.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    Integer getPageTotal(String name, Integer courseId);

    List<PaperVo> getPageData(String name, Integer courseId, Integer pageNum, Integer pageSize);
}
