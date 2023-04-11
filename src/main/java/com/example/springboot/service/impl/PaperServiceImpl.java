package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.entity.Paper;
import com.example.springboot.entity.PaperQuestion;
import com.example.springboot.entity.Question;
import com.example.springboot.mapper.PaperMapper;
import com.example.springboot.service.IPaperQuestionService;
import com.example.springboot.service.IPaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements IPaperService {
    @Autowired
    private IPaperQuestionService paperQuestionService;
    @Autowired
    private IQuestionService questionService;

    @Override
    @Async("mythreadpool")
    public void getPaperScore(Integer paperId) {
        List<PaperQuestion> paperQuestions = paperQuestionService
                .list(new LambdaQueryWrapper<PaperQuestion>()
                        .eq(PaperQuestion::getPaperId, paperId));
        List<Integer> questionIds = paperQuestions.stream()
                .map(PaperQuestion::getQuestionId)
                .collect(Collectors.toList());
        List<Question> questions = questionService.listByIds(questionIds);
        Integer score = 0;
        for (Question question : questions) {
            score +=  question.getScore();
        }
        Paper paper = new Paper();
        paper.setId(paperId);
        paper.setScore(score);
        this.updateById(paper);
    }
}
