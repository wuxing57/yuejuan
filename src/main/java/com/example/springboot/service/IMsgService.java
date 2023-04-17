package com.example.springboot.service;

import com.example.springboot.controller.vo.MsgVo;
import com.example.springboot.entity.Msg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
public interface IMsgService extends IService<Msg> {

    List<MsgVo> getPageData(Integer rec, Integer pageNum, Integer pageSize);

    Integer getPageTotal(Integer rec);
}
