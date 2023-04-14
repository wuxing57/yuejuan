<template>
  <div>
      <div style="" v-show="userInfo.classId == null">
          <el-button @click="addClass()">还没有加入班级？点击加入</el-button>
      </div>
      <div>
          <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
              <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
                  <el-form-item label="班级密钥">
                      <el-input  v-model="form.key" autocomplete="off" clearable ></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="save">确 定</el-button>
              </div>
          </el-dialog>
      </div>
      <div style="margin-bottom: 10px">
          <div style="border: 1px solid #cccccc ; border-radius: 10px; margin: 10px" v-for="item in tableData" :key="item.id">
              <div style="color: #666; padding: 10px ;font-size: 20px" >{{ item.name }}</div>
              <span>考试地点：{{item.room}}</span>&nbsp&nbsp
              <span>考试时间：{{item.time}}</span>&nbsp&nbsp
              <span>监考老师：{{item.teacher}}</span>&nbsp&nbsp
              <span>考试状态：{{item.state}}</span>&nbsp&nbsp

              <div>
                  <span><el-button @click="sign(item.id)" :disabled="notClick(item.time)">报名</el-button></span>&nbsp&nbsp
                  <span><el-button @click="attend(item.id)" :disabled="notClick(item.time)">参加考试</el-button></span>
              </div>

          </div>
      </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "FrontHome",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      dialogFormVisible: false,
      name: "",
      form:{},
      userInfo:[],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
      console.log(this.user)
  },
  methods: {
    load() {
        this.request.get("/user/"+this.user.id).then(res=>{
            if (res.code == 200){
                this.userInfo = res.data
            }
        })
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
    },
      notClick(time){
        let time1 = new Date(time).getTime();
        let time2 = new Date().getTime();
        return time2 > time1
      },
      addClass(){
        this.dialogFormVisible = true
      },
      save(){
        request.put("/cla/addClass/"+this.form.key+"/"+this.user.id).then(res=>{
              if (res.code == 200){
                  this.$message.success("加入班级成功")
                  this.dialogFormVisible = false
                  this.load()
              }else {
                  this.$message.error(res.msg)
              }
        })
      }
  }
}
</script>

<style>

</style>
