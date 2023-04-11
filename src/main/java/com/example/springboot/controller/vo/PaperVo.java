package com.example.springboot.controller.vo;

import com.example.springboot.entity.Paper;
import lombok.Data;

@Data
public class PaperVo extends Paper {
    private String courseName;
}
