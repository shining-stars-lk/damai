<template>
  <div class="app-container">
    <Header></Header>
    <div class="main">
      <div class="login">
        <div class="main-left">
          <img :src="bg" alt="">
        </div>
        <div class="main-right">
          <el-tabs
              v-model="activeName"
              type="card"
              class="demo-tabs"
              @tab-click="handleClick"
          >
            <el-tab-pane label="密码登录" name="first">
              <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
                <div class="error-tips" v-if="isTips">
                  <WarningFilled style="width: 1em; height: 1em; margin-left: 8px;color: #ff934c"/>
                  {{ tipsContent }}</div>
                <el-input v-model="userName" placeholder="请输入手机号或邮箱" prop="userName">
                  <template #prepend>
                    <el-icon :size="30" color="#ffffff">
                      <User/>
                    </el-icon>
                  </template>
                </el-input>
                <el-input type="password" show-password v-model="loginForm.password" placeholder="请输入密码"
                          prop="password">
                  <template #prepend>
                    <el-icon :size="30" color="#ffffff">
                      <Lock/>
                    </el-icon>
                  </template>
                </el-input>
                <el-button
                    :loading="loading"
                    size="large"
                    type="primary"
                    style="width:100%;"
                    class="btn"
                    @click.prevent="handleLogin"
                >
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
                <div v-show="experienceAccountFlag != 1" style="float: right;" v-if="register" class="register">
                  <router-link class="link-type" :to="'/register'">立即注册</router-link>
                </div>
                <div v-show="experienceAccountFlag == 1" style="float: right;" v-if="register" class="experienceAccount">
                  <a class="link-type" @click="getExperienceAccount">点击获取体验账号</a>
                </div>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="短信登录" name="second">暂未开放</el-tab-pane>
            <el-tab-pane label="扫码登录" name="third">暂未开放</el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
    <Footer></Footer>
    <el-dialog
        v-model="stateOpen"
        title="体验账号"
        width="500"

    >
      <div class="wrapper">
        <h2 class="tip-text">
          {{
            `扫码二维码关注后回复: 大麦     获取体验账号`
          }}
        </h2>
        <img
            class="qrcode-image"
            :src="wechatOfficialAccount"
            alt="微信公众号"
        />
        <div class="dialog-footer">
          <el-button class="experienceAccountConfirm" @click="stateOpen = false">确定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import wechatOfficialAccount from '@/assets/section/wechatOfficialAccount.jpg'
import bg from '@/assets/section/javaup.jpg'
import Header from '@/components/header/index'
import Footer from '@/components/footer/index'
import {isPhoneNumber, isEmailAddress} from '@/utils/index'
import {ref, getCurrentInstance, inject} from 'vue'
import useUserStore from '@/store/modules/user'
import {useRouter} from 'vue-router'

//体验账号标识
const experienceAccountFlag = ref(import.meta.env.VITE_EXPERIENCE_ACCOUNT_FLAG);
//获取体验账号弹出框
const stateOpen = ref(false)
const userStore = useUserStore()
const router = useRouter();
const loading = ref(false);
const activeName = ref('first')
// 注册开关
const register = ref(true);
const isTips = ref(false)
const tipsContent = ref('')
const {proxy} = getCurrentInstance();

const userName = ref('');
const loginForm = ref({
  email: '',
  mobile: '13212345678',
  password: '123456..',
  code: '0001'//pc网站
})

const loginRules = ref({});


const handleClick = (tab, event) => {
  console.log(tab, event)
}
const handleLogin = () => {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      if (userName.value == '') {
        isTips.value = true
        tipsContent.value = '请输入邮箱或者手机号'
      } else if (loginForm.value.password == '') {
        tipsContent.value = '请输入密码'
        isTips.value = true
      }else{
        isTips.value = false
        //正则匹配(手机号还是邮箱涉及到传参)
        identifyType(userName.value)
        // 调用action的登录方法
        userStore.login(loginForm.value).then(() => {
          router.push({path: "/"});
        }).catch(() => {
          loading.value = false;
        });
      }

    }
  });
}


function identifyType(value) {
  if (isPhoneNumber(value)) {
    loginForm.value.mobile = value
    return loginForm.value.mobile;
  } else if (isEmailAddress(value)) {
    loginForm.value.email = value
    return loginForm.value.email;
  }
}

function getExperienceAccount(){
  console.log('getExperienceAccount')
  stateOpen.value = true
}

</script>

<style scoped lang="scss">
.app-container {
  width: 100%;
  height: 100%;
  position: absolute;
  background: #ffffff;


  .main {
    width: 100%;
    height: 600px;
    background: linear-gradient(to right, #17073d, #17073d, #17073d);

    .login {
      height: 600px;
      margin: 0 auto;
      width: 1150px;
      overflow: hidden;

      .main-left {
        float: left;
        margin-top: 90px;

        img {
          width: 720px;
          height: 400px;
        }
      }

      .main-right {
        margin: 90px auto 10px;
        padding: 0;
        overflow: hidden;
        float: right;
        width: 350px;
        min-height: 310px;
        background: #fff;
        text-align: center;
      }
    }

  }


}

.register a {
  display: inline-block;
  margin-left: 10px;
  font-size: 14px;
  color: #08c;
  text-decoration: none;
  font-weight: 400;
}

.experienceAccount a {
  display: inline-block;
  margin-left: 10px;
  font-size: 20px;
  color: #cc3600;
  text-decoration: none;
  font-weight: 400;
}

:deep(.demo-tabs > .el-tabs__content) {
  padding: 15px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

:deep(.el-tabs__nav-scroll) {
  width: 350px;
  display: flex;
  overflow: hidden;
}

:deep(.el-tabs__nav-scroll .el-tabs__nav) {
  width: 350px !important;
  margin: 0 auto 20px !important;
}

:deep(.el-tabs--card > .el-tabs__header .el-tabs__item.is-active) {
  border-color: rgba(255, 55, 29, 0.85);
  color: rgba(255, 55, 29, 0.85);
  background-color: #fff;
}

:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {

  width: 116px;
  text-align: center;
  line-height: 38px;
  height: 38px;
  background-color: #e7e7e7;
  border: none;
  border-top: 2px solid #ccc;
  cursor: pointer;
  color: #222;
  font-size: 16px;
}

:deep(#pane-first) {
  width: 300px;
  margin: 0 auto;
}

:deep(.el-input-group__prepend) {
  width: 42px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  color: #fff;
  position: absolute;
  left: 8px;
  bottom: 1px;
  background-color: #ccc;

}

.el-input-group--prepend {
  border: none;
  height: 42px;
  outline: none;
  font-size: 14px;
  padding-left: 50px;
  margin-bottom: 20px;
}

.btn {
  background-color: rgba(255, 55, 29, 0.85);
  background-image: -webkit-gradient(linear, left top, right top, from(rgba(255, 55, 29, 0.85)), to(rgba(255, 55, 29, 0.85)));
  background-image: linear-gradient(90deg, rgba(255, 55, 29, 0.85), rgba(255, 55, 29, 0.85));
  border-color: rgba(255, 55, 29, 0.85);
  border-radius: 3px;
  font-size: 20px;
  height: 42px;
  line-height: 42px;
  outline: none;
  color: #fff;
  width: 100%;
  cursor: pointer;
}
.error-tips{
  border: 1px solid #ff934c;
  background: #fefcee;
  margin-bottom: 16px;
  font-size: 14px;
  padding: 5px 8px;
  overflow: hidden;
  position: relative;
  z-index: 1001;
  text-align: left;
}
.wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: 100%; /* Ensure the wrapper takes full height of the dialog */
}

.qrcode-image {
  max-width: 100%;
  margin: 20px 0; /* Add some space above and below the image */
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: center; /* Center the button horizontally */
}

.experienceAccountConfirm{
  background-color: rgba(255, 55, 29, 0.85);
  background-image: -webkit-gradient(linear, left top, right top, from(rgba(255, 55, 29, 0.85)), to(rgba(255, 55, 29, 0.85)));
  background-image: linear-gradient(90deg, rgba(255, 55, 29, 0.85), rgba(255, 55, 29, 0.85));
  border-color: rgba(255, 55, 29, 0.85);
  border-radius: 3px;
  font-size: 20px;
  height: 42px;
  line-height: 42px;
  outline: none;
  color: #fff;
  width: 20%;
  cursor: pointer;
}
</style>
