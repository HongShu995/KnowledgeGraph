import Vue from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";
import VueAxios from 'vue-axios';

import "./assets/css/index.css";
import "./assets/css/iconfont.css";

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueAxios,axios);


router.beforeEach((to, from, next) => {
  if (to.path == "/login") {
    next();
  } else if (!store.state.userId) {
    next({ path: "/login" });
  } else {
    next();
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
