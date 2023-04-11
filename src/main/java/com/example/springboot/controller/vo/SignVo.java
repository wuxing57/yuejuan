package com.example.springboot.controller.vo;

import com.example.springboot.entity.Sign;
import lombok.Data;

@Data
public class SignVo extends Sign {
    private String examName;
    private String studentName;
}
