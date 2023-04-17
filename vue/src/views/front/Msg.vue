<template>
 <div>
   <el-card>
       <div  v-for="(item, index) in msgList">
<!--          <div>{{item.title}}</div>-->
<!--          <div>{{item.readNum}}</div>-->
        <el-collapse v-model="activeName" @change="handleChange(item.id)" accordion>
          <template>
               <el-tag style="margin-left: 900px; margin-top: 60px">{{item.readNum == 1 ? "已读":"未读"}}</el-tag>
          </template>
          <el-collapse-item :title=item.title :name="index +''">
            <div>发送人：{{item.sendUsername}}</div>
            <div>发送时间：{{item.createTime}}</div>
             <div>内容：{{item.content}}</div>
          </el-collapse-item>
        </el-collapse>
          </div>
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
   </el-card>
 </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      msgList:[],
      pageNum: 1,
      activeName:['0'],
      pageSize:10,
      total:0

    }
  },
  created() {
     this.load()
  },
  methods:{
    handleChange(msgId){
      console.log('val',msgId)
      request.put("/msg/read/"+msgId).then(res =>{
           if (res.code ==='200'){
             console.log("已读成功")
             this.load()
           }
      })
    },
    load(){
      request.get("/msg/page",{
          params:{
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              rec: this.user.id
          }}).then(res=>{
        if (res.code ==='200'){
            console.log(res.data.records)
          this.msgList = res.data.records
            this.total = res.data.total
        }
      })
    },
      handleSizeChange(pageSize) {
          this.pageSize = pageSize
          this.load()
      },
      handleCurrentChange(pageNum) {
          this.pageNum = pageNum
          this.load()
      },
  }
}
</script>

<style scoped>

</style>