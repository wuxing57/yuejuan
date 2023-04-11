<template>
<div>
  <el-card>
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

     }
 },
  created() {
     request.get("/studentPaper/user/"+this.user.id).then(res =>{
         if (res.code ==='200'){
           this.paperList = res.data

         }
     })
  },
  methods:{
     viewPaper(id){
       console.log("id"+id)
       this.$router.push({
         name:'paper',
         query:{
           studentPaperId: id,
            type: 2
         }
       })
     }
  }
}
</script>

<style scoped>

</style>