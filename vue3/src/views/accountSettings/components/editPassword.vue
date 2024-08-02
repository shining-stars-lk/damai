<template>
  <Header></Header>
  <el-row>
    <el-form ref="editPsdRef" :model="editPsdForm" :rules="editPsdRules" class="login-form">
      <el-col :span="24">
        <el-form-item label="输入密码:" prop="password" >
          <el-input
              v-model="editPsdForm.password"
              class="input-with-select"
              type="password"
              show-password
          > </el-input>
        </el-form-item>
      </el-col>
      <el-button
          size="large"
          type="primary"
          class="btn"
          @click.prevent="savePsd"
      ><span>保存</span></el-button>
    </el-form>
  </el-row>
  <Footer class="foot"></Footer>
</template>

<script setup>

import Header from '../../../components/header/index'
import Footer from '../../../components/footer/index'
import {getEditPsd} from '@/api/accountSettings'
import {ElMessage} from "element-plus"
import {getUserIdKey} from "../../../utils/auth"
import {ref, reactive} from 'vue'
import {useRouter} from 'vue-router'
import useUserStore from '@/store/modules/user'


const router = useRouter();
const userStore = useUserStore()
const editPsdForm = ref({
  password: '',
  id: getUserIdKey()
})
const editPsdRules = reactive([
  {
    required: true,
    pattern: /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$)([^\u4e00-\u9fa5\s]){6,20}$/,
    message: '6-20位英文字母、数字或者符号（除空格），且字母、数字和标点符号至少包含两种',
    trigger: ['blur', 'focus']
  }
])


function savePsd() {
  getEditPsd(editPsdForm.value).then(response=>{
    if(response.code == '0'){
      ElMessage({
        message: '保存成功',
        type: 'success',
      })

      userStore.logOut().then(() => {
        location.href = '../../login';
      })

    }else{
      ElMessage({
        message: response.message,
        type: 'error',
      })
    }
  })
}
</script>

<style scoped lang="scss">
.el-row{
  width: 400px;
  height: 400px;
  margin: 100px auto 30px;
}
.btn{
  margin-left: 130px;
  background: rgba(255, 55, 29, 0.85);
  border: none;
}
</style>
