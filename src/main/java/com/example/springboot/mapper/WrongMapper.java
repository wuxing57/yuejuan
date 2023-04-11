package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Wrong;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WrongMapper extends BaseMapper<Wrong> {
}
