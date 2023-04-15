package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.vo.MsgVo;
import com.example.springboot.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
    public Result findPage(@RequestParam(defaultValue = "") String rec,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Msg> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(rec)) {
            queryWrapper.like("rec", rec);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        Page<Msg> msgPage = msgService.page(new Page<>(pageNum, pageSize), queryWrapper);
        IPage<MsgVo> msgVoIPage = msgPage.convert(msg -> {
            MsgVo msgVo = new MsgVo();
            BeanUtils.copyProperties(msg, msgVo);
            return msgVo;
        });
        if (msgVoIPage!= null){
            List<Integer> sendIds = msgVoIPage.getRecords().stream()
                    .map(Msg::getSend)
                    .collect(Collectors.toList());
            List<Integer> recIds = msgVoIPage.getRecords().stream()
                    .map(Msg::getRec)
                    .collect(Collectors.toList());
            List<User> sendList = userService.listByIds(sendIds);
            List<User> recList = userService.listByIds(recIds);
            Map<Integer, String> sendMap = sendList.stream().collect(Collectors.toMap(User::getId, User::getUsername));
            Map<Integer, String> recMap = recList.stream().collect(Collectors.toMap(User::getId, User::getUsername));
            msgVoIPage.convert(msgVo -> {
        msgVo.setSendUsername(sendMap.get(msgVo.getSend()));
        msgVo.setRecUsername(recMap.get(msgVo.getRec()));
        return msgVo;
    });
}
        return Result.success(msgVoIPage);
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

