package com.example.springboot.mapper;

import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    List<QuestionVo> getPageData(@Param("name") String name,
                                 @Param("type")Integer type,
                                 @Param("courseId")Integer courseId,
                                 @Param("pageNum")Integer pageNum,
                                 @Param("pageSize")Integer pageSize);

    Integer getPageTotal(@Param("name") String name,
                         @Param("type")Integer type,
                         @Param("courseId")Integer courseId);
}
