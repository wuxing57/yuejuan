package com.example.springboot.controller.vo;

import com.example.springboot.entity.Sign;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignVo  {
    private Integer id;

    @ApiModelProperty("考试id")
    private Integer examId;

    @ApiModelProperty("学生id")
    private Integer userId;

    @ApiModelProperty("审核状态")
    private String status;
    private String examName;
    private String studentName;
}
