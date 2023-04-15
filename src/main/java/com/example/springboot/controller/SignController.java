package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.vo.SignExcel;
import com.example.springboot.controller.vo.SignVo;
import com.example.springboot.entity.Exam;
import com.example.springboot.entity.StudentPaper;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IExamService;
import com.example.springboot.service.IStudentPaperService;
import com.example.springboot.service.IUserService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.ISignService;
import com.example.springboot.entity.Sign;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/sign")
public class SignController {

    @Resource
    private ISignService signService;
    @Resource
    private IExamService examService;
    @Resource
    IUserService userService;
    @Resource
    private IStudentPaperService studentPaperService;

    private final String now = DateUtil.now();


    /**
     * 参加考试
     * @param sign
     * @return
     */
    @PostMapping("/attend")
    public Result attend(@RequestBody Sign sign){
        Sign getSign = signService.getOne(Wrappers.<Sign>lambdaQuery()
                .eq(Sign::getUserId, sign.getUserId())
                .eq(Sign::getExamId, sign.getExamId()));
        if (getSign==null){
            throw new ServiceException("-1","没有报名不能参加考试");
        }
        if (!getSign.getStatus().equals("审核成功")){
            throw new ServiceException("-1","审核没有通过不能参加考试");
        }
        //查询考试信息
        Exam exam = examService.getOne(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getId, sign.getExamId()));
        if (exam != null && exam.getState().equals("已结束")){
            throw new ServiceException("-1","考试已结束不能参加考试");
        }
        if (exam != null && exam.getState().equals("未开始")){
            throw new ServiceException("-1","考试还未开始，请于："+exam.getTime()+"准时参加考试");
        }
        //校验重复参加考试
//        StudentPaper getStudentPaper = studentPaperService.getOne(Wrappers.<StudentPaper>lambdaQuery()
//                .eq(StudentPaper::getExamId, sign.getExamId())
//                .eq(StudentPaper::getUserId, sign.getUserId()));
//        if (getStudentPaper!=null){
//            throw new ServiceException("-1","你已经参加考试，不能重复参加");
//        }

        return Result.success();
    }
    /**
     * 审核成功
     * @param id
     * @return
     */
    @PostMapping("/success/{id}")
    public Result success(@PathVariable Integer id){
        signService.update(Wrappers.<Sign>lambdaUpdate()
                .eq(Sign::getId,id)
                .set(Sign::getStatus,"审核成功"));
        return Result.success();
    }

    /**
     * 审核失败
     * @param id
     * @return
     */
    @PostMapping("/fail/{id}")
    public Result fail(@PathVariable Integer id){
        signService.update(Wrappers.<Sign>lambdaUpdate()
                .eq(Sign::getId,id)
                .set(Sign::getStatus,"审核失败"));
        return Result.success();
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Sign sign) {

        if (sign.getId() == null) {
            //sign.setTime(DateUtil.now());
            //sign.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        signService.saveOrUpdate(sign);
        return Result.success();
    }

    /**
     * 报名
     * @param sign
     * @return
     */
    @PostMapping("/reg")
    public Result reg(@RequestBody Sign sign) {
        Sign getSign = signService.getOne(Wrappers.<Sign>lambdaQuery()
                .eq(Sign::getUserId, sign.getUserId())
                .eq(Sign::getExamId, sign.getExamId()));
        if (getSign!=null){
            return Result.error("-1","已经成功报名，无需再次报名");
        }
        Sign newSign = new Sign();
        newSign.setUserId(sign.getUserId());
        newSign.setExamId(sign.getExamId());
        newSign.setStatus("报名成功，待审核");
        signService.save(newSign);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        signService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        signService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(signService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(signService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Sign> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<Sign> signPage = signService.page(new Page<>(pageNum, pageSize), queryWrapper);
        IPage<SignVo> signVoIPage = signPage.convert(sign -> {
            SignVo signVo = new SignVo();
            BeanUtils.copyProperties(sign, signVo);
            return signVo;
        });
        if (signVoIPage != null){
            List<Integer> userIds = signVoIPage.getRecords().stream().map(Sign::getUserId).collect(Collectors.toList());
            List<Integer> examIds = signVoIPage.getRecords().stream().map(Sign::getExamId).collect(Collectors.toList());
            List<Exam> examList = examService.listByIds(examIds);
            List<User> userList = userService.listByIds(userIds);
            Map<Integer, String> examMap = examList.stream().collect(Collectors.toMap(Exam::getId, Exam::getName));
            Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(User::getId, User::getUsername));
            signVoIPage.convert(signVo -> {
                signVo.setExamName(examMap.get(signVo.getExamId()));
                signVo.setStudentName(userMap.get(signVo.getUserId()));
                return signVo;
            });
        }
        return Result.success(signVoIPage);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Sign> list = signService.list();

        List<Integer> userIds = list.stream().map(Sign::getUserId).collect(Collectors.toList());
        List<Integer> examIds = list.stream().map(Sign::getExamId).collect(Collectors.toList());

        Map<Integer, String> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));
        Map<Integer, String> examMap = examService.listByIds(userIds).stream().collect(Collectors.toMap(Exam::getId, Exam::getName));

        ArrayList<SignExcel> signExcelArrayList = new ArrayList<>();
        for (Sign sign : list) {
            SignExcel signExcel = new SignExcel();
            signExcel.setStatus(sign.getStatus());
            signExcel.setUserName(userMap.get(sign.getUserId()));
            signExcel.setExamName(examMap.get(sign.getExamId()));
            signExcelArrayList.add(signExcel);
        }
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("userName", "姓名");
        map.put("examName", "考试名称");
        map.put("status", "状态");
        writer.setHeaderAlias(map);
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(signExcelArrayList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Sign信息表", "UTF-8");
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
        List<Sign> list = reader.readAll(Sign.class);

        signService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

