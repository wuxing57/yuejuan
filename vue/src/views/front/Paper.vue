<template>

    <div>
        <div>
            <div>考试名称：{{exam.name}}</div>
            <div>考试科目：{{course.name}}</div>
            <div>开始时间：{{exam.time}}</div>
            <div>考试时长：{{paper.duration}}</div>
            <div v-if="type === 2">成绩：{{score}}</div>
            <div>总分：{{paper.score}}</div>
            <div v-if="type === 1">考试剩余时间：
                <van-count-down :time="time">
                    <template #default="timeData">
                        <span class="block">{{ timeData.hours }}</span>
                        <span class="colon">:</span>
                        <span class="block">{{ timeData.minutes }}</span>
                        <span class="colon">:</span>
                        <span class="block">{{ timeData.seconds }}</span>
                    </template>
                </van-count-down>
            </div>
        </div>
        <div>
            <el-button @click="back">返回</el-button>
        </div>
        <div v-for="(item, index) in questions" :key="item.id">
            <div>
                {{ index + 1 }}.{{ item.name }}
                <span v-if="item.type === 1">(选择题)</span>
                <span v-if="item.type === 2">(判断题)</span>
                <span v-if="item.type === 3">(问答题)</span>
                <span v-if="item.type === 4">(多选题)</span>
                <span v-if="item.type === 5">(填空题)</span>
            </div>
            <div v-if="item.type === 1">
                <el-radio-group v-model="answers[index]">
                    <el-radio v-model="item.answer" label="A">A.{{item.a}}</el-radio>
                    <el-radio v-model="item.answer" label="B">B.{{item.a}}</el-radio>
                    <el-radio v-model="item.answer" label="C">C.{{item.a}}</el-radio>
                    <el-radio v-model="item.answer" label="D">D.{{item.a}}</el-radio>
                </el-radio-group>
            </div>
            <div v-if="item.type === 2">
                <el-radio-group v-model="answers[index]">
                    <el-radio label="是">正确</el-radio>
                    <el-radio label="否">错误</el-radio>
                </el-radio-group>
            </div>
            <div v-if="item.type === 3">
                <el-input v-model="answers[index]" type="textarea" :rows="4"></el-input>
            </div>
            <div v-if="item.type === 4">
                <el-checkbox-group v-model="answers[index]">
                    <el-checkbox label="A" v-model="item.answer">A.{{item.a}}</el-checkbox>
                    <el-checkbox label="B" v-model="item.answer">B.{{item.a}}</el-checkbox>
                    <el-checkbox label="C" v-model="item.answer">C.{{item.a}}</el-checkbox>
                    <el-checkbox label="D" v-model="item.answer">D.{{item.a}}</el-checkbox>
                </el-checkbox-group>
            </div>
            <div v-if="item.type === 5">
                <el-input v-model="answers[index]" type="text"></el-input>
            </div>
        </div>
        <div v-if="type === 1">
            <el-button @click="submitPaper">提交</el-button>
            <el-button @click="$router.push('/front/home')">取消</el-button>
        </div>
        <div>
            <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
                <div>
                    考试已结束，试卷自动提交，请点击确认按钮退出考试系统
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="back">确 定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";
import Vue from 'vue';
import { CountDown } from 'vant';
Vue.use(CountDown)
export default {
  data(){
    return{
      questions:[],
      more:[],
      answers:[],
      studentAnswer:[],
      exam:{},
        endTime:0,
        timer:'',
      paper:{},
      time:'',
      isSubmit:false,
      course:{},
      dialogFormVisible:false,
      score:'',
      examId:this.$route.query.examId,
      studentPaperId:this.$route.query.studentPaperId,
      type:this.$route.query.type,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
      console.log("created")
      this.load()
    },
    mounted() {
        this.timer = setInterval(() => {
            if (Date.now() > this.endTime) {
                if (!this.isSubmit){
                    this.isSubmit = true
                    console.log("提交试卷")
                    //this.submitExam()
                }
            }
        }, 1000)
    },
  methods:{
      load(){
          if (this.type == 1){
              console.log("type=1")
              this.request.get("/exam/"+this.examId).then(res =>{
                  console.log("examId:"+this.examId)
                  if (res.code ==='200'){
                      this.exam = res.data

                      this.request.get('/course/'+this.exam.courseId).then(res =>{
                          if (res.code ==='200'){
                              this.course = res.data
                          }
                      })
                  }
              })
              this.request.get("/examPaper/exam/"+this.examId).then(res => {
                  if (res.code==='200'){
                      console.log("paperId="+res.data)
                      const paperId = res.data.paperId
                      this.request.get("/paper/"+paperId).then(res =>{
                          if (res.code ==='200'){
                              this.paper = res.data
                              this.time = this.getTime()
                          }
                      })
                      this.request.get("/paper/view/"+paperId).then(res => {
                          if (res.code === '200') {
                              this.questions=res.data
                              this.answers = Array(this.questions.length).fill([])
                              //console.log("question:",this.questions)
                          }
                      })
                  }else{
                      this.$message.error("考试还未出卷，请联系管理员")
                  }
              })
          }
          if (this.type ==2){
              console.log("type=2")
              request.get("/studentPaper/"+this.studentPaperId).then(res =>{
                  if (res.code ==='200'){
                      this.questions = JSON.parse(res.data.paper)
                      this.questions.forEach(item=>{
                          if (item.type === 4){
                              item.studentAnswer = item.studentAnswer.split(',');
                          }
                          this.answers.push(item.studentAnswer)
                      })
                      request.get("/studentPaper/"+this.studentPaperId).then(res =>{
                          console.log("studentPaper",res.data)
                          this.score = res.data.score
                      })
                      console.log("answer:",this.answers)
                      const examId = res.data.examId
                      console.log("examId:"+typeof examId)
                      this.request.get("/exam/"+examId).then(res =>{
                          if (res.code ==='200'){
                              this.exam = res.data
                              this.request.get('/course/'+this.exam.courseId).then(res =>{
                                  if (res.code ==='200'){
                                      this.course = res.data
                                  }
                              })
                              request.get("/examPaper/exam/"+ examId).then(res =>{
                                  if (res.code ==='200'){
                                      const paperId = parseInt(res.data.paperId)
                                      request.get("/paper/"+paperId).then(res =>{
                                          if (res.code ==='200'){
                                              this.paper = res.data
                                          }
                                      })
                                  }
                              })
                          }
                      })
                  }
              })
          }
      },
    submitPaper(){
        console.log("提交：",this.answers)
        this.isSubmit = true
        let i=0
        this.questions.forEach(item=>{
            item.studentAnswer = this.answers[i]
            if (item.type === 4){
                this.answers[i].sort()
                item.studentAnswer =  item.studentAnswer.toString()
            }
             i++
        })
        i = 0
        console.log(this.questions)
      this.request.post("/studentPaper",{examId:this.examId,paper: JSON.stringify(this.questions),userId:this.user.id}).then(res => {
        if (res.code === '200') {
          this.$message.success("提交成功")
        }else {
          this.$message.error(res.msg)
        }
      })
    },
      back(){
          console.log("back")
        if (this.type === 1){
            this.$router.push("/front/home")
        }
        if (this.type === 2){
            this.$router.push("/front/record")
        }
      },
      getTime(){
         let start = new Date(this.exam.time).getTime();
         let now = Date.now();
         let end = start + this.paper.duration * 60000
          this.endTime = end
          let remainingTime = end - now
          if (remainingTime >= 0){
              return  end - now
          }else {
              return  0
          }
      },
  }

}
</script>

<style scoped>
.colon {
    display: inline-block;
    margin: 0 4px;
    color: #ee0a24;
}
.block {
    display: inline-block;
    width: 22px;
    color: #fff;
    font-size: 12px;
    text-align: center;
    background-color: #ee0a24;
}
</style>