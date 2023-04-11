package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Cla;
import com.example.springboot.entity.StudentPaper;
import com.example.springboot.entity.User;
import com.example.springboot.service.IClaService;
import com.example.springboot.service.IStudentPaperService;
import com.example.springboot.service.IUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IStudentPaperService studentPaperService;
    @Autowired
    private IClaService claService;

    @GetMapping("/grade/{examId}")
    public Result grade(@PathVariable Integer examId){
        //获取所有成绩
        QueryWrapper<StudentPaper> studentPaperQueryWrapper = new QueryWrapper<>();
        studentPaperQueryWrapper.eq("exam_id",examId);
        List<StudentPaper> studentPaperList = studentPaperService.list(studentPaperQueryWrapper);
        //获取所有的班级
        Set<Integer> claSet = studentPaperList.stream()
                .map(StudentPaper::getClaId)
                .collect(Collectors.toSet());
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> claList = new ArrayList<>();
        ArrayList<Double> avgScoreList = new ArrayList<>();
        Optional<Integer> max = studentPaperList.stream()
                .map(StudentPaper::getScore)
                .max(Comparator.comparingInt(o -> o));
        Optional<Integer> min = studentPaperList.stream()
                .map(StudentPaper::getScore)
                .min(Comparator.comparingInt(o -> o));
        for (Integer claId : claSet) {
            Cla cla = claService.getById(claId);
            claList.add(cla.getName());
            Double score = studentPaperList.stream()
                    .filter(studentPaper -> studentPaper.getClaId().equals(claId))
                    .map(StudentPaper::getScore)
                    .collect(Collectors.averagingDouble(a -> Double.parseDouble(String.valueOf(a))));
            avgScoreList.add(score);

        }
         map.put("x",claList);
         map.put("y",avgScoreList);
         map.put("max",max);
         map.put("min",min);
         map.put("all",studentPaperList);
         return Result.success(map);
    }

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

}
