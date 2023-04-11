//package com.example.springboot.mq;
//
//import cn.hutool.json.JSON;
//import cn.hutool.json.JSONUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.example.springboot.controller.dto.PaperDto;
//import com.example.springboot.entity.Paper;
//import com.example.springboot.entity.PaperQuestion;
//import com.example.springboot.entity.Question;
//import com.example.springboot.entity.StudentPaper;
//import com.example.springboot.service.IPaperQuestionService;
//import com.example.springboot.service.IPaperService;
//import com.example.springboot.service.IQuestionService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class MqConsume {
//    @Autowired
//    private IPaperQuestionService paperQuestionService;
//    @Autowired
//    private IQuestionService questionService;
//    @Autowired
//    private IPaperService paperService;
//    @KafkaListener(topics = {"first-topic"},groupId = "test-consumer-group")
//    public void receive(Object message){
//
//        log.info("我是消费者，我接收到的消息是："+message);
//        JSON json = JSONUtil.parse(message);
//        StudentPaper studentPaper = json.toBean(StudentPaper.class);
//
//        String paper = studentPaper.getPaper();
//    }
//    @KafkaListener(topics = {"score"},groupId = "test-consumer-group")
//    public void computerScore(String message){
//        log.info("我是消费者，我接收到的消息是："+message);
//        JSON json = JSONUtil.parse(message);
//        PaperDto paperDto = json.toBean(PaperDto.class);
//        Integer paperId = paperDto.getPaperId();
//        Integer courseId = paperDto.getCourseId();
//        List<PaperQuestion> paperQuestions = paperQuestionService
//                .list(new LambdaQueryWrapper<PaperQuestion>()
//                        .eq(PaperQuestion::getPaperId, paperId));
//        List<Integer> questionIds = paperQuestions.stream()
//                .map(PaperQuestion::getQuestionId)
//                .collect(Collectors.toList());
//        List<Question> questions = questionService.listByIds(questionIds);
//        Integer score = 0;
//        for (Question question : questions) {
//             score +=  question.getScore();
//        }
//        Paper paper = new Paper();
//        paper.setId(paperId);
//        paper.setScore(score);
//        paperService.updateById(paper);
//    }
//}
