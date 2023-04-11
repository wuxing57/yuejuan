<template>
  <div style="margin-bottom: 10px">
    <div style="border: 1px solid #cccccc ; border-radius: 10px; margin: 10px" v-for="item in tableData" :key="item.id">
            <div style="color: #666; padding: 10px ;font-size: 20px" >{{ item.name }}</div>
            <span>考试地点：{{item.room}}</span>&nbsp&nbsp
            <span>考试时间：{{item.time}}</span>&nbsp&nbsp
            <span>监考老师：{{item.teacher}}</span>&nbsp&nbsp
            <span>考试状态：{{item.state}}</span>&nbsp&nbsp

      <div>
        <span><el-button @click="sign(item.id)">报名</el-button></span>&nbsp&nbsp
        <span><el-button @click="attend(item.id)">参加考试</el-button></span>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
    this.request.get("/echarts/file/front/all").then(res => {
      console.log(res.data)
      this.files = res.data.filter(v => v.type === 'png' || v.type === 'jpg' || v.type === 'webp')
    })
  },
  methods: {
    load() {
      this.request.get("/exam/page", {
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
    }

  }
}
</script>

<style>

</style>
