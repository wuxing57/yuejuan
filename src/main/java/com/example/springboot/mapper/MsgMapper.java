package com.example.springboot.mapper;

import com.example.springboot.controller.vo.MsgVo;
import com.example.springboot.entity.Msg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
public interface MsgMapper extends BaseMapper<Msg> {

    Integer getPageTotal(Integer rec);

    List<MsgVo> getPageData(@Param("rec") Integer rec,
                            @Param("pageNum")Integer pageNum,
                            @Param("pageSize")Integer pageSize);
}
