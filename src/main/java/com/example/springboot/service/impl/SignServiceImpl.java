package com.example.springboot.service.impl;

import com.example.springboot.controller.vo.SignVo;
import com.example.springboot.entity.Sign;
import com.example.springboot.mapper.SignMapper;
import com.example.springboot.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2022-12-16
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

    @Override
    public Integer getPageTotal(Integer studentId, Integer examId) {
        return baseMapper.getPageTotal(studentId, examId);
    }

    @Override
    public List<SignVo> getPageData(Integer studentId, Integer examId, Integer pageNum, Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        return baseMapper.getPageData(studentId, examId, pageNum, pageSize);
    }
}
