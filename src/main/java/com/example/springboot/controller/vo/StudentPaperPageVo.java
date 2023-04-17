package com.example.springboot.controller.vo;

import com.example.springboot.entity.StudentPaper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentPaperPageVo  {
    private String studentName;
    private String claName;
    private String examName;

    private Integer id;

    @ApiModelProperty("考试id")
    private Integer examId;

    @ApiModelProperty("试卷内容")
    private String paper;

    @ApiModelProperty("学生id")
    private Integer userId;

    @ApiModelProperty("提交时间")
    private String time;

    @ApiModelProperty("得分")
    private Integer score;

    @ApiModelProperty("阅卷状态")
    private Integer status;

    @ApiModelProperty("班级id")
    private Integer claId;
}
