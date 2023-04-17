package com.example.springboot.mapper;

import com.example.springboot.controller.vo.SignVo;
import com.example.springboot.entity.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-12-16
 */
public interface SignMapper extends BaseMapper<Sign> {

    List<SignVo> getPageData(@Param("studentId") Integer studentId,
                             @Param("examId")Integer examId,
                             @Param("pageNum")Integer pageNum,
                             @Param("pageSize")Integer pageSize);

    Integer getPageTotal(@Param("studentId") Integer studentId,
                         @Param("examId")Integer examId);
}
