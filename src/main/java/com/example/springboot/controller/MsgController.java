package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.vo.MsgVo;
import com.example.springboot.controller.vo.QuestionVo;
import com.example.springboot.service.IUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IMsgService;
import com.example.springboot.entity.Msg;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private IMsgService msgService;
    @Resource
    private IUserService userService;


    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Msg msg) {
        if (msg.getId() == null) {
            msg.setCreateTime(DateUtil.now());
            msg.setReadNum(0);
            //msg.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        msgService.saveOrUpdate(msg);
        return Result.success();
    }

    @GetMapping("/getRec")
    public Result getRec(){
        Set<Integer> recIds = msgService.list().stream().map(Msg::getRec).collect(Collectors.toSet());
        List<User> users = userService.listByIds(recIds);
        return Result.success(users);
    }
    @PutMapping("/read/{msgId}")
    public Result changeRead(@PathVariable Integer msgId){
        Msg msg = msgService.getById(msgId);
        msg.setReadNum(1);
        msgService.updateById(msg);
        return Result.success();
    }
    @GetMapping("/user/{userId}")
    public Result getByUserId(@PathVariable Integer userId){
        List<Msg> msgList = msgService.list(Wrappers.<Msg>lambdaQuery().eq(Msg::getRec, userId));
        return Result.success(msgList);
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        msgService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        msgService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(msgService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(msgService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") Integer rec,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        List<MsgVo>  data = null;
        Integer count = msgService.getPageTotal(rec);
        if (count > 0){
            data = msgService.getPageData(rec, pageNum, pageSize);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("total",count);
        result.put("records",data);
        return Result.success(result);
    }
    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Msg> list = msgService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Msg信息表", "UTF-8");
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
        List<Msg> list = reader.readAll(Msg.class);

        msgService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

