package com.example.springboot.service.impl;

import com.example.springboot.entity.PaperQuestion;
import com.example.springboot.mapper.PaperQuestionMapper;
import com.example.springboot.service.IPaperQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
@Service
public class PaperQuestionServiceImpl extends ServiceImpl<PaperQuestionMapper, PaperQuestion> implements IPaperQuestionService {

}
