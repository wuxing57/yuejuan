package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.WrongDto;
import com.example.springboot.controller.vo.WrongVo;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.Wrong;
import com.example.springboot.service.IQuestionService;
import com.example.springboot.service.WrongService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
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

    /**
     * 查询错题
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result getWrong(@PathVariable Integer userId,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           WrongDto wrongDto){
        Page<Wrong> wrongPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Wrong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Wrong::getStudentId, userId);
        wrapper.eq(wrongDto.getCourseId() != null, Wrong::getCourseId,
                wrongDto.getCourseId());
        Page<Wrong> page = wrongService.page(wrongPage, wrapper);
        List<WrongVo> wrongVoList = page.getRecords().stream()
                .map(wrong -> {
                    WrongVo wrongVo = new WrongVo();
                    wrongVo.setWrong(wrong);
                    Integer questionId = wrong.getQuestionId();
                    Question question = questionService.getById(questionId);
                    wrongVo.setQuestion(question);
                    return wrongVo;
                }).collect(Collectors.toList());
        HashMap<String, Object> result = new HashMap<>();
        result.put("data", wrongVoList);
        result.put("total", page.getTotal());
        return Result.success(result);

    }

    /**
     * 添加错题
     * @param wrongList
     * @return
     */
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
