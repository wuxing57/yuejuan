package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Wrong;
import com.example.springboot.mapper.WrongMapper;
import com.example.springboot.service.WrongService;
import org.springframework.stereotype.Service;

@Service
public class WrongServiceImpl extends ServiceImpl<WrongMapper, Wrong> implements WrongService {
}
