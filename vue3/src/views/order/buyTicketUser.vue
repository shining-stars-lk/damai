<template>
<div class="app-container">
  <el-form ref="formTicket" :model="form" :rules="rules" class="login-form">
    <el-form-item label="姓名" prop="relName" label-width="272px" >
      <el-input
          v-model="form.relName"
          type="text"
          placeholder="请填写观演人姓名"
      ></el-input>
    </el-form-item>
    <div class="line"></div>
    <el-form-item label="证件类型" prop="idType" label-width="272px"   >
      <el-select  v-model="form.idType">
        <el-option v-for="item in idType"
                   :value="item.value"
                   :label="item.name" >{{item.name}}</el-option>
      </el-select>
    </el-form-item>
    <div class="line"></div>
    <el-form-item label="证件号码" prop="idNumber" label-width="272px"  >
      <el-input
          v-model="form.idNumber"
          type="text"
          placeholder="请填写证件号码"
      ></el-input>
    </el-form-item>
    <div class="line"></div>
    <div class="tips"><el-icon><Warning /></el-icon>点击确定表示您已阅读并同意 <span>《实名须知》</span></div>
    <div class="sure">
      <el-button  class="submit" @click="submit">确定</el-button>
    </div>
  </el-form>


</div>
</template>

<script setup name="BuyTicket">
import {getCurrentInstance, ref,onMounted} from 'vue'
import {saveTicketUser} from "@/api/buyTicketUser";
import { getUserIdKey} from "@/utils/auth";
import {useRoute, useRouter} from 'vue-router'

const route = useRoute();
const router = useRouter();

const {proxy} = getCurrentInstance();
const form=ref({})
const rules = ref({})
form.value.idType = ref('1')
const detailList = ref([])
const allPrice = ref('')
const countPrice = ref('')
const num = ref('')

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

onMounted(()=>{
  detailList.value  = JSON.parse(history.state.detailList)
  allPrice.value  = history.state.allPrice
  countPrice.value  =history.state.countPrice
  num.value  =history.state.num
})
const submit =()=>{
  proxy.$refs.formTicket.validate(valid => {
    if (valid) {
      if(form.value.relName == undefined){
        ElMessage({
          message: '请填写观演姓名',
          type: 'error',
        })
      }else if(form.value.idNumber == undefined){
        ElMessage({
          message: '请填写证件号码',
          type: 'error',
        })
      }else{
        form.value.userId=getUserIdKey()
        saveTicketUser(form.value).then(response=>{
          if(response.code==0){
            router.replace({path:'/order/index'})
          }

        })
      }

    }
  });
}














</script>

<style scoped lang="scss">
.app-container{
  .el-form{
    .el-form-item{
      width: auto;
      height: 18.1vmin;
      padding-left: 5.6vmin;
      padding-right: 5.6vmin;
      display: flex;
      -webkit-box-orient: horizontal;
      flex-direction: row;
      -webkit-box-pack: start;
      justify-content: flex-start;
      -webkit-box-align: center;
      align-items: center;

      .el-input{

      }
    }
    .line{
      width: auto;
      height: 0.27vmin;
      margin-left: 5.6vmin;
      margin-right: 5.6vmin;
      background: rgb(238, 238, 238);
    }
    .tips{
      width: auto;
      height: 10.67vmin;
      color: rgb(204, 204, 204);
      font-size: 3.2vmin;
      line-height: 5.6vmin;
      margin-left: 5.6vmin;
      margin-right: 5.6vmin;
      display: flex;
      -webkit-box-align: center;
      align-items: center;
      span{
        color: rgb(59, 153, 252);
      }
    }
    .sure{
      width: 100%;
      position: absolute;
      display: flex;
      -webkit-box-pack: center;
      justify-content: center;
      -webkit-box-align: end;
      align-items: flex-end;
      bottom: 0px;
      height:21.5vmin;
      padding-bottom: 5.9vmin;
      box-shadow: rgba(0, 0, 0, 0.04) 0px -2px 6px 0px;
      .submit{
        display: flex;
        -webkit-box-pack: center;
        justify-content: center;
        -webkit-box-align: center;
        align-items: center;
        width: 88.8vmin;
        height: 13.3vmin;
        font-size: 4.27vmin;
        color: rgb(255, 255, 255);
        border-radius: 170.667px 170.667px 170.667px 0px;
        background-image: linear-gradient(-270deg, rgb(255, 40, 105) 0%, rgb(255, 50, 153) 100%);

      }
    }
  }


}

:deep(.el-form-item--default .el-form-item__label) {

  font-size: 62px;
  color: rgb(51, 51, 51);
  font-weight: normal;

}
:deep(.el-input .el-input__wrapper .el-input__inner) {
  border: none !important; /* 移除边框 */
  box-shadow: none !important; /* 移除阴影 */
}
</style>
