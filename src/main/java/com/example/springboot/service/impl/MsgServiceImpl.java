package com.example.springboot.service.impl;

import com.example.springboot.controller.vo.MsgVo;
import com.example.springboot.entity.Msg;
import com.example.springboot.mapper.MsgMapper;
import com.example.springboot.service.IMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {

    @Override
    public List<MsgVo> getPageData(Integer rec, Integer pageNum, Integer pageSize) {
        pageNum = (pageNum - 1) *pageSize;
        return baseMapper.getPageData(rec, pageNum, pageSize);
    }

    @Override
    public Integer getPageTotal(Integer rec) {
        return baseMapper.getPageTotal(rec);
    }
}
