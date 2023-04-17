package com.example.springboot.controller.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.springboot.entity.Paper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PaperVo  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("试卷名称")
    private String name;

    @ApiModelProperty("试卷总分")
    private Integer score;

    @ApiModelProperty("考试时长")
    private Integer duration;

    @ApiModelProperty("课程id")
    private Integer courseId;
    private String courseName;
}
