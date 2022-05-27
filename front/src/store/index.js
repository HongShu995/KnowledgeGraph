import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    tabList: [{ name: "首页", path: "/" }],
    userMenuList: [],
    userId: null,
    avatar: null,
    nickname: null,
    roles: null,
    collapse: false,
    node: {
      id: null,
      name: null,
      color: null,
      des: null,
      level: null,
      url: null,
      symbolSize: null,
      createTime: null,
    },
    link: {
      id: null,
      name: null,
      source: null,
      target: null,
      createTime: null
    }
  },
  mutations: {

    saveTab(state, tab) {
      if (state.tabList.findIndex(item => item.path === tab.path) == -1) {
        state.tabList.push({ name: tab.name, path: tab.path });
      }
    },

    removeTab(state, tab) {
      var index = state.tabList.findIndex(item => item.name === tab.name);
      state.tabList.splice(index, 1);
    },

    resetTab(state) {
      state.tabList = [{ name: "首页", path: "/" }];
    },

    trigger(state) {
      state.collapse = !state.collapse;
    },

    login(state, user) {
      state.userId = user.id;
      state.avatar = user.avatar;
      state.nickname = user.nickname;
    },

    logout(state) {
      state.userId = null;
      state.avatar = null;
      state.nickname = null;
      state.roles = null;
      state.userMenuList = [];
    },

    userRoles(state, roles) {
      state.roles = roles[0]
    },

    saveListMenu(state, userMenuList) {
      state.userMenuList = userMenuList
    },

    updateUserInfo(state, user) {
      state.nickname = user.nickname;
    },

    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },

    selectNode(state, params) {
      state.node.id = params.data.id;
      state.node.name = params.data.name;
      state.node.des = params.data.des;
      state.node.color = params.color;
      state.node.level = params.data.level;
      state.node.url = params.data.url;
      state.node.symbolSize = params.data.symbolSize;
      state.node.createTime = params.data.createTime;
      state.node.isHaveTest = params.data.isHaveTest;
    },

    selectLink(state, params) {
      state.link.id = params.data.id;
      state.link.name = params.data.name;
      state.link.source = params.data.source;
      state.link.target = params.data.target;
      state.link.createTime = params.data.createTime;
    },

    clear(state) {
      state.node.id = null;
      state.node.name = null;
      state.node.des = null;
      state.node.color = null;
      state.node.level = null;
      state.node.url = null;
      state.node.symbolSize = null;
      state.node.createTime = null;
      state.link.id = null;
      state.link.name = null;
      state.link.source = null;
      state.link.target = null;
      state.link.createTime = null;
    }



  },
  actions: {
  },
  modules: {
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
