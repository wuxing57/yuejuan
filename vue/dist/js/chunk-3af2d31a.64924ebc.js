(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3af2d31a"],{"5bc1":function(e,t,r){"use strict";r.r(t);var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("el-table",{attrs:{data:e.papers,border:"",stripe:"","header-cell-class-name":"headerBg"}},[r("el-table-column",{attrs:{type:"selection",width:"55"}}),r("el-table-column",{attrs:{prop:"id",label:"ID",width:"80",sortable:""}}),r("el-table-column",{attrs:{prop:"name",label:"题目"}}),r("el-table-column",{attrs:{prop:"type",label:"类型"},scopedSlots:e._u([{key:"default",fn:function(t){return[1===t.row.type?r("span",[e._v("选择题")]):e._e(),2===t.row.type?r("span",[e._v("判断题")]):e._e(),3===t.row.type?r("span",[e._v("问答题")]):e._e()]}}])}),r("el-table-column",{attrs:{prop:"score",label:"分数"}}),r("el-table-column",{attrs:{prop:"answer",label:"标准答案"}}),r("el-table-column",{attrs:{prop:"studentAnswer",label:"学生答案"}}),r("el-table-column",{attrs:{prop:"studentScore",label:"得分"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-input",{attrs:{type:"text"},model:{value:t.row.studentScore,callback:function(r){e.$set(t.row,"studentScore",r)},expression:"scope.row.studentScore"}})]}}])})],1),r("el-button",{on:{click:e.submitScore}},[e._v("提交")])],1)},a=[],s=(r("d3b7"),r("159b"),r("e9c4"),{name:"HandlePaper",data:function(){return{sp:this.$route.query.sp,papers:[]}},created:function(){var e=this;this.request.get("/studentPaper/"+this.sp).then((function(t){"200"===t.code&&(e.papers=JSON.parse(t.data.paper),e.papers&&e.papers.length&&e.papers.forEach((function(e){1!==e.type&&2!==e.type||(e.answer===e.studentAnswer?e.studentScore=e.score:e.studentScore=0)})))}))},methods:{submitScore:function(){var e=this,t=0;this.papers.forEach((function(e){null==e.studentScore&&(e.studentScore=0),t+=parseInt(e.studentScore)})),this.request.post("/studentPaper",{id:this.sp,score:t,paper:JSON.stringify(this.papers),status:1}).then((function(t){"200"===t.code&&e.$router.push("/studenpaper")}))}}}),u=s,o=r("2877"),p=Object(o["a"])(u,n,a,!1,null,"81a82184",null);t["default"]=p.exports},e9c4:function(e,t,r){var n=r("23e7"),a=r("da84"),s=r("d066"),u=r("2ba4"),o=r("e330"),p=r("d039"),l=a.Array,c=s("JSON","stringify"),d=o(/./.exec),i=o("".charAt),f=o("".charCodeAt),b=o("".replace),h=o(1..toString),w=/[\uD800-\uDFFF]/g,S=/^[\uD800-\uDBFF]$/,m=/^[\uDC00-\uDFFF]$/,y=function(e,t,r){var n=i(r,t-1),a=i(r,t+1);return d(S,e)&&!d(m,a)||d(m,e)&&!d(S,n)?"\\u"+h(f(e,0),16):e},v=p((function(){return'"\\udf06\\ud834"'!==c("\udf06\ud834")||'"\\udead"'!==c("\udead")}));c&&n({target:"JSON",stat:!0,forced:v},{stringify:function(e,t,r){for(var n=0,a=arguments.length,s=l(a);n<a;n++)s[n]=arguments[n];var o=u(c,null,s);return"string"==typeof o?b(o,w,y):o}})}}]);
//# sourceMappingURL=chunk-3af2d31a.64924ebc.js.map