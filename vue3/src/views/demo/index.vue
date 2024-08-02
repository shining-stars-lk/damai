<template>
  <div class="app-container">
    <!--    <div id='app'>-->
    <template v-if='condition.length'>
      <div style="display: block">
        <span>已选中:</span>
        <span v-for='(item,index) in condition' class='active'>{{ item.name }}&nbsp;|&nbsp;</span>
      </div>
    </template>
    <div class="filter-container" v-if='category.length'>
      <!--       类型-->
      <div class='filter-category' v-for='(items,index) in category'>
        <!--        类型名称-->
        <div class='filter-select' v-if='items.name' :key="items.id">
          <!--          类型-->
          <span class="type">{{ items.name }}:</span>
          <span class="city" v-if="items.name == '城市'"> 当前选中城市：<span
              class="selectCity">{{ items.selected }}</span></span>
          <!--         全部-->
          <span class="all"><span @click='allIn(index)' > 全部</span>
          </span>
          <ol v-if='items.items.length'>
            <li v-for='(item,key) in items.items'>
              <span :class="{'active':item.active}" @click='handle(index,key)' :key='item.id'>{{ item.name }}</span>
            </li>
          </ol>
        </div>
      </div>
    </div>
    <!--    </div>-->
  </div>
</template>

<script setup>
import {ref} from 'vue'

let count = 0;
const isActive = ref(false)
const activeIndexAll = ref(null)
const category = ref(
    [
      {
        name: '城市',
        selected: '上海',
        items: [
          {
            name: '上海',
            active: true
          },
          {
            name: '北京',
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
            active: true
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
            active: true
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
            active: true
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

function handle(index, key) {
  var item = category.value[index].items;
  item.filter(function (v, i) {
    v.active = false;
    if (i == key) {
      v.active = true;
      condition.value.filter(function (v2, i) {
        if (v.name == v2.name) {
          condition.value.splice(i, 1);
          count--;
        }
      });

      condition.value[count++] = v
    }
  });

}

function remove(index) {
  var item = this.category[index].items;
  item.filter(function (v1, key) {
    v1.active = false;
    condition.value.filter(function (v2, i) {
      if (v1.name == v2.name) {
        condition.value.splice(i, 1);
        count--;
      }
    });
  });
}

function allIn(index) {
  isActive.value = true
  var item = this.category[index].items;

  item.filter(function (v, key) {
    v.active = false;
    condition.value.filter(function (v2, i) {
      if (v.name == v2.name) {
        condition.value.splice(i, 1);
        count--;
      }
    });
    //vue3废弃了set使用下面方式
    condition.value[count++] = v
  });
}


</script>

<style scoped lang="scss">
.app-container {
  width: 928px;

  .filter-container {
    padding: 0 24px;
    border: 1px solid #e9e9e9;

    .filter-category:first-child {

      padding: 10px 0;
      border-bottom: 1px dotted #dfdfdf;

      .filter-select:first-child {
        display: block;

        .city {
          width: 93%;
          height: 26px;
          line-height: 26px;
          color: #9b9b9b;
          margin-bottom: 6px;
          display: inline-block;

          .selectCity {
            display: inline-block;
            height: 26px;
            padding: 0 8px;
            margin-left: 6px;
            text-align: center;
            line-height: 26px;
            color: #fff;
            background-color: #ed0b75;
          }
        }

        .type {
          width: 80px;
          line-height: 26px;
          color: #968788;
          text-align: right;
          margin-right: 18px;
        }

        .all {
          display: inline-block;
          height: 26px;
          line-height: 26px;
          padding: 0 8px;
          margin-right: 24px;
          color: #333;
          white-space: nowrap;
          cursor: pointer;
          padding-left: 100px;
        }

        ol {
          width: 80%;
          display: inline-block;
          margin: 0px;
          padding-left: 0px;

          li {
            display: inline-block;
            height: 26px;
            line-height: 26px;
            padding: 0 8px;
            margin-right: 24px;
            color: #333;
            white-space: nowrap;
            cursor: pointer;
          }
        }
      }
    }

    .filter-category {

      padding: 10px 0;
      border-bottom: 1px dotted #dfdfdf;

      .filter-select {
        display: flex;
        flex-direction: row;

        .city {
          width: 100%;
          height: 26px;
          line-height: 26px;
          //display: inline-block;
          color: #9b9b9b;
          margin-bottom: 6px;
          //flex-grow: 1;


          .selectCity {
            display: inline-block;
            height: 26px;
            padding: 0 8px;
            margin-left: 6px;
            text-align: center;
            line-height: 26px;
            color: #fff;
            background-color: #ed0b75;
          }
        }

        .type {
          width: 80px;
          line-height: 26px;
          color: #968788;
          text-align: right;
          margin-right: 18px;
          //flex-grow: 1;
        }

        .all {
          //flex-grow: 1;
          display: inline-block;
          height: 26px;
          line-height: 26px;
          padding: 0 8px;
          margin-right: 24px;
          color: #333;
          white-space: nowrap;
          cursor: pointer;
        }

        ol {
          width: 86%;
          display: inline-block;
          //flex-grow: 1;
          margin: 0px;
          padding-left: 0px;

          li {
            display: inline-block;
            height: 26px;
            line-height: 26px;
            padding: 0 8px;
            margin-right: 24px;
            color: #333;
            white-space: nowrap;
            cursor: pointer;
          }
        }
      }


    }

  }
}
.active{
  background-color: #ed0b75;
  color: #fff;
  display: inline-block;
  height: 26px;
  line-height: 26px;

  white-space: nowrap;
  cursor: pointer;
}
</style>
