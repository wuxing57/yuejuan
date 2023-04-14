<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{countList["用户总数"]}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> 试卷总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
              {{countList["试卷总量"]}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" /> 题库数量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
              {{countList["题库数量"]}}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" /> 班级总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
              {{countList["班级总数"]}}
          </div>
        </el-card>
      </el-col>
    </el-row>


<!--    <el-row>-->
<!--      <el-col :span="12">-->
<!--        <div id="line" style="width: 500px; height: 450px"></div>-->
<!--      </el-col>-->
<!--      <el-col :span="12">-->
<!--        <div id="pie" style="width: 500px; height: 400px"></div>-->
<!--      </el-col>-->
<!--    </el-row>-->
      <div id="line" style="width: 1200px;height:400px;"></div>


  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from "@/utils/request";

export default {
    name: "Home",
    data() {
        return {
            countList: [],
            line: [],

        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            request.get("/echarts/count").then(res => {
                if (res.code == 200) {
                    this.countList = res.data
                    //  console.log(res.data["题库数量"])
                }
            })
        },
        drawLine() {
            request.get("/echarts/line").then(res => {
                if (res.code === '200') {
                    this.line = JSON.parse(JSON.stringify(res.data))
                    const x = []
                    this.line.x.forEach(v => {
                        x.push(v)
                    })
                   // console.log(x)
                    const y = []
                    this.line.y.forEach(v => {
                        y.push(v)
                    })
                    //console.log(y)
                    // 基于准备好的dom，初始化echarts实例  这个和上面的main对应
                    var chartDom = document.getElementById('line');
                    var myChart = echarts.init(chartDom);
                    // 指定图表的配置项和数据
                    let option = {
                        title:{
                            text:'每日题目数量折线图'
                        },
                        tooltip:{
                             trigger:'item',
                            // triggerOn:'click',
                             formatter: (arg)=>{
                                 return arg.name+"号上传题目数量" +":"+arg.data
                             }
                        },
                        xAxis: {
                            name:'日',
                            type: 'category',
                            data: x
                        },
                        yAxis: {
                            name:'题目数量',
                            type: 'value'
                        },
                        series: [
                            {
                                data: y,
                                type: 'line'
                            }
                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })
        },
    },

    mounted() {
       this.drawLine()
    }
}
</script>

<style scoped>

</style>
