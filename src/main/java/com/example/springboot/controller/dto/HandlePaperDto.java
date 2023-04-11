package com.example.springboot.controller.dto;

import com.example.springboot.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class HandlePaperDto {
    private Integer paperId;
    private List<Question> handleQuestionId;
}
