package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.HandlePaperDto;
import com.example.springboot.controller.dto.PaperDto;
import com.example.springboot.controller.vo.PaperVo;
import com.example.springboot.entity.*;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.ICourseService;
import com.example.springboot.service.IPaperQuestionService;
import com.example.springboot.service.IQuestionService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IPaperService;

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
@RequestMapping("/paper")
@Slf4j
public class PaperController {

    @Resource
    private IPaperService paperService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IQuestionService questionService;
    @Resource
    private IPaperQuestionService paperQuestionService;


    private final String now = DateUtil.now();


    /**
     * 手动组卷
     * @param handlePaperDto
     * @return
     */
    @PostMapping("/generatorPaperByHandle")
    public Result generatorPaperByHandle(@RequestBody HandlePaperDto handlePaperDto) {
        ArrayList<PaperQuestion> paperQuestionArrayList = new ArrayList<>();
        List<Question> handleQuestionId = handlePaperDto.getHandleQuestionId();
        //存储题目
        for (Question question : handleQuestionId) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(handlePaperDto.getPaperId());
            paperQuestion.setQuestionId(question.getId());
            paperQuestionArrayList.add(paperQuestion);
        }
        paperQuestionService.saveBatch(paperQuestionArrayList);
        return Result.success();
    }

    /**
     * 查看试卷
     * @param paperId
     * @return
     */
    @GetMapping("/view/{paperId}")
    public Result viewPaper(@PathVariable Integer paperId){
        //根据题目id查询题目
        List<Question> questions = questionService.getQuestionByPaperId(paperId);
        return Result.success(questions);
    }

    /**
     * 自动组卷
     * @param paperDto
     * @return
     */
    @PostMapping("/generatorPaper")
    public Result generatorPaper(@RequestBody PaperDto paperDto) {
        //题目表
        ArrayList<PaperQuestion> paperQuestionArrayList = new ArrayList<>();
        //重复组卷先删除原来的
        paperQuestionService.remove(Wrappers.<PaperQuestion>lambdaQuery()
                .eq(PaperQuestion::getPaperId, paperDto.getPaperId()));
        //获取课程对应的所有题目
        List<Question> questionList = questionService.list(Wrappers.<Question>lambdaQuery()
                .eq(Question::getCourseId, paperDto.getCourseId())
                .eq(paperDto.getLevel() != null && paperDto.getLevel() != 0, Question::getLevel, paperDto.getLevel())
                .eq(paperDto.getKnowledgeId()!= null && paperDto.getKnowledgeId() != 0, Question::getKnowledgeId, paperDto.getKnowledgeId()));
        //获取选择题
        List<Question> questionList1 = questionList.stream()
                .filter(question -> question.getType().equals(1))
                .collect(Collectors.toList());
        //校验题目数量
        if (questionList1.size() < paperDto.getType1()) {
            throw new ServiceException("-1", "选择题数量不够");
        }
        randomGetQuestion(paperQuestionArrayList, paperDto, questionList1, paperDto.getType1());
        //获取判断题
        List<Question> questionList2 = questionList.stream()
                .filter(question -> question.getType().equals(2))
                .collect(Collectors.toList());
        if (questionList2.size() < paperDto.getType2()) {
            throw new ServiceException("-1", "判断题数量不够");
        }
        randomGetQuestion(paperQuestionArrayList, paperDto, questionList2, paperDto.getType2());
        //获取填空题
        List<Question> questionList3 = questionList.stream()
                .filter(question -> question.getType().equals(3))
                .collect(Collectors.toList());
        if (questionList3.size() < paperDto.getType3()) {
            throw new ServiceException("-1", "问答题数量不够");
        }
        randomGetQuestion(paperQuestionArrayList, paperDto, questionList3, paperDto.getType3());
        //获取多选题
        List<Question> questionList4 = questionList.stream()
                .filter(question -> question.getType().equals(4))
                .collect(Collectors.toList());
        if (questionList4.size() < paperDto.getType3()) {
            throw new ServiceException("-1", "多选题数量不够");
        }
        randomGetQuestion(paperQuestionArrayList, paperDto, questionList4, paperDto.getType4());
        //获取填空题
        List<Question> questionList5 = questionList.stream()
                .filter(question -> question.getType().equals(5))
                .collect(Collectors.toList());
        if (questionList5.size() < paperDto.getType3()) {
            throw new ServiceException("-1", "填空题数量不够");
        }
        randomGetQuestion(paperQuestionArrayList, paperDto, questionList5, paperDto.getType5());
        //保存试卷的所有题目
        paperQuestionService.saveBatch(paperQuestionArrayList);
        //计算总分
        Integer paperId = paperDto.getPaperId();
        paperService.getPaperScore(paperId);
        return Result.success();
    }

    /**
     * 获取随机题目
     * @param paperQuestionArrayList 题目表
     * @param paperDto 试卷id
     * @param questionList 唯一类型题目
     * @param questionSize 题目数量
     * @return
     */
    private  ArrayList<PaperQuestion> randomGetQuestion(ArrayList<PaperQuestion> paperQuestionArrayList,PaperDto paperDto, List<Question> questionList,Integer questionSize) {
        //随机获取相应的题目
        List<Question> questions = RandomUtil.randomEleList(questionList, questionSize);
        //存储题目
        for (Question question : questions) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperDto.getPaperId());
            paperQuestion.setQuestionId(question.getId());
            paperQuestionArrayList.add(paperQuestion);
        }
        return paperQuestionArrayList;
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Paper paper) {
        if (paper.getId() == null) {
            //paper.setTime(DateUtil.now());
            //paper.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        paperService.saveOrUpdate(paper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        paperService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        paperService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(paperService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(paperService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") Integer courseId,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        List<PaperVo> data = null;
        Integer count = paperService.getPageTotal(name, courseId);
        if (count > 0) {
            data = paperService.getPageData(name, courseId, pageNum, pageSize);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("total", count);
        result.put("records", data);
        return Result.success(result);
    }
//        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
////        User currentUser = TokenUtils.getCurrentUser();
////        if (currentUser.getRole().equals("ROLE_USER")) {
////            queryWrapper.eq("user", currentUser.getUsername());
////        }
//        Page<Paper> paperPage = paperService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        IPage<PaperVo> paperVoIPage = paperPage.convert(paper -> {
//            PaperVo paperVo = new PaperVo();
//            BeanUtils.copyProperties(paper, paperVo);
//            return paperVo;
//        });
//        if (paperVoIPage != null){
//            List<Integer> courseIds = paperVoIPage.getRecords().stream().map(Paper::getCourseId).collect(Collectors.toList());
//            List<Course> courseList = courseService.listByIds(courseIds);
//            Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, Course::getName));
//            paperVoIPage.convert(paperVo -> {
//                paperVo.setCourseName(courseMap.get(paperVo.getCourseId()));
//                return paperVo;
//            });
//        }
//        return Result.success(paperVoIPage);


    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Paper> list = paperService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Paper信息表", "UTF-8");
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
        List<Paper> list = reader.readAll(Paper.class);

        paperService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

