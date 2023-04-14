<template>
    <div>
        {{exam.name}}
        <div>
            <div style="margin: 10px 0">
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

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
                <el-table-column prop="examId" label="考试">
                    <template v-slot="scope">
                        <span>{{exam.name}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="studentName" label="学生">
                </el-table-column>
                <el-table-column prop="time" label="提交时间"></el-table-column>
                <el-table-column prop="score" label="得分"></el-table-column>
                <el-table-column prop="claName" label="班级"></el-table-column>
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
        <div>
            <div id="main" style="width: 600px; height: 400px"></div>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";
import * as echarts from "echarts";
import {json} from "caniuse-lite/data/features";

export default {
  data() {
    return {
      examId: this.$route.query.id,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      exam:[],
      avgMap:[],
        paperExamName:null,
        paperUserName:null,
        tableData: [],
        student: [],
        papers: [],
        score : null,
        total: 0,
        pageNum: 1,
        pageSize: 10,
        studentId: "",
        form: {},
        multipleSelection: [],
    }
  },
  created() {
    this.load()
  },
  mounted() {
    this.drawChart()
  },
  methods:{
    load(){
        //
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
      request.get("/exam/"+this.examId).then(res=>{
        this.exam = res.data
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
      reset() {
          this.name = ""
          this.load()
      },
    drawChart() {
      request.get("/echarts/grade/"+this.examId).then(res =>{
          if (res.code === '200'){
            this.avgMap =JSON.parse(JSON.stringify(res.data))
            const cla = []
            this.avgMap.x.forEach(v=>{
              cla.push(v)
            })
            console.log(cla)
            const score = []
            this.avgMap.y.forEach(v=>{
              score.push(v)
            })
            console.log(score)
            // 基于准备好的dom，初始化echarts实例  这个和上面的main对应
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);
            // 指定图表的配置项和数据
            let option = {
              title:{
                text:"平均成绩"
              },
              xAxis: {
                name:'班级',
                type: 'category',
                data: cla
              },
              yAxis: {
                name: "分数",
                type: 'value'
              },
              series: [
                {
                  data: score,
                  type: 'bar'
                }
              ],
              tooltip:{
                trigger: 'axis',
              }
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
          }
      })
    },
  }
}
</script>

<style scoped>

</style>