<template>
  <div class="app-container">
    <ul class="ul">
      <li class="li" v-for="(dict,ind) in categoryArr">
        <div v-if="dict.name== '城市'">
          <span class="name">{{ dict.name }}：</span>
          <div class="now"><span>当前选中城市：</span><span>{{ dict.selected }}</span></div>
        </div>
        <span v-else class="name1">{{ dict.name }}：</span>
        <span class="all" @click="allIn(ind,dict)" :class="{ active: activeIndexAll === ind }">全部</span>
        <ul>
          <!--          :class="{ active: activeIndex === index }"-->
          <li v-for="(val,index) in dict.items" @click="activeClick(ind,index)" :key="index"
              :class="{active: activeIndex ==getInnerItemKey(ind, index)}">{{ val.name }}
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script setup>
import {ref} from 'vue'

let count = 0;
const isShow = ref(true)
const isActive = ref(false)
const categoryArr = ref(
    [
      {
        name: '城市',
        selected: '北京',
        items: [
          {
            name: '北京',
            active: true
          },
          {
            name: '上海',
            active: false
          },
          {
            name: '杭州',
            active: false
          },
          {
            name: '成都',
            active: false
          }
        ]
      },
      {
        name: '分 类',
        items: [
          {
            name: '演唱会',
            active: false
          },
          {
            name: '音乐会',
            active: false
          },
          {
            name: '曲苑',
            active: false
          },
          {
            name: '杂坛',
            active: false
          },
          {
            name: '话剧',
            active: false

          },
          {
            name: '歌剧',
            active: false

          },
          {
            name: '展览',
            active: false

          },
          {
            name: '休闲',
            active: false

          },
          {
            name: '舞蹈',
            active: false

          },
          {
            name: '芭蕾',
            active: false

          },
          {
            name: '体育',
            active: false

          },
          {
            name: '其他',
            active: false

          },
          {
            name: '儿童',
            active: false

          },
          {
            name: '亲子',
            active: false

          }
        ]
      },
      {
        name: '子类',
        items: [
          {
            name: 'livehouse',
            active: false
          },
          {
            name: '其他',
            active: false
          },
          {
            name: '音乐节',
            active: false
          }
        ]
      },
      {
        name: '时间',
        items: [
          {
            name: '今天',
            active: false
          },
          {
            name: '明天',
            active: false
          },
          {
            name: '本周末',
            active: false
          }, {
            name: '一个月内',
            active: false
          }
        ]
      }
    ]
)
const condition = ref([])
const activeIndexAll = ref(null);
const activeIndex = ref(null);
const activeClick = (ind,index, val, dict) => {
  activeIndexAll.value = null;
  activeIndex.value = index;
  activeIndex.value = getInnerItemKey(ind, index)
//当点击的时候只是当前点击行的内容添加颜色，其他行不添加

}


//点击全部的时候去掉其他的样式
const allIn = (index, dict) => {
  activeIndexAll.value = index;
  activeIndex.value = null
}
</script>

<style scoped lang="scss">
.app-container {
  width: 928px;

  .ul {

    .li {
      list-style: none;
      display: flex;
      flex-direction: row;
      border-bottom: 1px dotted #dfdfdf;

      div {
        width: 42%;
        height: 30px;

        .name {
          width: 80px;
          line-height: 26px;
          color: #968788;
          text-align: right;
          margin-right: 18px;
        }

        .now {
          width: 200px;
          height: 30px;
          display: inline-block;
        }
      }

      .name1 {
        width: 80px;
        line-height: 26px;
        color: #968788;
        text-align: right;
        margin-right: 18px;
        display: inline-block;
      }

      .all {
        width: 80px;
        height: 30px;
      }

      ul {
        margin: 0;
        padding: 0;
        display: flex;
        width: calc(100% - 180px);
        flex-direction: row;
        flex-wrap: wrap;

        li {
          list-style: none;
          width: 90px;
          display: inline-block;
          height: 30px;
          text-align: center;
        }
      }

    }
  }

  //.filter-container {
  //  padding: 0 24px;
  //  border: 1px solid #e9e9e9;
  //
  //  .filter-category:first-child {
  //
  //    padding: 10px 0;
  //    border-bottom: 1px dotted #dfdfdf;
  //
  //    .filter-select:first-child {
  //      display: block;
  //
  //      .city {
  //        width: 93%;
  //        height: 26px;
  //        line-height: 26px;
  //        color: #9b9b9b;
  //        margin-bottom: 6px;
  //        display: inline-block;
  //
  //        .selectCity {
  //          display: inline-block;
  //          height: 26px;
  //          padding: 0 8px;
  //          margin-left: 6px;
  //          text-align: center;
  //          line-height: 26px;
  //          color: #fff;
  //          background-color: #ed0b75;
  //        }
  //      }
  //
  //      .type {
  //        width: 80px;
  //        line-height: 26px;
  //        color: #968788;
  //        text-align: right;
  //        margin-right: 18px;
  //      }
  //
  //      .all {
  //        display: inline-block;
  //        height: 26px;
  //        line-height: 26px;
  //        padding: 0 8px;
  //        margin-right: 24px;
  //        color: #333;
  //        white-space: nowrap;
  //        cursor: pointer;
  //        padding-left: 100px;
  //      }
  //
  //      ol {
  //        width: 80%;
  //        display: inline-block;
  //        margin: 0px;
  //        padding-left: 0px;
  //
  //        li {
  //          display: inline-block;
  //          height: 26px;
  //          line-height: 26px;
  //          padding: 0 8px;
  //          margin-right: 24px;
  //          color: #333;
  //          white-space: nowrap;
  //          cursor: pointer;
  //        }
  //      }
  //    }
  //  }
  //
  //  .filter-category {
  //
  //    padding: 10px 0;
  //    border-bottom: 1px dotted #dfdfdf;
  //
  //    .filter-select {
  //      display: flex;
  //      flex-direction: row;
  //
  //      .city {
  //        width: 100%;
  //        height: 26px;
  //        line-height: 26px;
  //        //display: inline-block;
  //        color: #9b9b9b;
  //        margin-bottom: 6px;
  //        //flex-grow: 1;
  //
  //
  //        .selectCity {
  //          display: inline-block;
  //          height: 26px;
  //          padding: 0 8px;
  //          margin-left: 6px;
  //          text-align: center;
  //          line-height: 26px;
  //          color: #fff;
  //          background-color: #ed0b75;
  //        }
  //      }
  //
  //      .type {
  //        width: 80px;
  //        line-height: 26px;
  //        color: #968788;
  //        text-align: right;
  //        margin-right: 18px;
  //        //flex-grow: 1;
  //      }
  //
  //      .all {
  //        //flex-grow: 1;
  //        display: inline-block;
  //        height: 26px;
  //        line-height: 26px;
  //        padding: 0 8px;
  //        margin-right: 24px;
  //        color: #333;
  //        white-space: nowrap;
  //        cursor: pointer;
  //      }
  //
  //      ol {
  //        width: 86%;
  //        display: inline-block;
  //        //flex-grow: 1;
  //        margin: 0px;
  //        padding-left: 0px;
  //
  //        li {
  //          display: inline-block;
  //          height: 26px;
  //          line-height: 26px;
  //          padding: 0 8px;
  //          margin-right: 24px;
  //          color: #333;
  //          white-space: nowrap;
  //          cursor: pointer;
  //        }
  //      }
  //    }
  //
  //
  //  }
  //
  //}
}

.active {
  background-color: #ed0b75;
  color: #fff;
  display: inline-block;
  height: 26px;
  line-height: 26px;

  white-space: nowrap;
  cursor: pointer;
}
</style>
