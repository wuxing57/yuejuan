(function(e){function n(n){for(var c,a,o=n[0],f=n[1],h=n[2],d=0,i=[];d<o.length;d++)a=o[d],Object.prototype.hasOwnProperty.call(u,a)&&u[a]&&i.push(u[a][0]),u[a]=0;for(c in f)Object.prototype.hasOwnProperty.call(f,c)&&(e[c]=f[c]);s&&s(n);while(i.length)i.shift()();return r.push.apply(r,h||[]),t()}function t(){for(var e,n=0;n<r.length;n++){for(var t=r[n],c=!0,a=1;a<t.length;a++){var o=t[a];0!==u[o]&&(c=!1)}c&&(r.splice(n--,1),e=f(f.s=t[0]))}return e}var c={},a={app:0},u={app:0},r=[];function o(e){return f.p+"js/"+({}[e]||e)+"."+{"chunk-01fa6ad1":"bbe78980","chunk-157fa584":"d7bb0ba5","chunk-17230fb4":"11ec9766","chunk-21960efc":"f37a05d5","chunk-255e3a4d":"59430ec8","chunk-25fb6332":"097157a8","chunk-27327d25":"2117905b","chunk-2d0dd3f2":"5b586119","chunk-2d0f04c0":"ec147a18","chunk-2d21a3d2":"71b5c636","chunk-2d22d746":"17187f34","chunk-309e31c4":"81bb44e7","chunk-34eaa8b0":"88198475","chunk-3a1b6992":"a5b984a5","chunk-3af2d31a":"64924ebc","chunk-46e1416a":"a987fdf4","chunk-46e73226":"cb44cc29","chunk-52a0db4e":"3b2b1754","chunk-5d298f30":"711a854e","chunk-5fc344fa":"f6163306","chunk-75322c28":"0162e965","chunk-7917d454":"54a3ce55","chunk-809ca502":"4fe25914","chunk-9102401c":"a9485c50","chunk-c6ce92e8":"e6d379fb","chunk-e4ea9aca":"d070485c","chunk-f04a75e0":"a0fe5076"}[e]+".js"}function f(n){if(c[n])return c[n].exports;var t=c[n]={i:n,l:!1,exports:{}};return e[n].call(t.exports,t,t.exports,f),t.l=!0,t.exports}f.e=function(e){var n=[],t={"chunk-01fa6ad1":1,"chunk-157fa584":1,"chunk-17230fb4":1,"chunk-21960efc":1,"chunk-255e3a4d":1,"chunk-25fb6332":1,"chunk-34eaa8b0":1,"chunk-3a1b6992":1,"chunk-46e1416a":1,"chunk-46e73226":1,"chunk-5d298f30":1,"chunk-5fc344fa":1,"chunk-75322c28":1,"chunk-7917d454":1,"chunk-809ca502":1,"chunk-9102401c":1,"chunk-f04a75e0":1};a[e]?n.push(a[e]):0!==a[e]&&t[e]&&n.push(a[e]=new Promise((function(n,t){for(var c="css/"+({}[e]||e)+"."+{"chunk-01fa6ad1":"7f25d60c","chunk-157fa584":"8ff8118b","chunk-17230fb4":"7f25d60c","chunk-21960efc":"ead87030","chunk-255e3a4d":"60735349","chunk-25fb6332":"7f25d60c","chunk-27327d25":"31d6cfe0","chunk-2d0dd3f2":"31d6cfe0","chunk-2d0f04c0":"31d6cfe0","chunk-2d21a3d2":"31d6cfe0","chunk-2d22d746":"31d6cfe0","chunk-309e31c4":"31d6cfe0","chunk-34eaa8b0":"7f25d60c","chunk-3a1b6992":"8ff8118b","chunk-3af2d31a":"31d6cfe0","chunk-46e1416a":"7f25d60c","chunk-46e73226":"025dfa08","chunk-52a0db4e":"31d6cfe0","chunk-5d298f30":"8ff8118b","chunk-5fc344fa":"7f25d60c","chunk-75322c28":"706e220b","chunk-7917d454":"44f75356","chunk-809ca502":"8ff8118b","chunk-9102401c":"7f25d60c","chunk-c6ce92e8":"31d6cfe0","chunk-e4ea9aca":"31d6cfe0","chunk-f04a75e0":"5f4c1967"}[e]+".css",u=f.p+c,r=document.getElementsByTagName("link"),o=0;o<r.length;o++){var h=r[o],d=h.getAttribute("data-href")||h.getAttribute("href");if("stylesheet"===h.rel&&(d===c||d===u))return n()}var i=document.getElementsByTagName("style");for(o=0;o<i.length;o++){h=i[o],d=h.getAttribute("data-href");if(d===c||d===u)return n()}var s=document.createElement("link");s.rel="stylesheet",s.type="text/css",s.onload=n,s.onerror=function(n){var c=n&&n.target&&n.target.src||u,r=new Error("Loading CSS chunk "+e+" failed.\n("+c+")");r.code="CSS_CHUNK_LOAD_FAILED",r.request=c,delete a[e],s.parentNode.removeChild(s),t(r)},s.href=u;var l=document.getElementsByTagName("head")[0];l.appendChild(s)})).then((function(){a[e]=0})));var c=u[e];if(0!==c)if(c)n.push(c[2]);else{var r=new Promise((function(n,t){c=u[e]=[n,t]}));n.push(c[2]=r);var h,d=document.createElement("script");d.charset="utf-8",d.timeout=120,f.nc&&d.setAttribute("nonce",f.nc),d.src=o(e);var i=new Error;h=function(n){d.onerror=d.onload=null,clearTimeout(s);var t=u[e];if(0!==t){if(t){var c=n&&("load"===n.type?"missing":n.type),a=n&&n.target&&n.target.src;i.message="Loading chunk "+e+" failed.\n("+c+": "+a+")",i.name="ChunkLoadError",i.type=c,i.request=a,t[1](i)}u[e]=void 0}};var s=setTimeout((function(){h({type:"timeout",target:d})}),12e4);d.onerror=d.onload=h,document.head.appendChild(d)}return Promise.all(n)},f.m=e,f.c=c,f.d=function(e,n,t){f.o(e,n)||Object.defineProperty(e,n,{enumerable:!0,get:t})},f.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},f.t=function(e,n){if(1&n&&(e=f(e)),8&n)return e;if(4&n&&"object"===typeof e&&e&&e.__esModule)return e;var t=Object.create(null);if(f.r(t),Object.defineProperty(t,"default",{enumerable:!0,value:e}),2&n&&"string"!=typeof e)for(var c in e)f.d(t,c,function(n){return e[n]}.bind(null,c));return t},f.n=function(e){var n=e&&e.__esModule?function(){return e["default"]}:function(){return e};return f.d(n,"a",n),n},f.o=function(e,n){return Object.prototype.hasOwnProperty.call(e,n)},f.p="",f.oe=function(e){throw console.error(e),e};var h=window["webpackJsonp"]=window["webpackJsonp"]||[],d=h.push.bind(h);h.push=n,h=h.slice();for(var i=0;i<h.length;i++)n(h[i]);var s=d;r.push([0,"chunk-vendors"]),t()})({0:function(e,n,t){e.exports=t("56d7")},"1a5d":function(e,n,t){var c={"./404.vue":["8cdb","chunk-f04a75e0"],"./About.vue":["f820","chunk-2d22d746"],"./Course.vue":["9f72","chunk-5fc344fa"],"./Dashbord.vue":["0619","chunk-52a0db4e"],"./Donate.vue":["8125","chunk-2d0dd3f2"],"./Exam.vue":["13b2","chunk-34eaa8b0"],"./File.vue":["9c88","chunk-2d0f04c0"],"./GetPaper.vue":["cd2d","chunk-e4ea9aca"],"./HandlePaper.vue":["5bc1","chunk-3af2d31a"],"./Home.vue":["bb51","chunk-2d21a3d2"],"./Login.vue":["a55b","chunk-255e3a4d"],"./Manage.vue":["f74b","chunk-21960efc"],"./Menu.vue":["9a0b","chunk-7917d454"],"./Paper.vue":["112a","chunk-17230fb4"],"./Password.vue":["43fe","chunk-3a1b6992"],"./Person.vue":["ce40","chunk-809ca502"],"./Question.vue":["e254","chunk-01fa6ad1"],"./Register.vue":["73cf","chunk-75322c28"],"./Role.vue":["c338","chunk-9102401c"],"./Sign.vue":["1c4f","chunk-25fb6332"],"./Studenpaper.vue":["3874","chunk-c6ce92e8"],"./User.vue":["1511","chunk-46e1416a"],"./front/Front.vue":["2bc2","chunk-46e73226"],"./front/Home.vue":["ec5a","chunk-309e31c4"],"./front/Paper.vue":["f114","chunk-27327d25"],"./front/Password.vue":["f8ea","chunk-157fa584"],"./front/Person.vue":["9820","chunk-5d298f30"]};function a(e){if(!t.o(c,e))return Promise.resolve().then((function(){var n=new Error("Cannot find module '"+e+"'");throw n.code="MODULE_NOT_FOUND",n}));var n=c[e],a=n[0];return t.e(n[1]).then((function(){return t(a)}))}a.keys=function(){return Object.keys(c)},a.id="1a5d",e.exports=a},4360:function(e,n,t){"use strict";var c=t("2b0e"),a=t("2f62"),u=t("a18c");c["default"].use(a["a"]);var r=new a["a"].Store({state:{currentPathName:"",paperId:"",closeQuestionList:!1},mutations:{setPath:function(e){e.currentPathName=localStorage.getItem("currentPathName")},logout:function(){localStorage.removeItem("user"),localStorage.removeItem("menus"),u["a"].push("/login"),Object(u["b"])()},savePaperId:function(e,n){e.paperId=n},close:function(e){e.closeQuestionList=!0},open:function(e){e.closeQuestionList=!1}}});n["a"]=r},"56d7":function(e,n,t){"use strict";t.r(n);t("e260"),t("e6cf"),t("cca6"),t("a79d");var c=t("2b0e"),a=function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("div",{attrs:{id:"app"}},[t("router-view")],1)},u=[],r=t("2877"),o={},f=Object(r["a"])(o,a,u,!1,null,null,null),h=f.exports,d=t("a18c"),i=t("4360"),s=t("5c96"),l=t.n(s),p=(t("0fae"),t("ab9e"),t("d3b7"),t("bc3a")),k=t.n(p),b=k.a.create({baseURL:"http://43.142.244.146:9090",timeout:5e3});b.interceptors.request.use((function(e){e.headers["Content-Type"]="application/json;charset=utf-8";var n=localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):null;return n&&(e.headers["token"]=n.token),e}),(function(e){return Promise.reject(e)})),b.interceptors.response.use((function(e){var n=e.data;return"blob"===e.config.responseType||("string"===typeof n&&(n=n?JSON.parse(n):n),"401"===n.code&&d["a"].push("/login")),n}),(function(e){return console.log("err"+e),Promise.reject(e)}));var m=b,v=t("7b1d"),g=t.n(v);c["default"].config.productionTip=!1,c["default"].use(l.a,{size:"mini"}).use(g.a),c["default"].prototype.request=m,new c["default"]({router:d["a"],store:i["a"],render:function(e){return e(h)}}).$mount("#app")},a18c:function(e,n,t){"use strict";t.d(n,"b",(function(){return f})),t.d(n,"c",(function(){return h}));t("d3b7"),t("3ca3"),t("ddb0"),t("159b"),t("ac1f"),t("5319"),t("b0c0"),t("d81d"),t("caad"),t("2532");var c=t("2b0e"),a=t("8c4f"),u=t("4360");c["default"].use(a["a"]);var r=[{path:"/login",name:"Login",component:function(){return t.e("chunk-255e3a4d").then(t.bind(null,"a55b"))}},{path:"/register",name:"Register",component:function(){return t.e("chunk-75322c28").then(t.bind(null,"73cf"))}},{path:"/404",name:"404",component:function(){return t.e("chunk-f04a75e0").then(t.bind(null,"8cdb"))}},{path:"/front",name:"Front",component:function(){return t.e("chunk-46e73226").then(t.bind(null,"2bc2"))},children:[{path:"home",name:"FrontHome",component:function(){return t.e("chunk-309e31c4").then(t.bind(null,"ec5a"))}},{path:"password",name:"Password",component:function(){return t.e("chunk-157fa584").then(t.bind(null,"f8ea"))}},{path:"person",name:"Person",component:function(){return t.e("chunk-5d298f30").then(t.bind(null,"9820"))}},{path:"paper",name:"paper",component:function(){return t.e("chunk-27327d25").then(t.bind(null,"f114"))}}]}],o=new a["a"]({mode:"hash",routes:r}),f=function(){o.matcher=new a["a"]({mode:"history",routes:r})},h=function(){var e=localStorage.getItem("menus");if(e){var n={path:"/",name:"Manage",component:function(){return t.e("chunk-21960efc").then(t.bind(null,"f74b"))},redirect:"/home",children:[{path:"person",name:"个人信息",component:function(){return t.e("chunk-809ca502").then(t.bind(null,"ce40"))}},{path:"password",name:"修改密码",component:function(){return t.e("chunk-3a1b6992").then(t.bind(null,"43fe"))}},{path:"handlePaper",name:"阅卷",component:function(){return t.e("chunk-3af2d31a").then(t.bind(null,"5bc1"))}},{path:"getPaper",name:"getPaper",component:function(){return t.e("chunk-e4ea9aca").then(t.bind(null,"cd2d"))}}]},c=JSON.parse(e);c.forEach((function(e){if(e.path){var c={path:e.path.replace("/",""),name:e.name,component:function(){return t("1a5d")("./"+e.pagePath+".vue")}};n.children.push(c)}else e.children.length&&e.children.forEach((function(e){if(e.path){var c={path:e.path.replace("/",""),name:e.name,component:function(){return t("1a5d")("./"+e.pagePath+".vue")}};n.children.push(c)}}))}));var a=o.getRoutes().map((function(e){return e.name}));a.includes("Manage")||o.addRoute(n)}};h(),o.beforeEach((function(e,n,t){if(localStorage.setItem("currentPathName",e.name),u["a"].commit("setPath"),e.matched.length)t();else{var c=localStorage.getItem("menus");t(c?"/404":"/login")}})),n["a"]=o},ab9e:function(e,n,t){}});
//# sourceMappingURL=app.a1060782.js.map