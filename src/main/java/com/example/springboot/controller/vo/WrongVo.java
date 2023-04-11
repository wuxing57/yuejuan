package com.example.springboot.controller.vo;

import com.example.springboot.entity.Question;
import com.example.springboot.entity.Wrong;
import lombok.Data;

@Data
public class WrongVo {
   private Wrong wrong;
   private Question question;
}
