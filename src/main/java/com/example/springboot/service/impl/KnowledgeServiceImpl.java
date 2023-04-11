package com.example.springboot.service.impl;

import com.example.springboot.entity.Knowledge;
import com.example.springboot.mapper.KnowledgeMapper;
import com.example.springboot.service.IKnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 知识点 服务实现类
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements IKnowledgeService {

}
