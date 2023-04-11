<template>
<div>

    {{exam.name}}
    <div id="main" style="width: 600px; height: 400px"></div>

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
      request.get("/exam/"+this.examId).then(res=>{
        this.exam = res.data
      })
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