package com.example.springboot.mapper;

import com.example.springboot.controller.vo.PaperVo;
import com.example.springboot.entity.Paper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
public interface PaperMapper extends BaseMapper<Paper> {

    List<PaperVo> getPageData(@Param("name") String name,
                              @Param("courseId")Integer courseId,
                              @Param("pageNum")Integer pageNum,
                              @Param("pageSize")Integer pageSize);

    Integer getPageTotal(@Param("name")String name, @Param("courseId")Integer courseId);
}
