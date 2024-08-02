<template>
  <Header @updateValue="handleUpdate"></Header>
  <div class="app-container">
    <!--    轮播图-->
    <el-carousel :interval="5000" arrow="always" class="carousel-lamp">
      <el-carousel-item v-for="item in picArr" :key="item">
        <img :src="item" alt="">
      </el-carousel-item>
    </el-carousel>
    <!--    中间各类型-->
    <div class="category">
      <ul>
        <li v-for="(item,ind) in categoryArr">
          <router-link   :to="{ path: '/allType/index', query: {type:item.type,name:item.name,id:item.id} }">
            <i :class="['sprit','sprit'+(ind+1)]"></i>
            <span>{{ item.name }}</span>
          </router-link>
        </li>
      </ul>
    </div>

    <div class="diffrentType" v-for="(item,index) in  programList">
      <div>
        <div class="name">
          <span>{{ item.categoryName }}</span>
          <router-link :to="{ path: '/allType/index', query: {type:1,name:item.categoryName,id:item.categoryId} }" class="more">查看全部</router-link>
        </div>
        <div class="box">
          <div class="box-left"  >
            {{item.programListVoList[0].itemPicture}}
            <router-link :to="{ name: 'detial', params: { id: item.programListVoList[0].id }}"><img :src="item.programListVoList[0].itemPicture" alt=""></router-link>
          </div>

            <div class="box-right">
              <div class="rtLink" v-for="(dict,ind) in item.programListVoList.slice(1)">
                <router-link  :to="{ name: 'detial', params: { id: dict.id }}" >
                <img :src="dict.itemPicture" alt="">
                <div class="info">
                  <div class="img-title">{{ dict.title }}</div>
                  <div class="local">{{ dict.place }}</div>
                  <div class="showTime">{{ dict.showTime }}{{ dict.showWeekTime }}</div>
                  <div class="price">{{ dict.minPrice }} <span class="rise">起</span></div>
                </div>
                </router-link>
              </div>
            </div>

        </div>

      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script setup>
import Header from '@/components/header/index'
import swiperPic1 from '@/assets/section/javaup.png'
import concert from '@/assets/section/concert.jpg'
import small from '@/assets/section/small.jpg'
import {onMounted, ref} from 'vue'
import Footer from '@/components/footer/index'
import {getcategoryType, getMainCategory} from '@/api/index'
//轮播图目前固定一张
const picArr = [swiperPic1]

const categoryArr = ref([])
const programList = ref([])
const queryParams = ref({
  "areaId": 0,
  "parentProgramCategoryIds": []
})
const programListVoList = ref([])
//     [
//   {name: '演唱会', setClass: 'sprit1', url: '/allType/index'},
//   {name: '话剧歌剧', setClass: 'sprit2', url: '/allType/index'},
//   {name: '体育', setClass: 'sprit3', url: '/allType/index'},
//   {name: '儿童亲子', setClass: 'sprit4', url: '/allType/index'},
//   {name: '展览休闲', setClass: 'sprit5', url: '/allType/index'},
//   {name: '音乐会', setClass: 'sprit6', url: '/allType/index'},
//   {name: '曲苑杂坛', setClass: 'sprit7', url: '/allType/index'},
//   {name: '舞蹈芭蕾', setClass: 'sprit8', url: '/allType/index'},
//   {name: '二次元', setClass: 'sprit9', url: '/allType/index'},
//   {name: '旅游展览', setClass: 'sprit10', url: '/allType/index'}
// ]
//获取中间的类目信息
onMounted(() => {
  getgetcategoryList()

})

//查询节目类型
function getgetcategoryList() {
  getcategoryType({type: 1}).then(response => {
    categoryArr.value = response.data
    getMainCategoryList()
  })
}

function handleUpdate(value) {
  queryParams.value.areaId = value
}

function getMainCategoryList() {
  for (let i = 0; i < 4 && i < categoryArr.value.length; i++) {
    queryParams.value.parentProgramCategoryIds.push(categoryArr.value[i].id);
  }
  getMainCategory(queryParams.value).then(response => {
    programList.value = response.data
  })
}



</script>
<style scoped lang="scss">
.app-container {
  width: 1200px;
  margin: 0 auto;

  .carousel-lamp {
    width: 100%;
    img{
      width: 1200px;
      height: 300px;
    }
  }

  .category {

    margin-top: 15px !important;
    padding: 22px 0 25px 0;
    border: 1px solid #EBEBEB;
    zoom: 1;

    ul {
      list-style-type: none;
      margin: 0;
      padding: 0;
      margin-left: 40px;
      width: 1160px;
      height: 80px;

      li {
        float: left;
        display: block;
        width: 110px;
        text-align: center;

        a {
          width: 110px;
          height: 50px;
          display: block;


          span {
            width: 110px;
            height: 20px;
            display: inline-block;
            font-size: 16px;
            color: #111;
            text-align: center;

            &:hover {
              color: rgba(255, 55, 29, 0.85);
            }
          }
        }


        .sprit {
          display: block;
          width: 48px;
          height: 48px;
          margin: 0 auto;
          background: url("/src/assets/section/sprit.png") no-repeat;
          background-size: 100% auto;
        }

        .sprit1 {
          background-position: 0 0;
        }

        .sprit2 {
          background-position: 0 -64px;
        }

        .sprit3 {
          background-position: 0 -120px;
        }

        .sprit4 {
          background-position: 0 -180px;
        }

        .sprit5 {
          background-position: 0 -240px;
        }

        .sprit6 {
          background-position: 0 -297px
        }

        .sprit7 {
          background-position: 0 -360px;
        }

        .sprit8 {
          background-position: 0 -420px;
        }

        .sprit9 {
          background-position: 0 -480px;
        }

        .sprit10 {
          background-position: 0 -540px;
        }
      }
    }


  }

  .diffrentType {
    width: 1200px;
    position: relative;
    padding: 20px;
    border: 1px solid #EBEBEB;
    margin-top: 15px;
    display: flex;

    .name {
      font-size: 24px;
      display: inline-block;
      vertical-align: middle;
      margin-left: 5px;
      color: #111;
      width: 1100px;
      height: 40px;
      line-height: 40px;
      overflow: hidden;
    }

    .more {
      display: inline-block;
      vertical-align: middle;
      float: right;
      font-size: 14px;
      color: #9B9B9B;
      max-width: 100px;
      line-height: 40px;
      height: 100%;
      overflow: hidden;
      text-align: right;
    }
  }

  .box {
    margin-top: 15px;

    .box-left {
      display: inline-block;
      width: 270px;
      height: 360px;
      position: relative;
      overflow: hidden;
      border: 1px solid #efefef;

      img {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;

      }
    }

    .box-right {
      display: inline-block;
      width: 870px;
      margin-left: 15px;
      height: 360px;
      vertical-align: top;
      overflow: hidden;

      .rtLink {
        width: 273px;
        height: 160px;
        display: block;
        margin-right: 16px;
        display: inline-block;
        margin-bottom: 40px;
        color: #000;
        overflow: hidden;

        img {
          width: 118px;
          height: 158px;
          overflow: hidden;
          position: relative;
          display: inline-block;
          border: 1px solid #efefef;
        }

        .info {
          width: 138px;
          height: 100%;
          position: relative;
          padding-left: 15px;
          display: inline-block;
          vertical-align: top;

          .img-title {
            line-height: 20px;
            font-size: 14px;
            color: #4A4A4A;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
          }

          .local {
            width: 100%;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            font-size: 12px;
            margin-top: 14px;
            color: #9B9B9B;
            overflow: hidden;
            word-break: break-all;
          }

          .showTime {
            width: 100%;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            font-size: 12px;
            margin-top: 14px;
            color: #9B9B9B;
            overflow: hidden;
            word-break: break-all;
          }

          .price {
            width: 138px;
            position: absolute;
            left: 15px;
            bottom: 0;
            font-size: 19px;
            color: rgba(255, 55, 29, 0.85);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            font-weight: bold;

            .rise {
              font-size: 14px;
            }
          }
        }
      }

    }
  }
}


</style>


