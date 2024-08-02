<template>
  <!--订单详情-->
  <div class="orderDetail">
    <Header></Header>
    <div class="app-container">
      <div class="orderNum" v-if="orderData[0]" >订单号: {{orderData[0].orderNumber}}</div>
      <div class="isPay" v-if="orderData[0]&&orderData[0].orderStatus == 1"><span>未支付</span><span>需付款: <span>￥{{orderData[0].orderPrice}}</span></span></div>
      <div class="isPay" v-if="orderData[0]&&orderData[0].orderStatus == 2">交易关闭<span>需付款: <span>￥{{orderData[0].orderPrice}}</span></span></div>
      <div class="isPay" v-if="orderData[0]&&orderData[0].orderStatus == 3"><span>已支付</span> <span>实付款: <span>￥{{orderData[0].orderPrice}}</span></span></div>
      <div class="isPay" v-if="orderData[0]&&orderData[0].orderStatus == 4">交易关闭<span>需付款: <span>￥{{orderData[0].orderPrice}}</span></span></div>
      <div class="program-table">
        <el-table :data="orderData" border style="width: 100%" class="tableCloumn">
          <el-table-column   label="项目信息"  width="400px" >
            <template #default="scope">
              <img :src="scope.row.programItemPicture" alt="">
              <div class="project">
                <div class="title">{{scope.row.programTitle}}</div>
                <div class="content">演出场次: {{scope.row.programShowTime}}</div>
                <div class="content">演出场馆: {{scope.row.programPlace}}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="座位信息" align="center">
            <template #default="scope" >
              <div v-for="item in scope.row.orderTicketInfoVoList">
                <span>{{item.seatInfo}}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column   label="单价"   align="center">
            <template #default="scope" >
              <div v-for="item in scope.row.orderTicketInfoVoList">
                <span>{{'￥'+item.price}}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column   label="数量"  align="center">
            <template #default="scope" >
              <div v-for="item in scope.row.orderTicketInfoVoList">
                <span>{{item.quantity}}</span>
              </div>
            </template></el-table-column>
          <el-table-column   label="优惠"  align="center">
            <template #default="scope" >
              <div v-for="item in scope.row.orderTicketInfoVoList">
                <span v-if="item.favourablePrice!=''">{{item.favourablePrice}}</span>
                <span v-else>-</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column  label="小计"  align="center"><template #default="scope" >
            <div v-for="item in scope.row.orderTicketInfoVoList">
              <span>{{item.relPrice}}</span>
            </div>
          </template></el-table-column>
        </el-table>
      </div>
      <ul v-for="item in orderData" class="orderDetialInfo">
        <li>
          <p class="title">配送信息</p>
          <div>配送方式：{{item.distributionMode}}</div>
          <div>取票方式：{{item.takeTicketMode}}</div>
          <div>收货人：{{item.userAndTicketUserInfoVo.userInfoVo.name}}</div>
          <div>手机号：{{item.userAndTicketUserInfoVo.userInfoVo.mobile}}</div>
        </li>
        <li>
          <p  class="title">订单信息</p>
          <div>订单编号：{{item.orderNumber}}</div>
          <div>创建时间：{{item.createOrderTime}}</div>
        </li>
        <li>
         <p  class="title">发票信息</p>
          <div>发票类型: 请在演出开始前，在程序上开具发票</div>
        </li>
        <li>
          <p  class="title">金额明细</p>
          <div> 商品总价: ￥{{item.orderPrice}}</div>
        </li>
        </ul>
      <div class="buyCustom" v-for="dict in orderData">
        <p  class="title">购票人</p>
        <div class="info" v-for="(ticketUserInfo,index) in dict.userAndTicketUserInfoVo.ticketUserInfoVoList" :key="index">
          <div>购票人姓名: {{ticketUserInfo.relName}}</div>
          <div>证件类型: {{getIdTypeName(ticketUserInfo.idType)}}</div>
          <div>证件号码: {{ticketUserInfo.idNumber}}</div>
        </div>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script setup name="OrderDetail">
import {ref, computed, onMounted, getCurrentInstance, nextTick, onBeforeMount} from 'vue'
import Header from '@/components/header/index'
import Footer from '@/components/footer/index'
import {useRoute} from 'vue-router'
import {getIdTypeName} from '@/api/common.js'
import {ElMessage} from "element-plus";
import {getOrderDetailApi} from '@/api/order.js'

//订单详情入参
const orderDetailParams = ref({
  orderNumber:undefined
})
//订单详情数据
const orderData = ref([])
const route = useRoute();
// 获取路由参数
orderDetailParams.value.orderNumber = route.params.orderNumber;

onMounted(()=>{
  getOrderDetail()
})
//订单详情方法
function getOrderDetail() {
  getOrderDetailApi(orderDetailParams.value).then(response => {
    orderData.value.push(response.data);
  })
}

function getOrderStatus(orderStatus){
  if (orderStatus == 1) {
    return '未支付';
  }
  if (orderStatus == 2) {
    return '交易关闭';
  }
  if (orderStatus == 3) {
    return '已支付';
  }
  if (orderStatus == 4) {
    return '交易关闭';
  }
}
</script>

<style scoped lang="scss">
.orderDetail{
  width: 100%;
  height: 100%;
  background: #ffffff;
  .app-container{
    width: 1200px;
    margin: 0 auto;
    padding: 10px 0;
    .orderNum{
      margin-top: 10px;
      font-size: 16px;
      margin-bottom: 20px;
    }
    .isPay{
      font-size: 30px;
      display: flex;
      flex-direction: row;
      margin-bottom: 20px;
      span:first-child{
      flex-grow: 0.8;
      }
      span:last-child{
        font-size: 14px;
        flex-grow: 0.2;
        span{
          font-size: 30px;
        }
      }
    }
    .program-table{
      .tableCloumn{
        img{
          width: 62px;
          height: 80px;
          float: left;
        }
        .project{
          width: 293px;
          padding-left:68px;

          .title{
            width:315px;
            color: #4a4a4a;
            margin-bottom: 4px;
            display: inline-block;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;

          }
          .content{
            color: #9b9b9b;
            margin-bottom: 2px;
          }

        }
      }

    }
    .orderDetialInfo{
      margin: 0;
      padding: 0;
      width: 100%;
      height: 180px;
      border: 1px solid #f5f7fa;
      margin-top: 30px;
      li{
        width: 25%;
        height: 100%;
        border-right: 1px solid #f5f7fa;
        list-style: none;
        float: left;
        .title{
          width: 100%;
          height: 20px;
          display: block;
          font-size: 20px;
          padding-left: 20px;
        }
        div{
          padding: 2px 2px 2px 10px;
          font-size: 14px;


        }
      }
    }
    .buyCustom{
      width: 100%;
      height: 180px;
      margin-top: 20px;
      margin-bottom: 20px;
      border: 1px solid #f5f7fa;
      .title{
        width: 100%;
        height: 20px;
        display: block;
        font-size: 20px;
        padding-left: 20px;
      }
      .info{
        width: 20%;
        height: 150px;
        margin-top: 10px;
        div{
          padding: 2px 2px 2px 10px;
          font-size: 14px;
        }
      }
    }
  }

}

</style>
