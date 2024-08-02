<template>
  <div class="app-container">
    <Header></Header>
    <div class="main">
      <div class="main-center">
        <el-row>
          <el-form ref="registerRef" :model="registerForm" :rules="registerRules">
            <el-col :span="18">
              <el-form-item label="手机号码:" prop="mobile" label-width="230px">
                <el-input v-model="registerForm.mobile" class="input-with-select" maxlength="11">
                  <template #prepend>
                    <el-select v-model="select" placeholder="Select" style="width: 145px">
                      <el-option label="中国大陆 +86" value="1"/>
                    </el-select>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="输入密码:" prop="password" label-width="230px">
                <el-input
                    v-model="registerForm.password"
                    class="input-with-select"
                    type="password"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="确认密码:" prop="confirmPassword" label-width="230px">
                <el-input
                    v-model="registerForm.confirmPassword"
                    class="input-with-select"
                    type="password"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label-width="230px" :style="chkStyle">
                <el-checkbox v-model="checkBox" @change="boxChange"/>
                <span class="chx">{{ agreeOpt }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-button
                  size="large"
                  type="primary"
                  style="width:100%;"
                  class="btn"
                  @click.prevent="handleAgreeLogin"
              >
                <span>同意并注册</span>
              </el-button>
            </el-col>
          </el-form>
        </el-row>
      </div>
    </div>
    <Verify
        mode="pop"
        :captchaType="captchaType"
        :imgSize="{width:'400px',height:'200px'}"
        ref="verify"
        @update:value="handleValueFromChild"

    ></Verify>
    <Footer></Footer>
  </div>
</template>

<script setup>
import Header from '@/components/header/index'
import Footer from '@/components/footer/index'
import Verify from '@/components/verifition/Verify'
import {ref, reactive} from 'vue'
import {isCaptcha, register} from '@/api/login'
import {getCurrentInstance} from 'vue'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import $bus from '../utils/bus'
import CryptoJS from 'crypto-js'
import {aesEncrypt} from "../components/verifition/utils/ase";

const {proxy} = getCurrentInstance();
const router = useRouter();

const code = ref('86')
const select = ref('1')
const agreeOpt = ref('我已阅读接受《大麦会员服务协议》《隐私权政策》《订票服务条款》并同意自动注册成为会员')
const checkBox = ref(false)
const chkStyle = ref({})
const registerForm = ref({
  password: '',
  confirmPassword: '',
  captchaId: '',
  mobile: ''
})
//手机号校验
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
//密码校验
const equalToPassword = (rule, value, callback) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};
const registerRules = reactive({
  mobile: [{required: true, trigger: "blur", validator: validatePhone}],
  password: [{
    required: true,
    pattern: /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$)([^\u4e00-\u9fa5\s]){6,20}$/,
    message: '6-20位英文字母、数字或者符号（除空格），且字母、数字和标点符号至少包含两种',
    trigger: ['blur', 'focus']
  }],
  confirmPassword: [
    {required: true, trigger: "blur", message: "请再次输入您的密码"},
    {required: true, validator: equalToPassword, trigger: "blur"}
  ],
});


/**检查是否需要验证码：如果true需要验证码，如果是false就不需要验证码
 * 获取验证码id，注册用
 */
function handleAgreeLogin() {
  isCaptcha().then(response => {
    let {verifyCaptcha, captchaId} = response.data
    if (verifyCaptcha == false) {
      //如果是false直接登录，如果是true验证码
      registerForm.value.captchaId = captchaId
      //此处勾选协议接口为传参，前端进行校验
      if (checkBox.value == false) {
        chkStyle.value = {color: 'red'}
      } else {
        chkStyle.value = {color: '#666666'}

        registerInfo()
      }
    } else {
      if (checkBox.value == false) {
        chkStyle.value = {color: 'red'}
      } else {
        chkStyle.value = {color: '#666666'}
        proxy.$refs.registerRef.validate(valid => {
          if (valid) {
            onShow('blockPuzzle')
          }
        })
      }

    }
  })
}

function boxChange(val) {
  if (val == true) {
    chkStyle.value = {color: '#666666'}
  }
}

function registerInfo() {
  proxy.$refs.registerRef.validate(valid => {
    if (valid) {
      //去掉86区号
      //registerForm.value.mobile = code.value + registerForm.value.mobile
      register(registerForm.value).then(response => {
        if ( response.code == '0') {
          ElMessage({
            message: '注册成功',
            type: 'success',
          })
          router.push({path: "./login"});
          reset()

        }
      }).catch(() => {

      });
    }

  })

}


function reset() {
  registerForm.value = {
    password: '',
    confirmPassword: '',
    captchaId: '',
    mobile: ''
  }
}

//认证
const verify = ref(null)
const captchaType = ref('')
//滑块为例
//
const onShow = (type) => {
  captchaType.value = type
  verify.value.show()
}
let secretKey = ref('')
let captchaVerify= ref('')


$bus.on('res', (data) => {
  captchaVerify.value = data.repData.captchaVerification
})


//此处是关闭验证码后，提示注册成功跳转到登录页面
function handleValueFromChild(value) {
  if (value == '关闭') {
    registerForm.value.captchaVerification =captchaVerify.value
    isCaptcha().then(res => {
      let {captchaId} = res.data
      registerForm.value.captchaId = captchaId
      //去掉86区号
      //registerForm.value.mobile = code.value + registerForm.value.mobile
      register(registerForm.value).then(response => {
        if (response.code == '0'&&response.data === true) {
          ElMessage({
            message: '注册成功',
            type: 'success',
          })
          router.push({path: "./login"});
          reset()
        }else{
          ElMessage({
            message:response.message,
            type: 'error',
          })
        }
      }).catch(() => {

      });
    })
  }
}
</script>

<style scoped lang="scss">
.app-container {
  width: 100%;
  height: 100%;
  position: absolute;
  background: #ffffff;

  .main {
    background-color: #f8f8f8;
    padding-top: 40px;

    .main-center {
      width: 1200px;
      height: 500px;
      background: #ffffff;
      padding-top: 40px;
      margin: 0 auto;
      position: relative;


      .el-form {
        width: 720px;
        margin: 0 auto;
        color: #666666;

        .el-input {
          height: 38px;

        }

        .el-select {
          .el-input {
            height: 38px;
          }
        }

        .chx {
          width: 260px;
          height: 37px;
          display: inline-block;
        }

        .slider {
          width: 400px; /* 调整容器大小 */
          .el-input[type='range'] {
            appearance: none;
            background-color: #ccc;
            height: 8px;
            border-radius: 5px;
            cursor: pointer;
            outline: none;
            padding: 0;
            margin: 0;
            position: relative;
            transition: background-size 0.3s ease;
          }

          .el-input[type='range']::before {
            content: '';
            display: block;
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-image: linear-gradient(to right, red, orange, yellow, green, blue, indigo, violet);
            z-index: 1;
          }

          .el-input[type='range']::after {
            content: attr(data-label);
            text-align: center;
            font-weight: bold;
            color: white;
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            left: calc((var(--percentage) / 100) * 100%);
            z-index: 2;
          }
        }


      }
    }

  }
}

:deep .el-input-group--prepend .el-input-group__prepend .el-select .el-input .el-input__wrapper {
  height: 38px !important;
}

.btn {
  background-color: rgba(255, 55, 29, 0.85);
  background-image: -webkit-gradient(linear, left top, right top, from(#ff4aae), to(rgba(255, 55, 29, 0.85)));
  background-image: linear-gradient(90deg, #ff4aae, rgba(255, 55, 29, 0.85));
  border-color: rgba(255, 55, 29, 0.85);
  border-radius: 3px;
  font-size: 20px;
  height: 42px;
  line-height: 42px;
  outline: none;
  color: #fff;
  width: 100%;
  cursor: pointer;
  margin-top: 60px;
  margin-left: 280px;
}

.el-form-item--default {
  margin-bottom: 34px;
}
</style>
