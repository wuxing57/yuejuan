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
import com.example.springboot.controller.vo.KnowledgeVo;
import com.example.springboot.entity.Course;
import com.example.springboot.service.ICourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IKnowledgeService;
import com.example.springboot.entity.Knowledge;

import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.util.Pair.toMap;

/**
 * <p>
 * 知识点 前端控制器
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Resource
    private IKnowledgeService knowledgeService;
    @Resource
    private ICourseService courseService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Knowledge knowledge) {
        if (knowledge.getId() == null) {
            //knowledge.setTime(DateUtil.now());
            //knowledge.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        if (knowledge.getId() == null){
            knowledge.setCreateTime(DateUtil.now());
        }
        knowledgeService.saveOrUpdate(knowledge);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        knowledgeService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        knowledgeService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(knowledgeService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(knowledgeService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Knowledge> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<Knowledge> knowledgePage = knowledgeService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        IPage<KnowledgeVo> knowledgeVoIPage = knowledgePage.convert(new Function<Knowledge, KnowledgeVo>() {
//            @Override
//            public KnowledgeVo apply(Knowledge knowledge) {
//                KnowledgeVo knowledgeVo = new KnowledgeVo();
//                BeanUtils.copyProperties(knowledge, knowledgeVo);
//                return knowledgeVo;
//            }
//        });
        IPage<KnowledgeVo> knowledgeVoIPage = knowledgePage.convert(knowledge -> {
                KnowledgeVo knowledgeVo = new KnowledgeVo();
                BeanUtils.copyProperties(knowledge, knowledgeVo);
                return knowledgeVo;
        });
        if (knowledgeVoIPage != null){
            List<Integer> courseIds = knowledgeVoIPage.getRecords().stream()
                    .map(Knowledge::getCourseId).collect(Collectors.toList());
            List<Course> courseList = courseService.listByIds(courseIds);
            Map<Integer, String> map = courseList.stream().collect(Collectors.toMap(Course::getId, Course::getName));
//            knowledgeVoIPage.convert(new Function<KnowledgeVo, KnowledgeVo>() {
//                @Override
//                public KnowledgeVo apply(KnowledgeVo knowledgeVo) {
//                    knowledgeVo.setCourseName(map.get(knowledgeVo.getCourseId()));
//                    return knowledgeVo;
//                }
//            });
            knowledgeVoIPage.convert(knowledgeVo -> {
                knowledgeVo.setCourseName(map.get(knowledgeVo.getCourseId()));
                return knowledgeVo;
            });
        }
        return Result.success(knowledgeVoIPage);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Knowledge> list = knowledgeService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Knowledge信息表", "UTF-8");
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
        List<Knowledge> list = reader.readAll(Knowledge.class);

        knowledgeService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

