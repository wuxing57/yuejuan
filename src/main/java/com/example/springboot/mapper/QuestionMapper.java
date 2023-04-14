package com.example.springboot.mapper;

import com.example.springboot.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-12-14
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> getQuestionByPaperId(Integer paperId);

    Integer getQuestionCount(String startTime, String endTime);
}
