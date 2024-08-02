<template>
  <Header></Header>
  <el-row>
    <el-form ref="editMobileRef" :model="editMobileForm" :rules="editMobileRules" class="login-form">
      <el-col :span="24">
        <el-form-item label="请输入手机号码:" prop="mobile">
          <el-input
              v-model="editMobileForm.mobile"
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
import {getEditMobile} from "../../../api/accountSettings";


const router = useRouter();
const userStore = useUserStore()
const editMobileForm = ref({
  mobile: '',
  id: getUserIdKey()
})

const validatePhone = (rule, value, callback) => {
  const reg = /^1[3-9]\d{9}$/;
  if (!value) {
    return callback(new Error('手机号码不能为空'));
  } else if (!reg.test(value)) {
    return callback(new Error('请输入正确的手机号码'));
  } else {
    callback();
  }
};
const editMobileRules = reactive({
      mobile: [{required: true, trigger: "blur", validator: validatePhone}]
    }
)


function savePsd() {
  getEditMobile(editMobileForm.value).then(response => {
    if (response.code == '0') {
      ElMessage({
        message: '保存成功',
        type: 'success',
      })

      userStore.logOut().then(() => {
        location.href = '../../login';
      })

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
