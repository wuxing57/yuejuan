package com.example.springboot.service;

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
}
