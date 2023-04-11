<template>
 <div>
   <el-card>
      <div  v-for="item in msgList">
<!--          <div>{{item.title}}</div>-->
<!--          <div>{{item.readNum}}</div>-->
        <el-collapse  @change="handleChange(item.id)">
          <template>
               <el-tag style="margin-left: 900px; margin-top: 60px">{{item.readNum == 1 ? "已读":"未读"}}</el-tag>
          </template>
          <el-collapse-item :title=item.title name="1">
            <div>发送人：{{item.send}}</div>
            <div>发生时间：{{item.createTime}}</div>
             <div>内容：{{item.content}}</div>
          </el-collapse-item>
        </el-collapse>
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
      request.get("/msg/user/"+this.user.id).then(res=>{
        if (res.code ==='200'){
          this.msgList = res.data
        }
      })
    },
  }
}
</script>

<style scoped>

</style>