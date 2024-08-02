<template>
  <!--个人信息-->
  <Header></Header>
  <div class="red-line"></div>
  <div class="section">
    <MenuSideBar class="sidebarMenu" activeIndex="3"></MenuSideBar>
    <div class="right-section">
      <div class="breadcrumb"><span>个人信息</span></div>
      <div class="right-tab">
        <ul class="title">
          <li class="left">基础资料</li>
<!--          <li class="right">资料完整度：<span>30%</span></li>-->
        </ul>
        <div class="box">
          <div class="info-list">
            <div class="tips-info">完善更多个人信息，有助于我们为您提供更加个性化的服务，本程序将尊重并保护您的隐私。</div>
            <el-form ref="perInfoRef" :model="perInfoForm" :rules="perInfoRules" class="perInfo-form">
              <el-form-item label-width="100px" label="昵称:" prop="name">
                <el-input v-model="perInfoForm.name"/>
              </el-form-item>
              <el-form-item label-width="100px" label="真实姓名:" prop="relName">
                <el-input v-model="perInfoForm.relName"/>
              </el-form-item>
              <el-form-item label-width="100px" label="性别:" prop="gender">
                <el-radio-group v-model="perInfoForm.gender">
                  <el-radio label="1" size="large">男</el-radio>
                  <el-radio label="2" size="large">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label-width="100px" label="身份证号:" prop="idNumber">
                <el-input v-model="perInfoForm.idNumber"/>
              </el-form-item>
              <el-button
                  size="small"
                  type="primary"
                  class="btn"
                  @click.prevent="gePersonList"
              >保存
              </el-button>

            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <Footer class="foot"></Footer>
</template>

<script setup>
import MenuSideBar from '../../components/menuSidebar/index'
import Header from '../../components/header/index'
import Footer from '../../components/footer/index'
import {ref, reactive, getCurrentInstance,nextTick,onMounted } from 'vue'
import {getPersonInfo, getPersonInfoId} from '@/api/personInfo'
import useUserStore from "../../store/modules/user";
import {ElMessage} from 'element-plus'
import {getName,getUserIdKey} from "@/utils/auth";

const {proxy} = getCurrentInstance();
const useUser = useUserStore()

const perInfoForm = reactive({
  name: '',
  relName: '',
  gender: '1',
  idNumber: '',
  id: useUser.userId.value
})
const perInfoRules = ref({
  name: [
    {required: true, trigger: "blur", message: "请输入昵称"},
  ],
  gender: [
    {required: true, trigger: "blur",},
  ],
})


function gePersonList() {
  proxy.$refs.perInfoRef.validate(valid => {
    if (valid) {
      getPersonInfo(perInfoForm).then(response => {
        if (response.code == 0) {
          ElMessage({
            message: '保存成功',
            type: 'success',
          })
        }else{
            ElMessage({
              message: response.message,
              type: 'error',
            })
        }

      })

    }
  })
}

//回显
onMounted(()=>{
  nextTick(()=>{
    getPersonInfoIdList()
  })
})


async function getPersonInfoIdList() {
  const id = getUserIdKey()
  getPersonInfoId({id: id}).then(response => {
    let {gender, id, idNumber, name, relAuthenticationStatus, relName} = response.data
    perInfoForm.name = name
    perInfoForm.relName = relName
    perInfoForm.gender = gender
    perInfoForm.idNumber = idNumber
    perInfoForm.id = id
  })
}
</script>

<style scoped lang="scss">
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

      .title {
        border-bottom: 2px solid rgba(255, 55, 29, 0.85);
        height: 29px;

        li {
          width: 104px;
          height: 29px;
          overflow: hidden;
          margin-left: 5px;

        }

        li.right {
          float: right;
          color: #333;
          font-size: 14px;
          font-family: "Microsoft YaHei";
          width: 180px;
          text-align: right;
          line-height: 20px;

          span {
            color: rgba(255, 55, 29, 0.85);
            font-size: 28px;
            font-weight: 700;
            line-height: 25px;
          }
        }

        li.left {
          background: rgba(255, 55, 29, 0.85) no-repeat scroll -139px -122px;
          display: block;
          line-height: 29px;
          text-align: center;
          font-size: 12px;
          padding: 0 2px;
          background-position: -139px -93px;
          color: #fff;
          font-weight: 700;
          float: left;
        }
      }
    }

    .box {
      border: 1px solid #eef1f3;

      .info-list {
        padding: 25px 40px;
        color: #666;

        .tips-info {
          background: #fffdee;
          border: 1px solid #edd28b;
          padding: 10px 20px;
          color: #999;
          margin-bottom: 15px;

        }

        .btn {
          margin-left: 150px;
          background: rgba(255, 55, 29, 0.85) no-repeat;
          width: 73px;
          height: 25px;
          border: 0;
          cursor: pointer;
          color: #fff;
        }
      }
    }
  }

}

.foot {
  margin-top: 500px;
}

:deep(.el-input__wrapper) {
  flex-grow: 0.3
}


</style>
