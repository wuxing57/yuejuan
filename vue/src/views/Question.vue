<template>
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
            v-for="item in [{name:'选择题',value:1},{name:'判断题',value:2},
            {name:'问答题',value:3}, {name:'多选题',value:4}, {name:'填空题',value:5}]"
            :key="item.value" :label="item.name" :value="item.value">
        </el-option>
      </el-select>
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
      <!-- <el-upload action="http://localhost:9090/question/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="题干"></el-table-column>
      <el-table-column prop="courseName" label="所属课程"></el-table-column>
      <el-table-column prop="type" label="类型">
        <template v-slot="scope">
          <span v-if="scope.row.type===1" >选择题</span>
          <span v-if="scope.row.type===2">判断题</span>
          <span v-if="scope.row.type===3">问答题</span>
          <span v-if="scope.row.type===4">多选题</span>
          <span v-if="scope.row.type===5">填空题</span>
        </template>
      </el-table-column>
      <el-table-column prop="level" label="难度">
          <template v-slot="scope">
              <span v-if="scope.row.level===1" >简单</span>
              <span v-if="scope.row.level===2">一般</span>
              <span v-if="scope.row.level===3">中等</span>
              <span v-if="scope.row.level===4">困难</span>
              <span v-if="scope.row.level===5">很难</span>
          </template>
      </el-table-column>
      <el-table-column prop="userName" label="出题人"></el-table-column>
<!--      <el-table-column prop="detial" label="解析"></el-table-column>-->
      <el-table-column prop="time" label="出题时间"></el-table-column>
<!--      <el-table-column prop="answer" label="答案"></el-table-column>-->

      <el-table-column label="操作"  width="180" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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
        <el-form-item label="课程">
          <el-select v-model="form.courseId" placeholder="请选择">
            <el-option
                v-for="item in courses"
                :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option
                v-for="item in [{name:'选择题',value:1},{name:'判断题',value:2},{name:'问答题',value:3},
                {name:'多选题',value:4},{name:'填空题',value:5}]"
                :key="item.value" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题干">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="选项a" v-if="form.type === 1 || form.type === 4">
          <el-input v-model="form.a" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="选项b" v-if="form.type === 1 || form.type === 4">
          <el-input v-model="form.b" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="选项c" v-if="form.type === 1 || form.type === 4">
          <el-input v-model="form.c" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="选项d" v-if="form.type === 1 || form.type === 4">
          <el-input v-model="form.d" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="答案" v-if="form.type===2">
          <el-radio v-model="form.answer" label="是">是</el-radio>
          <el-radio v-model="form.answer" label="否">否</el-radio>
        </el-form-item>
        <el-form-item label="答案" v-else-if="form.type===1">
          <el-radio v-model="form.answer" label="A">A</el-radio>
          <el-radio v-model="form.answer" label="B">B</el-radio>
          <el-radio v-model="form.answer" label="C">C</el-radio>
          <el-radio v-model="form.answer" label="D">D</el-radio>
        </el-form-item>
        <el-form-item label="答案" v-else-if="form.type===3">
          <el-input v-model="form.answer" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="答案" v-else-if="form.type===4">
          <el-checkbox-group v-model="more" @change="handleCheckChange">
            <el-checkbox label="A" >A</el-checkbox>
            <el-checkbox label="B" >B</el-checkbox>
            <el-checkbox label="C" >C</el-checkbox>
            <el-checkbox label="D" >D</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="答案" v-else-if="form.type===5">
          <el-input v-model="form.answer" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分数" >
            <el-input-number v-model="form.score" @change="handleChange" :min="1" :max="20" :step="5" label="描述文字"></el-input-number>
        </el-form-item>
        <el-form-item label="解析">
          <el-input v-model="form.detial" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="难易程度" >
          <el-rate
              v-model="form.level"
              :texts="['简单','一般','中等','困难','很难']"
              show-text>
          </el-rate>
        </el-form-item>
        <el-form-item label="知识点id" v-if="form.courseId != null">
          <el-select v-model="form.knowledgeId" placeholder="请选择">
            <el-option
                v-for="item in knowledge"
                :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Question",
  data() {
    return {
      tableData: [],
      more: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      type: "",
      courseId: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      courses:[],
      handleQuestionId:[],
      knowledge:[]
    }
  },
  created() {
    this.load()
    this.request.get("/course").then(res => {
      this.courses=res.data
    })
  },
    watch:{
      'form.courseId':{
         handler(newVal, oldVal){
             const courseId =Number(newVal)
             if (newVal !== oldVal && !isNaN(courseId)){
                 console.log(newVal)
                 request.get("/knowledge/course/"+courseId).then(res =>{
                     this.knowledge = res.data
                 })
             }
         }
      }
    },
  methods: {
    load() {
      this.request.get("/question/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          type: this.type,
          courseId: this.courseId,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      //获取使用知识点
      // this.request.get("/knowledge").then(res=>{
      //     if (res.code === '200'){
      //         this.knowledge = res.data
      //     }
      // })
    },
    save() {
      if (this.form.type === 4) this.form.answer = this.more.toString()
      this.more = []
      console.log("保存的："+this.form.answer )
      this.request.post("/question", this.form).then(res => {
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
      this.form = { }
      this.more =[]
        request.get("/knowledge/"+courseId).then(res =>{
            this.knowledge = res.data
        })
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
      if (this.form.type === 4){
        this.more = this.form.answer.split(',')
      }
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
      this.request.delete("/question/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
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
      this.request.post("/question/del/batch", ids).then(res => {
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
      this.type = ''
      this.courseId = ''
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
      window.open("http://localhost:9090/question/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleChange(){

    },
    handleCheckChange(val){
      console.log("answer:"+val)
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
