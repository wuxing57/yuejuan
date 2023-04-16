<template>

    <div>
        <div>
            <div>考试名称：{{exam.name}}</div>
            <div>考试科目：{{course.name}}</div>
            <div>开始时间：{{exam.time}}</div>
            <div>考试时长：{{paper.duration}}</div>
            <div>总分：{{paper.score}}</div>
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
    </div>


<!--<div>-->
<!--    <el-card>-->
<!--      <div>考试名称：{{exam.name}}</div>-->
<!--      <div>考试科目：{{course.name}}</div>-->
<!--      <div>开始时间：{{exam.time}}</div>-->
<!--      <div>考试时长：{{paper.duration}}</div>-->
<!--      <div>总分：{{paper.score}}</div>-->
<!--      <aa/>-->
<!--      <div v-for="(item,index) in questions " :key="questions.id" >-->
<!--        <div>-->
<!--          {{index+1}}.{{item.name}}-->
<!--          <span v-if="item.type===1">(选择题)</span>-->
<!--          <span v-if="item.type===2">(判断题)</span>-->
<!--          <span v-if="item.type===3">(问答题)</span>-->
<!--          <span v-if="item.type===4">(多选题)</span>-->
<!--          <span v-if="item.type===5">(填空题)</span>-->
<!--        </div>-->
<!--        <div v-if="item.type===1">-->
<!--          <span><el-radio  v-model="item.studentAnswer" label="A">A.{{item.a}}</el-radio></span>-->
<!--          <span><el-radio  v-model="item.studentAnswer" label="B">B.{{item.b}}</el-radio></span>-->
<!--          <span><el-radio  v-model="item.studentAnswer" label="C">C.{{item.c}}</el-radio></span>-->
<!--          <span><el-radio  v-model="item.studentAnswer" label="D">D.{{item.d}}</el-radio></span>-->
<!--        </div>-->
<!--        <div v-if="item.type===2">-->
<!--          <span><el-radio  v-model="item['studentAnswer']" label="是">是</el-radio></span>-->
<!--          <span><el-radio  v-model="item['studentAnswer']" label="否">否</el-radio></span>-->
<!--        </div>-->
<!--        <div v-if="item.type===3">-->
<!--          <span><el-input type="textarea" v-model="item['studentAnswer']"></el-input></span>-->
<!--        </div>-->
<!--        <div  v-if="item.type===4">-->
<!--          <el-checkbox-group v-model="more" >-->
<!--            <el-checkbox label="A" >A.{{item.a}}</el-checkbox>-->
<!--            <el-checkbox label="B" >B.{{item.b}}</el-checkbox>-->
<!--            <el-checkbox label="C" >C.{{item.c}}</el-checkbox>-->
<!--            <el-checkbox label="D" >D.{{item.d}}</el-checkbox>-->
<!--          </el-checkbox-group>-->
<!--        </div>-->
<!--        <div v-if="item.type===5">-->
<!--          <span><el-input v-model="item['studentAnswer']"></el-input></span>-->
<!--        </div>-->
<!--      </div>-->
<!--    </el-card>-->
<!--  <div v-if="type === 1">-->
<!--    <el-button @click="submitPaper">提交</el-button>-->
<!--    <el-button @click="$router.push('/front/home')">取消</el-button>-->
<!--  </div>-->
<!--</div>-->
</template>

<script>
import request from "@/utils/request";
export default {
  data(){
    return{
      questions:[],
      more:[],
      answers:[],
      studentAnswer:[],
      exam:{},
      paper:{},
      course:{},
      currentTime:Date.now(),
      startTime:0,
      endTime:0,
      examId:this.$route.query.examId,
      studentPaperId:this.$route.query.studentPaperId,
      type:this.$route.query.type,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    if (this.type === 1){
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
    if (this.type ===2){
       request.get("/studentPaper/"+this.studentPaperId).then(res =>{
           if (res.code ==='200'){
             this.questions = JSON.parse(res.data.paper)
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
    watch:{
      answers:{
          handler(newVal, oldVal){
              console.log(newVal)
          }
      }
    },
  methods:{
    submitPaper(){
        console.log("提交：",this.answers)
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
  }

}
</script>

<style scoped>

</style>