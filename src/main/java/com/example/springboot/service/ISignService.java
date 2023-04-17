package com.example.springboot.service;

import com.example.springboot.controller.vo.SignVo;
import com.example.springboot.entity.Sign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2022-12-16
 */
public interface ISignService extends IService<Sign> {

    Integer getPageTotal(Integer studentId, Integer examId);

    List<SignVo> getPageData(Integer studentId, Integer examId, Integer pageNum, Integer pageSize);
}
