package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.vo.PaperVo;
import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.entity.Course;
import com.example.springboot.entity.Paper;
import com.example.springboot.service.ICourseService;
import com.example.springboot.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IQuestionService;
import com.example.springboot.entity.Question;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private IQuestionService questionService;
    @Resource
    private ICourseService courseService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Question question) {
        if (question.getId() == null) {
            question.setTime(DateUtil.now());
            question.setUserId(TokenUtils.getCurrentUser().getId());
        }
        questionService.saveOrUpdate(question);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        questionService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        questionService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(questionService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(questionService.getById(id));
    }

    @Resource
    private IUserService userService;
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam(required = false) Integer type,
                           @RequestParam(required = false) Integer courseId,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (type!=null) {
            queryWrapper.eq("type", type);
        }
        if (courseId!=null) {
            queryWrapper.eq("course_id", courseId);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<Question> questionPage = questionService.page(new Page<>(pageNum, pageSize), queryWrapper);
        IPage<QuestionVo> questionVoIPage = questionPage.convert(question -> {
            QuestionVo questionVo = new QuestionVo();
            BeanUtils.copyProperties(question, questionVo);
            return questionVo;
        });
        if (questionVoIPage != null){
            List<Integer> courseIds = questionVoIPage.getRecords().stream().map(Question::getCourseId).collect(Collectors.toList());
            List<Integer> userIds = questionVoIPage.getRecords().stream().map(Question::getUserId).collect(Collectors.toList());
            List<Course> courseList = courseService.listByIds(courseIds);
            List<User> users = userService.listByIds(userIds);
            Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, Course::getName));
            Map<Integer, String> userMap = users.stream().collect(Collectors.toMap(User::getId, User::getNickname));
            questionVoIPage.convert( questionVo -> {
                questionVo.setCourseName(courseMap.get(questionVo.getCourseId()));
                questionVo.setUserName(userMap.get(questionVo.getUserId()));
                return questionVo;
            });
        }
        return Result.success(questionVoIPage);
    }
    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Question> list = questionService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Question信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Question> list = reader.readAll(Question.class);

        questionService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

