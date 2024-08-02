<template>
  <div class="app-container">
    <div class="pay-header">
      <div class="back"><el-icon><ArrowLeftBold /></el-icon></div>
      <div class="content"><img :src="pay" alt=""><span>支付宝付款</span></div>
    </div>
    <div class="pay-section">
      <el-button type="primary" class="payContinue" @click="continuePay">继续浏览器付款</el-button>
    </div>
  </div>
</template>

<script setup name="PayMethod">
import pay from "@/assets/section/pay.png"
import {ref,onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {getOrderDetailApi,orderPayApi} from "@/api/order.js";
//订单编号
const orderNumber = ref('')
//订单详情数据
const orderDetailData = ref('');
const route = useRoute();
const router = useRouter();
import {useMitt} from "@/utils/index";

const emitter = useMitt();


function continuePay() {
  //支付前，要调取订单详情
  if (orderDetailData.value == '' || orderDetailData.value == null) {
    getOrderDetail()
  }

  const orderPayParams = {
    'platform':3,
    'orderNumber':orderNumber.value,
    'subject':orderDetailData.value.programTitle,
    'price':orderDetailData.value.orderPrice,
    'channel':'alipay',
    'payBillType':1
  }
  orderPayApi(orderPayParams).then(response => {
    //将支付宝返回的表单字符串写在浏览器中，表单会自动触发submit提交
    document.write(response.data);

  })
}

//跳转后的接收值
onMounted(() => {
  getOrderDetail()
})
//订单详情方法
function getOrderDetail() {
  orderNumber.value = history.state.orderNumber;
  const orderDetailParams = {'orderNumber': orderNumber.value}
  //传值-订单号
  localStorage.setItem('orderNumber',orderNumber.value)
  getOrderDetailApi(orderDetailParams).then(response => {
    orderDetailData.value = response.data;
  })
}

</script>

<style scoped lang="scss">
.app-container {
  .pay-header {
    display: flex;
    -webkit-box-pack: justify;
    -webkit-justify-content: space-between;
    justify-content: space-between;
    -webkit-box-align: center;
    -webkit-align-items: center;
    flex-direction: row;
    align-items: center;
    height: 100%;
    padding: 0 55px;
    background-color: #fff;

    .back {
      width: 40px;
      .el-icon{
        font-size: 40px;
      }
    }

    .content {
      width: calc(100% - 40px);
      text-align: center;
      position: relative;
      img {
        width: 60px;
        height: 60px;
        position: absolute;
        top: 4px;
        left: 40%;
      }

      span {
        font-size: 40px;
        font-weight: 700;
        color: #333;
        height: 70px;
        display: inline-block;
        line-height: 70px;
        width: 200px;
        margin-left: 20px;
      }
    }
  }
  .pay-section{
    .payContinue{
      width: 95%;
      height: 123px;
      margin-top: 300px;
      font-size: 60px;
      margin-left: 40px;
    }
  }
}

</style>
