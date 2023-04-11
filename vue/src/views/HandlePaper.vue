<template>
<div>
  <el-table :data="papers" border stripe :header-cell-class-name="'headerBg'">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
    <el-table-column prop="name" label="题目"></el-table-column>
    <el-table-column prop="type" label="类型">
      <template v-slot="scope">
        <span v-if="scope.row.type===1" >选择题</span>
        <span v-if="scope.row.type===2">判断题</span>
        <span v-if="scope.row.type===3">问答题</span>
        <span v-if="scope.row.type===4">多选题</span>
        <span v-if="scope.row.type===5">填空题</span>
      </template>
    </el-table-column>
    <el-table-column prop="score" label="分数"></el-table-column>
    <el-table-column prop="answer" label="标准答案"></el-table-column>
    <el-table-column prop="studentAnswer" label="学生答案"></el-table-column>
    <el-table-column prop="studentScore" label="得分">
      <template v-slot="scope">
        <el-input type="text" v-model="scope.row.studentScore"></el-input>
      </template>
    </el-table-column>
  </el-table>

  <el-button @click="submitScore">提交</el-button>
</div>
</template>

<script>
import It from "element-ui/src/locale/lang/it";
import request from "@/utils/request";

export default {
  name: "HandlePaper",
  data(){
    return{
      sp:this.$route.query.sp,
      papers:[],
      studentId:'',

    }
  },
  created() {
    this.request.get("/studentPaper/"+this.sp).then(res=>{
      if (res.code==='200'){
        this.papers=JSON.parse(res.data.paper)
        if (this.papers&&this.papers.length){
          this.papers.forEach(item=>{
            if (item.type===1||item.type===2){
              if (item.answer === item.studentAnswer){
                item.studentScore=item.score
              }else {
                item.studentScore=0
            }
            }
          })
        }
      }
    })
    //获取学生id
    request.get("/studentPaper/"+this.sp).then(res=> {
        if (res.code ==='200'){
          this.studentId =res.data.userId
        }
    })
  },
  methods:{
    submitScore(){
      let sum=0
      const param = []
      this.papers.forEach(item=>{
        if (item.studentScore==null){
          item.studentScore=0
        }
         sum+=parseInt(item.studentScore)
        if (item.studentScore != item.score){
          param.push({"questionId":item.id, "studentId":this.studentId,"studentPaperId":this.sp,
            "studentAnswer":item.studentAnswer, "studentScore": item.studentScore})
        }
      })
      request.post("/wrong/all",JSON.stringify(param)).then(res =>{
        if (res.code ==='200'){
          console.log("生成错题成功")
        }
      })
      this.request.put("/studentPaper",{id:this.sp,score:sum,paper:JSON.stringify(this.papers),status:1}).then(res=>{
        if (res.code==='200'){
          this.$router.push("/studenpaper")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>