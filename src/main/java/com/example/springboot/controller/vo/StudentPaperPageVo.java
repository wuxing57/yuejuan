package com.example.springboot.controller.vo;

import com.example.springboot.entity.StudentPaper;
import lombok.Data;

@Data
public class StudentPaperPageVo extends StudentPaper {
    private String studentName;
    private String claName;
}
