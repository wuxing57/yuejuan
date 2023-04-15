<template>
<div>
  <el-card>
      <div>
          <div style="margin: 10px 0">
              <el-select v-model="courseId" placeholder="请选择课程" >
                  <el-option
                          v-for="item in courses"
                          :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
              </el-select>
              <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
              <el-button type="warning" @click="reset">重置</el-button>
          </div>
      </div>
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
      <div style="padding: 10px 0">
          <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="pageNum"
                  :page-sizes="[2, 5, 10, 20]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="total">
          </el-pagination>
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
        total: 0,
        pageNum: 1,
        pageSize: 10,
        courses:[],
        courseId:'',
    }
  },
  created() {
   this.load()
  },
  methods:{
      load(){
          request.get('/wrong/'+this.user.id,{
              params:{
                  pageNum: this.pageNum,
                  pageSize: this.pageSize,
                  courseId: this.courseId
              }
          }).then(res =>{
              if (res.code ==='200'){
                  this.wrong = res.data.data
                  this.total = res.data.total
              }
          })
          request.get("/course").then(res=>{
               if (res.code == 200){
                   this.courses = res.data
               }
          })
      },
    questionType(type){
      if (type === 1) return '选择题'
      if (type === 2) return '判断题'
      if (type === 3) return '问答题'
      if (type === 4) return '多选题'
      if (type === 5) return '填空题'
    },
      handleSizeChange(pageSize) {
          console.log(pageSize)
          this.pageSize = pageSize
          this.load()
      },
      handleCurrentChange(pageNum) {
          console.log(pageNum)
          this.pageNum = pageNum
          this.load()
      },
      reset() {
          this.courseId = ""
          this.load()
      },
  },
}
</script>

<style scoped>

</style>