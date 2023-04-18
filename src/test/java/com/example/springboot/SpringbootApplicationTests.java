package com.example.springboot;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.entity.Exam;
import com.example.springboot.entity.StudentPaper;
import com.example.springboot.entity.User;
import com.example.springboot.service.IExamService;
import com.example.springboot.service.IQuestionService;
import com.example.springboot.service.IStudentPaperService;
import com.example.springboot.service.IUserService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.*;

@SpringBootTest
class SpringbootApplicationTests {


    @Test
    void contextLoads() {
        ArrayList<Object> questionList1 = new ArrayList<>();
        questionList1.add("1");
        questionList1.add("2");
        questionList1.add("3");
        questionList1.add("5");
        questionList1.add("6");
        questionList1.add("7");
        System.out.print(RandomUtil.randomEleList(questionList1, 4));
    }

    @Autowired
    private IStudentPaperService studentPaperService;
    @Autowired
    private IExamService examService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IQuestionService questionService;

    @Test
    public void daochu() {
//        String startTime = "2022" +"-"+"12"+"-"+"14"+" 00:00";
//        String endTime = "2022" +"-"+"12"+"-"+"14"+" 23:59";
//        Integer count = questionService.getQuestionCount(startTime, endTime);
//        System.out.println(count);
        Calendar instance = Calendar.getInstance();
        int month = instance.get(Calendar.MONTH);
        int year = instance.get(Calendar.YEAR);
        int days = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(month);
        System.out.println(year);
        System.out.println(days);
    }

    @Test
    public void questionpage(){
        System.out.println("questionService.getPageTotal(\"\", 1, 2) = " + questionService.getPageTotal("", 1, 2));
       // System.out.println(pageData);
    }

    @Test
    public void timeZ(){
        Exam exam = examService.getById(3);
        String time = exam.getTime();
        Integer duration = exam.getDuration();
        String status = exam.getState();
        long start = DateUtil.parse(time,"yyyy-MM-dd HH:mm").getTime();
        long now = System.currentTimeMillis();
        long end = start + duration * 60000;
        System.out.println("status"+status);
        System.out.println("start"+start);
        System.out.println("now"+now);
        System.out.println("end"+end);
    }
}
