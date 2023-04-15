<template>
<div>
  <el-card >
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
     <div v-for="item in paperList">
       <div>考试id：{{item.id}}</div>
       <div>试卷名称：{{item.examName}}</div>
       <div>课程名称：{{item.courseName}}</div>
       <div>考试时间：{{item.time}}</div>
       <div>考试状态：{{item.status}}</div>
       <div>
         <el-button @click="viewPaper(item.id)">查看试卷</el-button>
       </div>
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
           request.get("/studentPaper/user/"+this.user.id,{
               params:{
                   pageNum: this.pageNum,
                   pageSize: this.pageSize,
                   courseId: this.courseId
               }
           }).then(res =>{
               if (res.code ==='200'){
                   this.paperList = res.data.data
                   this.total = res.data.total
                   console.log("考试记录："+ this.paperList)
               }
           })
           request.get("/course").then(res=>{
               if (res.code == 200){
                   this.courses = res.data
               }
           })
       },
     viewPaper(id){
       console.log("id"+id)
       this.$router.push({
         name:'paper',
         query:{
           studentPaperId: id,
            type: 2
         }
       })
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
  }
}
</script>

<style scoped>

</style>