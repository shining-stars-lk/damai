<template>
  <!--个人信息-->
  <div class="container">
    <Header></Header>
    <div class="red-line"></div>
    <div class="section">
      <MenuSideBar class="sidebarMenu" activeIndex="4"></MenuSideBar>
      <div class="right-section">
        <div class="breadcrumb"><span>常用购票人管理</span></div>
        <div class="right-tab">
          <el-button class="addUser" @click="addTicketUser">新增购票人</el-button>
          <el-table :data="ticketUserListData" v-if="isShow" style="width: 100%" border>
            <el-table-column  type="index"  label="序号" width="100px"   align="center"/>
            <el-table-column prop="relName" label="姓名"    align="center"/>
            <el-table-column prop="index" label="证件类型"  align="center">
              <template #default="scope">
                {{ getIdTypeName(scope.row.idType)}}
              </template>
            </el-table-column>
            <el-table-column prop="idNumber" label="证件号"   align="center" />
            <el-table-column prop="index" label="操作"   align="center">
              <template #default="scope">
                <el-button link type="primary" icon="Delete" @click="delTicketUser(scope.row.id)">删除</el-button>
              </template>

            </el-table-column>

          </el-table>
          <div class="addTicketUserInfo"  v-if="!isShow">
            <div class="title">新增购票人信息</div>
            <div class="line"></div>
            <el-form ref="ticketRef" :model="formTicket" :rules="formTicketRules" class="ticketForm" label-width="100px" >
              <el-col :span="24">
                <el-form-item label="姓名:"  prop="relName" >
                  <el-input
                      v-model="formTicket.relName"
                      type="text"
                      placeholder="请填填写姓名"
                  > </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="证件类型:" prop="idType"  >
                  <el-select  v-model="formTicket.idType"  >
                    <el-option v-for="item in idType"
                               :value="item.value"
                               :label="item.name" >{{item.name}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="证件号码:" prop="idNumber" >
                  <el-input
                      v-model="formTicket.idNumber"
                      type="text"
                      placeholder="请填写证件号码"
                  ></el-input>
                </el-form-item>
              </el-col>
             <el-form-item>
               <el-button
                   class="save"
                   @click.prevent="saveTicket"
               >保存</el-button>
               <el-button
                   class="btn"
                   @click.prevent="closeTicket"
               >取消</el-button>
             </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
    <Footer class="foot"></Footer>
  </div>

</template>

<script setup name="TicketUser">
import MenuSideBar from '../../components/menuSidebar/index'
import Header from '../../components/header/index'
import Footer from '../../components/footer/index'
import {ref, computed, onMounted, reactive, getCurrentInstance} from 'vue'
import useUserStore from "../../store/modules/user";
import {delTicketUserApi, selectTicketUserListApi} from '@/api/accountCenter.js'
import {getIdTypeName} from '@/api/common.js'
import {ElMessage} from 'element-plus'
import { getUserIdKey} from "@/utils/auth";
import {saveTicketUser} from "@/api/buyTicketUser";


const {proxy} = getCurrentInstance();
const useUser = useUserStore()
//购票人列表入参
const ticketUserListParams = reactive({
  userId:undefined
})
const formTicket = ref({})
formTicket.value.idType = ref('1')

const formTicketRules = ref({
  relName:  [{ required: true, message: "请输入姓名", trigger: "blur" }],
  idNumber:[{ required: true, message: "请输入证件号码", trigger: "blur" }],
})
//购票人列表数据
const ticketUserListData = ref([])
const isShow = ref(true)

const idType = ref([{
  name:'身份证',
  value:'1'
},{
  name:'港澳台居民居住证',
  value:'2'
},{
  name:'港澳台居民来往内地通行证',
  value:'3'
},{
  name:'台湾居民来往内地通行证',
  value:'4'
},{
  name:'护照',
  value:'5'
},{
  name:'歪果仁永久居住证',
  value:'6'
}])
// 获取路由参数
ticketUserListParams.userId = useUser.userId
selectTicketUserList()
//购票人列表方法
function selectTicketUserList() {
  selectTicketUserListApi(ticketUserListParams).then(response => {
    ticketUserListData.value = response.data;
  })
}

function delTicketUser(ticketUserId){
  const delTicketUserParam = {'id' : ticketUserId}
  delTicketUserApi(delTicketUserParam).then(response => {
    selectTicketUserList()
  })
}
//新增购票人
function addTicketUser(){
  isShow.value =false
  reset()
}



//保存
function saveTicket(){
  proxy.$refs.ticketRef.validate(valid => {
    if (valid) {
      formTicket.value.userId=getUserIdKey()
      saveTicketUser(formTicket.value).then(response=>{
        if(response.code==0){
          isShow.value = true
          selectTicketUserList()
          reset()
        }
      })


    }
  });
}
//取消
function closeTicket(){
  isShow.value =true
  reset()
}

function reset(){
  formTicket.value= {}
  formTicket.value.idType = ref('1')
}
</script>

<style scoped lang="scss">
.container{
  .red-line {
    border-bottom: 5px solid rgba(255, 55, 29, 0.85);
  }

  .section {
    width: 1000px;
    margin: 15px auto 0;

    .sidebarMenu {
      //width: 201px;
      float: left;
    }

    .right-section {
      width: 789px;
      float: right;

      .breadcrumb {
        border: 1px solid #efefef;
        height: 38px;
        overflow: hidden;
        background: rgba(255, 55, 29, 0.85) repeat-x;
        padding: 0 15px;
        line-height: 38px;
        color: #ffffff;
        margin-bottom: 15px;
      }

      .right-tab {
        margin-top: 23px;
          .addUser{
            margin: 0px 0px 10px 86%;
            background-color: rgba(255, 55, 29, 0.85);
            color: #fff;
          }
        .addTicketUserInfo{
          border: 1px solid #ebeef5;
          .title{
            width: 136px;
            line-height: 36px;
            border-bottom: 2px solid rgba(255, 55, 29, 0.85);;
            padding-left: 15px;
            font-size: 16px;
            color: #333333;
          }
          .line{
            width: 100%;
            height:2px;
            background: #ebeef5;
            margin-bottom: 50px;
          }
          .ticketForm{
            margin-top: 2px;
            .save{
              background-color: rgba(255, 55, 29, 0.85);
              color: #fff;
            }
          }
        }
     }


    }

  }

  .foot {
    margin-top: 500px;
  }
}


:deep(.el-input__wrapper) {
  flex-grow: 0.3
}
:deep(.el-select .el-input__wrapper ) {
  flex-grow: 0.4;
  width: 340.5px !important;
}


</style>
