package com.example.springboot.controller.vo;

import com.example.springboot.entity.StudentPaper;
import lombok.Data;

@Data
public class RecordVo extends StudentPaper {
    private String courseName;
    private String examName;
}
