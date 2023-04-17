package com.example.springboot.mapper;

import com.example.springboot.controller.vo.StudentPaperPageVo;
import com.example.springboot.entity.StudentPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-12-17
 */
public interface StudentPaperMapper extends BaseMapper<StudentPaper> {

    List<StudentPaperPageVo> getPageData(@Param("examId") Integer examId,
                                         @Param("studentId")Integer studentId,
                                         @Param("pageNum")Integer pageNum,
                                         @Param("pageSize")Integer pageSize);

    Integer getPageTotal(@Param("examId") Integer examId,
                         @Param("studentId") Integer studentId);
}
