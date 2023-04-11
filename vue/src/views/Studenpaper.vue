<template xmlns="http://www.w3.org/1999/html">
  <div>
    <div style="margin: 10px 0">
      <el-select v-model="examId" placeholder="请选择考试" >
        <el-option
            v-for="item in exam"
            :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="studentId" placeholder="请选择学生" >
        <el-option
            v-for="item in student"
            :key="item.id" :label="item.username" :value="item.id">
        </el-option>
      </el-select>
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <!-- <el-upload action="http://localhost:9090/studentPaper/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="examId" label="考试">
          <template v-slot="scope">
            <span>{{exam.find(v => v.id === scope.row.examId).name}}</span>
          </template>
      </el-table-column>
      <el-table-column prop="studentName" label="学生">
      </el-table-column>
      <el-table-column prop="time" label="提交时间"></el-table-column>
      <el-table-column prop="score" label="得分"></el-table-column>
      <el-table-column prop="status" label="阅卷状态">
        <template v-slot="scope">
          <span v-if="scope.row.status === 0" >
            <el-tag type="danger">未完成</el-tag>
          </span>
          <span v-if="scope.row.status === 1">
            <el-tag type="success">已完成</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column  label="查看试卷">
        <template v-slot="scope">
          <el-button type="success"  @click="viewPaper(scope.row.id)">查看<i class="el-icon-edit"></i></el-button>
        </template>
      </el-table-column>

      <el-table-column label="操作"  width="180" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="$router.push('/handlePaper?sp='+scope.row.id)">阅卷 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
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

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="考试id">
          <el-input v-model="form.examId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="试卷内容">
          <el-input v-model="form.paper" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生id">
          <el-input v-model="form.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="提交时间">
          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="得分">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


      <el-dialog  title="试卷信息" :visible.sync="dialogFormVisible2" width="40%" :close-on-click-modal="false">
        <div id="printArea">
          <div style="text-align: center">
            <div>
              <h2 >{{paperExamName}}</h2>
              <br/>
            </div>
            姓名： {{paperUserName}}  成绩：{{score}}
            <br/>
          </div>
        <div id="see" v-if="papers !=null">
          <div v-for="(item,index) in papers " :key="papers.id" >
            <h3>
              <div v-if="index === 0 ">{{questionType(item.type)}}</div>
              <div v-if="index >0 && item.type===1 && papers[index-1].type !== item.type">选择题</div>
              <div v-if="index >0 && item.type===2 && papers[index-1].type !== item.type">填空题</div>
              <div v-if="index >0 && item.type===3 && papers[index-1].type !== item.type">问答题</div>
            </h3>
            <div >
              <div>
                {{index+1}}.{{item.name}} ({{item.score}}分)
              </div>
              <div >
                <div v-if="item.type===1" >
                  <el-radio    v-model="item['studentAnswer']" label="A">A.{{item.a}}</el-radio>
                  <el-radio   v-model="item['studentAnswer']" label="B">B.{{item.b}}</el-radio>
                  <el-radio    v-model="item['studentAnswer']" label="C">C.{{item.c}}</el-radio>
                  <el-radio  v-model="item['studentAnswer']" label="D">D.{{item.d}}</el-radio>
                </div>
                <div v-if="item.type===2">
                  <span><el-radio   v-model="item['studentAnswer']" label="是">是</el-radio></span>
                  <span><el-radio    v-model="item['studentAnswer']" label="否">否</el-radio></span>
                </div>
                <div v-if="item.type===3">
                  <span><el-input type="textarea" v-model="item['studentAnswer']"></el-input></span>
                </div>
                <div>学生得分：{{item.studentScore}}</div>
                <br/>
                <div>正确答案：{{item.answer}}</div>
                <div>解析：{{item.detial}}</div>
                <br/>
              </div>
            </div>
            </div>
        </div>
        </div>
          <el-button type="primary" style="display:block;margin:0 auto"  v-print="print">打 印</el-button>
      </el-dialog>
    </div>


</template>

<script>
export default {
  name: "studentPaper",
  data() {
    return {
      print: {
        id: 'printArea',
        popTitle: '在线考试系统', // 打印配置页上方的标题
        extraHead: '', // 最上方的头部文字，附加在head标签上的额外标签，使用逗号分割
        preview: true, // 是否启动预览模式，默认是false
        previewTitle: '预览的标题', // 打印预览的标题
        previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
        zIndex: 25000, // 预览窗口的z-index，默认是20002，最好比默认值更高
        previewBeforeOpenCallback () { console.log('正在加载预览窗口！'); console.log(this.msg, this) }, // 预览窗口打开之前的callback
        previewOpenCallback () { console.log('已经加载完预览窗口，预览打开了！') }, // 预览窗口打开时的callback
        beforeOpenCallback () { console.log('开始打印之前！') }, // 开始打印之前的callback
        openCallback () { console.log('执行打印了！') }, // 调用打印时的callback
        closeCallback () { console.log('关闭了打印工具！') }, // 关闭打印的callback(无法区分确认or取消)
        clickMounted () { console.log('点击v-print绑定的按钮了！') },
         },
      html:null,
      paperExamName:null,
      paperUserName:null,
      tableData: [],
      exam: [],
      student: [],
      papers: [],
      score : null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      examId: "",
      studentId: "",
      form: {},
      dialogFormVisible: false,
      dialogFormVisible2: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
    this.request.get("/exam").then(res=>{
        this.exam = res.data
    })
    this.request.get("/user").then(res=>{
      this.student = res.data
    })
  },
  methods: {
    questionType(type){
      if (type ===1){
        return "选择题"
      }else if (type === 2){
        return "判断题"
      }else if (type === 3) {
        return "问答题"
      }
    },
    load() {
      this.request.get("/studentPaper/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          examId: this.examId,
          studentId: this.studentId,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/studentPaper", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/studentPaper/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    viewPaper(Id){
      this.request.get("/studentPaper/"+Id).then(res => {
        if (res.code === '200') {
          this.$message.success("查看试卷成功")
          this.dialogFormVisible2 = true
          this.papers=JSON.parse(res.data.paper)
          this.score = res.data.score
          this.request.get("/exam/"+res.data.examId).then(res =>{
              this.paperExamName = res.data.name
          })
          this.request.get("/user/"+res.data.userId).then(res =>{
            this.paperUserName = res.data.username
          })

          //console.log(this.papers)
        } else {
          this.$message.error("查看试卷失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/studentPaper/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
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
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/studentPaper/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>
<style>

</style>
