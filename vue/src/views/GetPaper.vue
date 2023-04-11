<template>
  <div>
    <div>
      <el-card >
        <el-button @click="searchQuestion()">搜索题目</el-button>
        <el-button @click="backPaper()">返回</el-button>
        <ul>
          <li v-for="(item,index) in questionList" :key="item.id">
            <span>{{item.name}} {{questionType(item.type)}}  <el-button @click="delQuestion(item.id)">删除</el-button></span>
          </li>
        </ul>

      </el-card>
    </div>
    <div>
      <el-dialog title="添加题目" :visible.sync="dialogAddQuestion" width="60%" :close-on-click-modal="false" @close="closeAddQuestion">

        <div>
          <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入题干" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-select v-model="courseId" placeholder="请选择课程" >
              <el-option
                  v-for="item in courses"
                  :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
            <el-select v-model="type" placeholder="请选择题型">
              <el-option
                  v-for="item in [{name:'选择题',value:1},{name:'判断题',value:2},{name:'问答题',value:3},]"
                  :key="item.value" :label="item.name" :value="item.value">
              </el-option>
            </el-select>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
          </div>
          <el-button @click="addQuestion">添加</el-button>

          <el-table  ref="multipleTable":data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
            <el-table-column prop="name" label="题干"></el-table-column>
            <el-table-column  label="所属课程">
              <template v-slot="scope">
                <span>{{courses.find(v => v.id===scope.row.courseId).name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型">
              <template v-slot="scope">
                <span v-if="scope.row.type===1" >选择题</span>
                <span v-if="scope.row.type===2">判断题</span>
                <span v-if="scope.row.type===3">问答题</span>
                <span v-if="scope.row.type===4">多选题</span>
                <span v-if="scope.row.type===5">填空题</span>
              </template>
            </el-table-column>
          </el-table>
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
        </div>

      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data(){
    return{
      addpaperId:this.$route.query.paperId,
      addcourseId:this.$route.query.courseId,
      questionList: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      multipleSelection:[],
      name: "",
      type: "",
      dialogAddQuestion:false,
      courseId: "",
      paperId:'',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      courses:[],
    }
  },
  created() {
    this.getPaperQuestion()
    this.load()
    this.request.get("/course").then(res => {
      this.courses=res.data
    })
    this.getPaperId()
  },
  methods:{
    getPaperQuestion(){
      const paperId = this.addpaperId
      console.log("paperId:"+paperId)
      this.request.get("/paperQuestion/get-question/"+paperId).then(res =>{
        if (res.code ==='200'){
          this.questionList = res.data
        }else {
          this.$message.error("获取题目失败")
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
    searchQuestion(){
      console.log("courseId:"+this.addcourseId)
      this.dialogAddQuestion = true
    },
    delQuestion(questionId){
      console.log("del...")
      console.log("questionId"+questionId)

      const paperId = this.addpaperId
      console.log("paperId"+paperId)
       this.request.delete("paperQuestion/"+paperId+"/"+questionId).then(res =>{
            if (res.code ==='200'){
              this.$message.success("删除成功")
              this.getPaperQuestion()
            }else {
              this.$message.error("删除失败")
            }
       })
    },
    getPaperId(){
      if (this.$store.state.paperId !== ''){
        console.log(this.$store.state.paperId)
        this.paperId = this.$store.state.paperId
      }
      console.log("list:"+this.paperId)
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
    load() {
      this.request.get("/question/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          type: this.type,
          courseId: this.addcourseId,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.name = ""
      this.type = ''
      this.addcourseId = ''
      this.load()
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    addQuestion() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要添加的数据")
        return
      }
      //组合paperQuestion对象
      let param =[]
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      ids.forEach(questionId =>{
        param.push({"paperId":this.addpaperId,"questionId":questionId})
      })
      let list = JSON.stringify(param)
      console.log(list)
      this.request.post("/paperQuestion/all",list).then(res => {
        if (res.code === '200') {
          this.$message.success("添加成功")
          this.getPaperQuestion()
          this.dialogAddQuestion = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    closeAddQuestion(){
      this.$refs.multipleTable.clearSelection();
      console.log("close")
      this.multipleSelection =[]
      console.log("数组="+this.multipleSelection)
    },
    backPaper(){
      this.$router.push('/paper')
    }
}
}
</script>

<style scoped>

</style>