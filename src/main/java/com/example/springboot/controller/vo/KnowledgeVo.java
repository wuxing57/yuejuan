package com.example.springboot.controller.vo;

import com.example.springboot.entity.Knowledge;
import lombok.Data;

@Data
public class KnowledgeVo extends Knowledge {
    private String courseName;
}
