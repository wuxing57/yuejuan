package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Question;
import com.example.springboot.exception.ErrorCode;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IPaperService;
import com.example.springboot.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPaperQuestionService;
import com.example.springboot.entity.PaperQuestion;

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
@Slf4j
@RequestMapping("/paperQuestion")
public class PaperQuestionController {

    @Resource
    private IPaperQuestionService paperQuestionService;
    @Resource
    private IQuestionService questionService;
    @Resource
    private IPaperService paperService;

    private final String now = DateUtil.now();

    /**
     * 根据paperId查询问题
     * @param paperId
     * @return
     */
    @GetMapping("/get-question/{paperId}")
    public Result getQuestionByPaperId(@PathVariable Integer paperId){
        LambdaQueryWrapper<PaperQuestion> paperQuestionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        paperQuestionLambdaQueryWrapper.eq(PaperQuestion::getPaperId,paperId);
        //查询当前试卷的所有问题id
        List<PaperQuestion> list = paperQuestionService.list(paperQuestionLambdaQueryWrapper);
        List<Integer> questionIds = list.stream()
                .map(PaperQuestion::getQuestionId)
                .collect(Collectors.toList());
        //获取所有的问题
        List<Question> questions = null;
        if (CollUtil.isNotEmpty(questionIds)){
            questions = questionService.listByIds(questionIds);
        }
        return Result.success(questions);
    }
    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody PaperQuestion paperQuestion) {
        if (paperQuestion.getId() == null) {
            //paperQuestion.setTime(DateUtil.now());
            //paperQuestion.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        paperQuestionService.saveOrUpdate(paperQuestion);
        return Result.success();
    }

    /**
     * 手动添加题目
     * @param paperQuestionList
     * @return
     */
    @PostMapping("/all")
    public Result saveAll(@RequestBody List<PaperQuestion> paperQuestionList) {
        //获取试卷id
        PaperQuestion paperQuestion = paperQuestionList.get(0);
        Integer paperId = paperQuestion.getPaperId();
        //获取当前试卷所有题目
        List<PaperQuestion> questionList = paperQuestionService.list(new LambdaQueryWrapper<PaperQuestion>()
                .eq(PaperQuestion::getPaperId, paperId));
        //获取所有题目id
        List<Integer> questionId = questionList.stream()
                .map(PaperQuestion::getQuestionId)
                .collect(Collectors.toList());
        //添加题目的所有id
        List<Integer> addQuestionids = paperQuestionList.stream()
                .map(PaperQuestion::getQuestionId)
                .collect(Collectors.toList());
        //去除null
        addQuestionids = CollUtil.removeNull(addQuestionids);
        //寻找是否有相同题目
//        log.info(questionId.toString());
//        log.info(addQuestionids.toString());
        if (!CollUtil.isNotEmpty(questionId) || !CollUtil.isNotEmpty(addQuestionids) ){
            throw new ServiceException(ErrorCode.NO_Null);
        }
        //判断题目是否重复
        for (Integer ids : questionId) {
            for (Integer addIds : addQuestionids) {
                 if (ids.equals(addIds)){
                     throw new ServiceException(ErrorCode.REPEAT_ADD);
                 }
            }
        }
        paperQuestionService.saveBatch(paperQuestionList);
        paperService.getPaperScore(paperId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        paperQuestionService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/{paperId}/{questionId}")
    public Result deleteByQuestionId(@PathVariable Integer paperId,@PathVariable Integer questionId) {
        LambdaQueryWrapper<PaperQuestion> paperQuestionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        paperQuestionLambdaQueryWrapper.eq(questionId !=null, PaperQuestion::getQuestionId,questionId);
        paperQuestionLambdaQueryWrapper.eq(paperId !=null, PaperQuestion::getPaperId,paperId);
        boolean remove = paperQuestionService.remove(paperQuestionLambdaQueryWrapper);
        if (remove){
            return Result.success();
        }else {
            return Result.error();
        }

    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        paperQuestionService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(paperQuestionService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(paperQuestionService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<PaperQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(paperQuestionService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<PaperQuestion> list = paperQuestionService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("PaperQuestion信息表", "UTF-8");
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
        List<PaperQuestion> list = reader.readAll(PaperQuestion.class);

        paperQuestionService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

