package com.example.springboot;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.springboot.entity.Exam;
import com.example.springboot.entity.StudentPaper;
import com.example.springboot.entity.User;
import com.example.springboot.service.IExamService;
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

    @Test
    public void daochu() throws IOException {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
    }
}
