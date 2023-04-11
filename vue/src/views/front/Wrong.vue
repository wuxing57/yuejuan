<template>
<div>
  <el-card>
     <div v-for="item in wrong" :key="item.id">
       <div>学生答案：{{item.wrong.studentAnswer}}</div>
       <div>学生得分：{{item.wrong.studentScore}}</div>
       <div>题干：{{item.question.name}}</div>
       <div>题型：{{questionType(item.question.type)}}</div>
       <div>题目分数：{{item.question.score}}</div>
       <div>题目解析：{{item.question.detial}}</div>
       <div>题目答案：{{item.question.answer}}</div>
       <el-divider></el-divider>
     </div>
  </el-card>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      paperList:[],
      wrong:[],

    }
  },
  created() {
    request.get('/wrong/'+this.user.id).then(res =>{
        if (res.code ==='200'){
           this.wrong = res.data
        }
    })
  },
  methods:{
    questionType(type){
      if (type === 1) return '选择题'
      if (type === 2) return '判断题'
      if (type === 3) return '问答题'
      if (type === 4) return '多选题'
      if (type === 5) return '填空题'
    }
  },
}
</script>

<style scoped>

</style>