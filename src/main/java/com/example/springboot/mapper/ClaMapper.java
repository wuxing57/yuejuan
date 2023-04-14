package com.example.springboot.mapper;

import com.example.springboot.entity.Cla;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
public interface ClaMapper extends BaseMapper<Cla> {

    Integer getClassByKey(String key);
}
