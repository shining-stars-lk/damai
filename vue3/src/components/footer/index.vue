<template>
  <div class="footer">
    <div class="footer-sec">
      <ul class="footer-links">
<!--        <li v-for="item in footerList">{{ item }}<span class="line">|</span></li>-->
        <li ><a href="https://gitee.com/java-up-up" target="_blank">Gitee地址</a><span class="line">|</span></li>
        <li ><a href="https://github.com/shining-stars-lk/damai" target="_blank">GitHub地址</a><span class="line">|</span></li>
        <li ><a href="https://javaup.chat" target="_blank">文档地址</a><span class="line">|</span></li>
      </ul>
<!--      <div class="footer-ft">-->
<!--        <router-link to="/index" class="link">-->
<!--          <img :src="logo2" alt="">-->
<!--        </router-link>-->
<!--        <div class="code">-->
<!--          <img :src="code" alt="">-->
<!--          <span>APP下载</span>-->
<!--        </div>-->
<!--      </div>-->
      <div class="footer-rt">
<!--        <el-button-->
<!--            type="primary"-->
<!--            style="width:100%;"-->
<!--            class="elBtn"-->
<!--        ><span>在线客服</span>-->
<!--        </el-button>-->
        <div class="footer-details-info">
          <ul class="footer-links">
            <li ><a href="https://beian.miit.gov.cn/" target="_blank">京ICP备2024068560号-2</a><span class="line">|</span></li>
            <li ><a href="https://beian.mps.gov.cn/#/query/webSearch" target="_blank">京公网安备11011402054237号</a></li>
          </ul>

        </div>
<!--        <ul class="footer-details">-->
<!--          <li><a href="https://beian.miit.gov.cn/" target="_blank">京ICP备2024068560号-1</a></li>-->
<!--          <li>举报投诉邮箱：damai_tousu@member.alibaba.com</li>-->
<!--        </ul>-->
      </div>
    </div>
  </div>
</template>

<script setup>
import logo2 from '@/assets/login/logo2.png'
import code from '@/assets/login/code.png'
import {ref} from "vue";
const footerList = ref(
    [
      '帮助中心', '公司介绍', '品牌识别', '公司大事记', '协议及隐私权政策', '廉正举报', '联系合作', '招聘信息', '防骗秘籍'
    ]
)
</script>

<style scoped lang="scss">
.footer {
  background-color: #f8f8f8;
  width: 100%;
  //height: 298px;
 height: 150px;
  //position: fixed;

  .footer-sec {
    width: 1200px;
    margin: 20px auto 0 auto;

    .footer-links {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 36px 0;
      text-align: center;
      zoom: 1;
      list-style: none;
      font-size: 13px;
      color: #111;

      li {
        &:last-child {
          .line {
            display: none !important;
          }
        }

        .line {
          padding: 0 24px;


        }
      }


    }

    .footer-ft {
      zoom: 1;
      float: left;
      margin-top: 20px;
      padding-bottom: 58px;
      margin-right: 100px;

      .link {
        img {

        }
      }

      .code {

        text-align: center;
        margin-left: 40px;
        float: right;

        img {
          display: inline-block;
          width: 87px;
          height: 87px;
          margin-bottom: 8px;
        }

        span {
          font-size: 14px;
          color: #111;
          text-align: center;
          width: 87px;
          display: block;
        }

      }
    }

    .footer-rt {
      zoom: 1;
      padding-bottom: 10px;

      .elBtn {
        float: left;
        display: block;
        width: 98px !important;
        height: 30px;
        line-height: 1;
        text-align: center;
        background-color: rgba(255, 55, 29, 0.85);
        color: #fff;
        font-size: 14px;
        border-radius: 20px;
        margin-top: 20px;
        border: none;
        margin-bottom: 10px;

        span {
          display: inline-flex;
          align-items: center;
        }
      }

      .footer-details {
        list-style: none;

        li {
          width: 69%;
          height: 20px;
          float: left;
          font-size: 13px;
          color: #111;
          margin-top: 10px;
        }
      }
      .footer-details-info{
        width: 1200px;
        height: 20px;
        text-align: center;
        font-size: 14px;
      }
    }
  }

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
}
</style>
