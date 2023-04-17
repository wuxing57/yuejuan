package com.example.springboot.service.impl;

import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.entity.Question;
import com.example.springboot.mapper.QuestionMapper;
import com.example.springboot.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2022-12-14
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public List<Question> getQuestionByPaperId(Integer paperId) {
      List<Question> questions=  questionMapper.getQuestionByPaperId(paperId);
        return questions;
    }

    @Override
    public Integer getQuestionCount(String startTime, String endTime) {
        return baseMapper.getQuestionCount(startTime, endTime);
    }

    @Override
    public List<QuestionVo> getPageData(String name, Integer type, Integer courseId, Integer pageNum, Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        return  baseMapper.getPageData(name, type, courseId, pageNum, pageSize);
    }

    @Override
    public Integer getPageTotal(String name, Integer type, Integer courseId) {
        return  baseMapper.getPageTotal(name, type, courseId);
    }
}
