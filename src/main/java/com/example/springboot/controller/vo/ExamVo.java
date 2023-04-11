package com.example.springboot.controller.vo;

import com.example.springboot.entity.Exam;
import lombok.Data;

@Data
public class ExamVo extends Exam {
    private String courseName;
}
