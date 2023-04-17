package com.example.springboot.controller.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.springboot.entity.Question;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionVo  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型 1：选择  2：判断  3：问答")
    private Integer type;

    @ApiModelProperty("选项a")
    private String a;

    @ApiModelProperty("选项b")
    private String b;

    @ApiModelProperty("选项c")
    private String c;

    @ApiModelProperty("选项d")
    private String d;

    @ApiModelProperty("分数")
    private Integer score;

    @ApiModelProperty("出题人id")
    private Integer userId;

    @ApiModelProperty("解析")
    private String detial;

    @ApiModelProperty("出题时间")
    private String time;

    @ApiModelProperty("答案")
    private String answer;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("难易程度")
    private Integer level;

    @ApiModelProperty("知识点id")
    private Integer knowledgeId;
    private String courseName;
    private String userName;
}
