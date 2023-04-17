package com.example.springboot.controller.vo;

import com.example.springboot.entity.Msg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MsgVo extends Msg {
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("发送人")
    private Integer send;

    @ApiModelProperty("接收人")
    private Integer rec;

    @ApiModelProperty("已读数量")
    private Integer readNum;

    @ApiModelProperty("创建时间")
    private String createTime;
    private String sendUsername;
    private String recUsername;
}
