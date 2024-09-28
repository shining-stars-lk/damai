import {createApp,ref,provide} from 'vue'
import Cookies from 'js-cookie'
import App from './App.vue'
import  router from './router/router'
import '@/assets/styles/index.scss' // global css
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import {createPinia} from "pinia"
import locale from 'element-plus/lib/locale/lang/zh-cn' // 中文语言
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './permission'

// 分页组件
import Pagination from '@/components/Pagination'

const app = createApp(App)

app.component('Pagination', Pagination)


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}




app.use(router)
app.use(createPinia())
app.use(ElementPlus, {
    locale: locale,
    // 支持 large、default、small
    size: Cookies.get('size') || 'default'
})
app.mount('#app')

