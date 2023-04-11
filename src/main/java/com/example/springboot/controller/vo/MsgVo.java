package com.example.springboot.controller.vo;

import com.example.springboot.entity.Msg;
import lombok.Data;

@Data
public class MsgVo extends Msg {
    private String sendUsername;
    private String recUsername;
}
