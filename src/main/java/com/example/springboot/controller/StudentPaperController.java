package com.example.springboot.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.vo.RecordVo;
import com.example.springboot.controller.vo.StudentPaperPageVo;
import com.example.springboot.controller.vo.WrongVo;
import com.example.springboot.entity.*;
import com.example.springboot.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthPainter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2022-12-17
 */
@RestController
@Slf4j
@Api("学生试卷接口")
@RequestMapping("/studentPaper")
public class StudentPaperController {
    @Resource
    private IStudentPaperService studentPaperService;
    @Resource
    private IUserService userService;
    @Resource
    private IExamPaperService examPaperService;
    @Resource
    private IExamService examService;
    @Resource
    private IPaperService paperService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IClaService claService;


    private final String now = DateUtil.now();


    // 学生交卷
    @PostMapping
    @ApiOperation("学生交卷")
    public Result save(@RequestBody StudentPaper studentPaper) {
        StudentPaper getStudentPaper = studentPaperService.getOne(Wrappers.<StudentPaper>lambdaQuery()
                .eq(StudentPaper::getExamId, studentPaper.getExamId())
                .eq(StudentPaper::getUserId, studentPaper.getUserId()));
        if (getStudentPaper!=null){
            return Result.error("-1","不能重复提交试卷");
        }
        if (studentPaper.getId() == null) {
            studentPaper.setTime(DateUtil.now());
            studentPaper.setUserId(TokenUtils.getCurrentUser().getId());
        }
        studentPaperService.save(studentPaper);
        return Result.success();
    }
    @PutMapping
    public Result updateById(@RequestBody StudentPaper studentPaper){
        studentPaperService.updateById(studentPaper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        studentPaperService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        studentPaperService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(studentPaperService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(studentPaperService.getById(id));
    }

    /**
     * 查询考试记录
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    public Result userId(@PathVariable Integer userId) {
        List<StudentPaper> studentPaperList = studentPaperService.list(Wrappers.<StudentPaper>lambdaQuery().eq(StudentPaper::getUserId, userId));
        List<RecordVo> recordVoList = BeanUtil.copyToList(studentPaperList, RecordVo.class);
        recordVoList.stream()
                .forEach(recordVo -> {
                    Integer examId = recordVo.getExamId();
                    Exam exam = examService.getById(examId);
                    String examName = exam.getName();
                    Integer courseId = exam.getCourseId();
                    String courseName = courseService.getById(courseId).getName();
                    recordVo.setExamName(examName);
                    recordVo.setCourseName(courseName);
                });
        return Result.success(recordVoList);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") Integer examId,
                           @RequestParam(defaultValue = "") Integer studentId,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<StudentPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (examId != null) {
            queryWrapper.eq("exam_id", examId);
        }
        if (studentId != null) {
            queryWrapper.eq("user_id", studentId);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<StudentPaper> studentPaperPage = studentPaperService.page(new Page<>(pageNum, pageSize), queryWrapper);
        IPage<StudentPaperPageVo> studentPaperPageVoIPage = studentPaperPage.convert(studentPaper -> {
            StudentPaperPageVo studentPaperPageVo = new StudentPaperPageVo();
            BeanUtils.copyProperties(studentPaper, studentPaperPageVo);
            return studentPaperPageVo;
        });
        //填充数据库的id为name
        if (studentPaperPageVoIPage != null){
            List<Integer> studentIds = studentPaperPageVoIPage.getRecords().stream()
                    .map(StudentPaper::getUserId).collect(Collectors.toList());
            List<Integer> claIds = studentPaperPageVoIPage.getRecords().stream()
                    .map(StudentPaper::getClaId).collect(Collectors.toList());
            List<User> userList = userService.listByIds(studentIds);
            List<Cla> claList = claService.listByIds(claIds);
            Map<Integer, String> claMap = claList.stream().collect(Collectors.toMap(Cla::getId, Cla::getName));
            Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(User::getId, User::getUsername));
            studentPaperPageVoIPage.convert(studentPaperPageVo -> {
                studentPaperPageVo.setStudentName(userMap.get(studentPaperPageVo.getUserId()));
                studentPaperPageVo.setClaName(claMap.get(studentPaperPageVo.getClaId()));
                return studentPaperPageVo;
            });
        }
        return Result.success(studentPaperPageVoIPage);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<StudentPaper> list = studentPaperService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("StudentPaper信息表", "UTF-8");
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
        List<StudentPaper> list = reader.readAll(StudentPaper.class);
        studentPaperService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

