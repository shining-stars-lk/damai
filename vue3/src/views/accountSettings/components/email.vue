<template>
  <Header></Header>
  <el-row>
    <el-form ref="editEmailRef" :model="editEmailForm" :rules="editEmailRules" class="login-form">
      <el-col :span="24">
        <el-form-item label="请输入邮箱:" prop="email">
          <el-input
              v-model="editEmailForm.email"
              class="input-with-select"
              type="text"
          ></el-input>
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
import {getEditEmail} from "../../../api/accountSettings";


const router = useRouter();
const userStore = useUserStore()
const editEmailForm = ref({
  email: '',
  id: getUserIdKey()
})
const editEmailRules = reactive({
      email: [{
        required: true,
        pattern: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
        message: '请输入正确的邮箱',
        trigger: ['blur', 'focus']
      }]
    }
)


function savePsd() {
  getEditEmail(editEmailForm.value).then(response => {
    if (response.code == '0') {
      ElMessage({
        message: '保存成功',
        type: 'success',
      })

      // userStore.logOut().then(() => {
      //   location.href = '../../login';
      // })

    } else {
      ElMessage({
        message: response.message,
        type: 'error',
      })
    }
  })
}
</script>

<style scoped lang="scss">
.el-row {
  width: 400px;
  height: 400px;
  margin: 100px auto 30px;
}

.btn {
  margin-left: 130px;
  background: rgba(255, 55, 29, 0.85);
  border: none;
}
</style>
