package com.example.springboot.service;

import com.example.springboot.controller.vo.StudentPaperPageVo;
import com.example.springboot.entity.StudentPaper;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2022-12-17
 */
public interface IStudentPaperService extends IService<StudentPaper> {

    Integer getPageTotal(Integer examId, Integer studentId);

    List<StudentPaperPageVo> getPageData(Integer examId, Integer studentId, Integer pageNum, Integer pageSize);
}
