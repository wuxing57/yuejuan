package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Wrong  {
   @TableId(type = IdType.AUTO)
   private Integer id;
   @ApiModelProperty("问题id")
   private Integer questionId;
   @ApiModelProperty("学生id")
   private Integer studentId;
   @ApiModelProperty("学生答案")
   private String studentAnswer;
   @ApiModelProperty("学生分数")
   private Integer studentScore;

   @ApiModelProperty("学生试卷id")
   private Integer studentPaperId;

   @ApiModelProperty("课程id")
   private Integer courseId;
}
