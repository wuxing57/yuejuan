package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.Course;
import com.example.springboot.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IExamService;
import com.example.springboot.entity.Exam;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2022-12-15
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Resource
    private  IExamService examService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Exam exam) {
        RedisUtils.deleteObject(Constants.REDIS_EXAM_ALL);
        if (exam.getId() == null) {
            //exam.setTime(DateUtil.now());
            //exam.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        updateStatus(exam);
        examService.saveOrUpdate(exam);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        RedisUtils.deleteObject(Constants.REDIS_EXAM_ALL);
        examService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        RedisUtils.deleteObject(Constants.REDIS_EXAM_ALL);
        examService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        List<Exam> examList = RedisUtils.getCacheList(Constants.REDIS_EXAM_ALL);
        if (CollUtil.isEmpty(examList)){
            examList = examService.list();
            RedisUtils.setCacheList(Constants.REDIS_EXAM_ALL, examList);
        }
        return Result.success(examList);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(examService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "",required = false) String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<Exam> examPage = examService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Exam> records = examPage.getRecords();
        for (Exam exam : records) {
            updateStatus(exam);
        }
        return Result.success(examPage);
    }

    private  void updateStatus(Exam exam) {
        String time = exam.getTime();
        Integer duration = exam.getDuration();
        if (duration != null){
            String status = exam.getState();
            long start = DateUtil.parse(time,"yyyy-MM-dd HH:mm").getTime();
            long now = System.currentTimeMillis();
            long end = start + duration * 60000;
            //计算状态
            String newSatus = "";
            if (now < start){
                newSatus = "未开始";
            }else if (now > end){
                newSatus = "已结束";
            }else {
                newSatus = "进行中";
            }
            //状态不一致更新
            if (!status.equals(newSatus)){
                exam.setState(newSatus);
                examService.updateById(exam);
            }
        }
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Exam> list = examService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Exam信息表", "UTF-8");
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
        List<Exam> list = reader.readAll(Exam.class);

        examService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

