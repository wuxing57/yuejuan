package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
@Getter
@Setter
@ApiModel(value = "Exam对象", description = "")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("考试名称")
    private String name;

    @ApiModelProperty("考试地点")
    private String room;

    @ApiModelProperty("考试时间")
    private String time;

    @ApiModelProperty("监考老师")
    private String teacher;

    @ApiModelProperty("考试状态 0：未开始 1:进行中：2：已结束")
    private String state;

    @ApiModelProperty("考试对应课程id")
    private Integer courseId;
    @ApiModelProperty("考试时长")
    private Integer duration;

}
