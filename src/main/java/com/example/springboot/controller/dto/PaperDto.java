package com.example.springboot.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaperDto implements Serializable {
    private Integer type1;
    private Integer type2;
    private Integer type3;
    private Integer type4;
    private Integer type5;
    private Integer level;
    private Integer knowledgeId;
    private Integer paperId;
    private Integer courseId;
}
