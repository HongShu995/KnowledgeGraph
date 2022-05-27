<template>
  <div class="container">
    <div class="login">
      <h1 style="text-align: center">计算机网络学习系统</h1>
      <div class="login-title">用户登录</div>
      <!--登录表单-->
      <el-form :model="loginForm"
               status-icon
               :rules="rules"
               ref="loginForm"
               class="login-form"
      >
        <!-- 用户名输入框 -->
        <el-form-item prop="username">
          <el-input type="text"
                    v-model="loginForm.username"
                    prefix-icon="el-icon-user-solid"
                    placeholder="用户名"
          >
          </el-input>
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input
              type="password"
              v-model="loginForm.password"
              prefix-icon="el-icon-lock"
              show-password
              placeholder="密码"
          />
        </el-form-item>

        <div style="text-align:center">
          <el-form-item>
            <el-button type="primary" @click="submitForm('loginForm')">提交</el-button>
            <el-button @click="resetForm()">重置</el-button>
          </el-form-item>
        </div>
      </el-form>

    </div>
  </div>
</template>

<script>
import {generaMenu} from "@/assets/js/menu";
export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      const that = this;
      //发送登录请求
      let param = new URLSearchParams();
      param.append("username", that.loginForm.username);
      param.append("password", that.loginForm.password);
      this.axios.post("/api/login",param).then(({ data }) => {
        if (data.flag) {
          // 登录后保存用户信息
          that.$store.commit("login", data.data);
          // 加载用户菜单
          generaMenu();
          that.$message.success(data);
          that.$router.push({ path: "/" });
        } else {
          that.$message.error(data);
        }
      });
    },

    resetForm() {
      this.loginForm.username="";
      this.loginForm.password="";
    }
  }
}


</script>

<style scoped>

.container {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background: url('http://120.77.241.193:9999/images/bishe/cover.jpg') center center /
    cover no-repeat;
}

.login {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  background: #fff;
  padding: 300px 60px 300px;
  width: 350px;
}

.login-title {
  color: #303133;
  font-weight: bold;
  font-size: 1rem;
  text-align: center;
}

.login-form {
  margin-top: 1.2rem;
}

</style>
