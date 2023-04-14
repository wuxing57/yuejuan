<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
            <!-- <el-upload action="http://localhost:9090/paper/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
              <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
            <el-table-column prop="name" label="试卷名称"></el-table-column>
            <el-table-column prop="score" label="试卷总分"></el-table-column>
            <el-table-column prop="duration" label="考试时长(分钟)"></el-table-column>
            <el-table-column  label="所属课程">
                <template v-slot="scope">
                    <span>{{courses.find(v => v.id===scope.row.courseId).name}}</span>
                </template>
            </el-table-column>
            <el-table-column  label="查看试卷">
                <template v-slot="scope">
                    <el-button type="success" @click="viewPaper(scope.row.id)">查看<i class="el-icon-edit"></i></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作"  width="380" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="handleAutoPaper(scope.row.id,scope.row.courseId)">自动组卷 <i class="el-icon-circle-plus-outline"></i></el-button>
                    <el-button type="primary" @click="handlePaper(scope.row.id,scope.row.courseId)">手动组卷 <i class="el-icon-circle-plus-outline"></i></el-button>
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

        <!-- 新增内容 -->
        <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
            <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
                <el-form-item label="试卷名称">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="试卷总分">
                    <el-input v-model="form.score" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="考试时长(分钟)">
                    <el-input v-model="form.duration" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="课程">
                    <el-select v-model="form.courseId" placeholder="请选择">
                        <el-option
                                v-for="item in courses"
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

        <!-- 自动组卷 -->
        <el-dialog title="信息" :visible.sync="dialogFormVisible1" width="40%" :close-on-click-modal="false">
            <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
                <el-form-item label="选择题">
                    <el-input v-model="form1.type1" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="填空题">
                    <el-input v-model="form1.type2" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="问答题">
                    <el-input v-model="form1.type3" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="generatorPaper">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 查看试卷 -->
        <el-dialog title="试卷信息" :visible.sync="dialogFormVisible2" width="50%" :close-on-click-modal="false">
            <div>
                <div v-for="(item,index) in papers " :key="item.id">
                    <div style="font-size:large;">
                        <br>
                        <el-header height="15px">
                            {{index+1}}.  {{item.name}}
                            <span v-if="item.type===1">(选择题)</span>
                            <span v-if="item.type===2">(填空题)</span>
                            <span v-if="item.type===3">(问答题)</span>
                        </el-header>
                    </div>
                    <el-main >
                        <div v-if="item.type===1" style="font-size:medium;height:25px" >
                            <span> <strong>A.</strong>{{item.a}}&nbsp;&nbsp;</span>
                            <span> <strong>B.</strong>{{item.b}}&nbsp;&nbsp;</span>
                            <span> <strong>C.</strong>{{item.c}}&nbsp;&nbsp;</span>
                            <span> <strong>D.</strong>{{item.d}}&nbsp;&nbsp;</span>
                        </div>
                        <div style="height:20px;" >答案：{{item.answer}}</div>
                        <div>解析：{{item.detial}}</div>
                    </el-main>
                    <el-divider></el-divider>
                </div>

            </div>
        </el-dialog>

    </div>
</template>

<script>
export default {
    data() {
        return {
            tableData: [],
            total: 0,
            pageNum: 1,
            pageSize: 10,
            name: "",
            form: {},
            form1: {},
            form2: {},
            dialogFormVisible: false,
            dialogFormVisible1: false,
            dialogFormVisible2: false,
            dialogFormVisible3: false,
            dialogAddQuestion: false,
            multipleSelection: [],
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
            courses:[],
            papers:[],
            questions:[],
            questionList:[],
            type: "",
            courseId:""
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            this.request.get("/paper/page", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                }
            }).then(res => {
                this.tableData = res.data.records
                this.total = res.data.total
            })
            this.request.get("/course").then(res => {
                this.courses=res.data
            })
        },
        questionType(type){
            if (type === 1) return '选择题'
            if (type === 2) return '判断题'
            if (type === 3) return '问答题'
        },
        generatorPaper(){
            this.request.post("/paper/generatorPaper", this.form1).then(res => {
                if (res.code === '200') {
                    this.$message.success("组卷成功")
                    this.dialogFormVisible1 = false
                    this.load()
                } else {
                    this.$message.error("组卷失败")
                }
            })
        },
        addQuestion(paperId){
            this.dialogAddQuestion = true
            this.$store.commit("open")
            if (this.$store.state.closeQuestionList){
                this.dialogAddQuestion = false;
                this.handlePaper(paperId)
            }
        },
        //刷新手动组卷题目
        loadQuestion(){
            this.this.getPaperQuestion(this.$store.state.paperId)
        },
        delQuestion(id){
            this.request.delete("/paperQuestion/question-id/"+id).then(res =>{
                if (res.code === '200'){
                    this.$message.success("删除成功")
                }
                this.getPaperQuestion(this.$store.state.paperId)
            })
        },
        viewPaper(paperId){
            this.request.get("/paper/view/"+paperId).then(res => {
                if (res.code === '200') {
                    this.i = 0;
                    this.$message.success("查看试卷成功")
                    this.dialogFormVisible2 = true
                    this.papers=res.data
                } else {
                    this.$message.error("查看试卷失败")
                }
            })
        },
        save() {
            this.request.post("/paper", this.form).then(res => {
                if (res.code === '200') {
                    this.$message.success("保存成功")
                    this.dialogFormVisible = false
                    this.load()
                } else {
                    this.$message.error("保存失败")
                }
            })
        },
        handleAutoPaper(paperId,courseId){
            this.form1={type1:4,type2:4,type3:2,paperId:paperId,courseId:courseId}
            this.dialogFormVisible1 = true
        },
        handlePaper(paperId,courseId){
            console.log(paperId)
            this.$router.push({name:'getPaper',query:{
                    paperId: paperId,
                    courseId: courseId,
                }})
        },
        getPaperQuestion(paperId){
            this.request.get("/paperQuestion/get-question/"+paperId).then(res =>{
                this.questionList = res.data
                console.log("questionList"+this.questionList)
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
            this.request.delete("/paper/" + id).then(res => {
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
            this.request.post("/paper/del/batch", ids).then(res => {
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
            // console.log(pageSize)
            this.pageSize = pageSize
            this.load()
        },
        handleCurrentChange(pageNum) {
            // console.log(pageNum)
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
            window.open("http://localhost:9090/paper/export")
        },
        handleExcelImportSuccess() {
            this.$message.success("导入成功")
            this.load()
        }
    }
}
</script>


<style>
.headerBg {
    background: #eee!important;
}

</style>
