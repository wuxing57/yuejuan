package com.example.springboot.service.impl;

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
}
