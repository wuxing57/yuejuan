<template >
  <div>
      <div>
        
          <div style="border-radius: 10px; margin: 15px" v-for="item in tableData" :key="item.id">
            <el-card>
              <div style="color: #666; padding-bottom: 20px; font-size: 23px" class="el-icon-star-off"> {{ item.name }}</div>
              <div style="padding-bottom: 20px;">
                <span style="font-size: 18px;">考试地点：{{item.room}}</span>&nbsp;&nbsp;
                <span style="font-size: 18px;">考试时间：{{item.time}}</span>&nbsp;&nbsp;
                <span style="font-size: 18px;">监考老师：{{item.teacher}}</span>&nbsp;&nbsp;
                <span style="font-size: 18px;">考试状态：{{item.state}}</span>&nbsp;&nbsp;
              </div>
              <div >
                  <span><el-button @click="sign(item.id)" :disabled="notClickSign(item.time)">报名</el-button></span>&nbsp&nbsp
                  <span><el-button @click="attend(item.id)" >参加考试</el-button></span>
              </div>
            </el-card>
          </div>
        
      </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      dialogFormVisible: false,
      name: "",
      form:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
      console.log(this.user)
  },
  methods: {
    load() {
      request.get("/exam/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }),
          this.request.get("/course").then(res => {
            this.courses=res.data
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
    attend(examId){
      const param={examId:examId,userId:this.user.id}
      this.request.post("/sign/attend", param).then(res => {
        if (res.code === '200') {
          this.$router.push({
            name:'paper',
            query:{
              examId: examId,
              type: 1
            }
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    sign(examId) {
      const form1={examId:examId,userId:this.user.id}
      this.request.post("/sign/reg", form1).then(res => {
        if (res.code === '200') {
          this.$message.success("报名成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
      notClickSign(time){
        let time1 = new Date(time).getTime();
        let now = new Date().getTime();
        return now > time1
      },
      // notClickAttend(time){
      //     let time1 = new Date(time).getTime();
      //     let now = new Date().getTime();
      //     return now > time1
      // },
  }
}
</script>

<style>

</style>
