package com.example.springboot.service;

import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2022-12-14
 */
public interface IQuestionService extends IService<Question> {

    List<Question> getQuestionByPaperId(Integer paperId);

    Integer getQuestionCount(String startTime, String endTime);

    List<QuestionVo> getPageData(String name, Integer type, Integer courseId, Integer pageNum, Integer pageSize);

    Integer getPageTotal(String name, Integer type, Integer courseId);
}
