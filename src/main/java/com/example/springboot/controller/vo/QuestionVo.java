package com.example.springboot.controller.vo;

import com.example.springboot.entity.Question;
import lombok.Data;

@Data
public class QuestionVo extends Question {
    private String courseName;
    private String userName;
}
