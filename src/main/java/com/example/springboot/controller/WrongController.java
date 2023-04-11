package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springboot.common.Result;
import com.example.springboot.controller.vo.WrongVo;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.Wrong;
import com.example.springboot.service.IQuestionService;
import com.example.springboot.service.WrongService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api("错题管理接口")
@RequestMapping("/wrong")
public class WrongController {
    @Resource
    private WrongService wrongService;
    @Resource
    private IQuestionService questionService;

    @GetMapping("/{userId}")
    public Result getWrong(@PathVariable Integer userId){
        List<Wrong> wrongList = wrongService.list(Wrappers.<Wrong>lambdaQuery().eq(Wrong::getStudentId, userId));
        List<WrongVo> wrongVoList = wrongList.stream()
                .map(wrong -> {
                    WrongVo wrongVo = new WrongVo();
                    wrongVo.setWrong(wrong);
                    Integer questionId = wrong.getQuestionId();
                    Question question = questionService.getById(questionId);
                    wrongVo.setQuestion(question);
                    return wrongVo;
                }).collect(Collectors.toList());
        return Result.success(wrongVoList);

    }
    @PostMapping("/all")
    public Result add(@RequestBody List<Wrong> wrongList){
        Wrong wrong = wrongList.get(0);
        Integer studentId = wrong.getStudentId();
        Integer studentPaperId = wrong.getStudentPaperId();
        wrong = wrongService.getOne(Wrappers.<Wrong>lambdaQuery()
                .eq(Wrong::getStudentId, studentId)
                .eq(Wrong::getStudentPaperId, studentPaperId));
        if (wrong != null){
            wrongService.remove(Wrappers.<Wrong>lambdaQuery().eq(Wrong::getStudentId, studentId)
                    .eq(Wrong::getStudentPaperId, studentPaperId));
        }
        boolean b = wrongService.saveBatch(wrongList);
        return b ? Result.success():Result.error();
    }
}
