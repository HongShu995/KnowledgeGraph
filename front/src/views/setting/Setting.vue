<template>
  <el-card class="main-card">
    <div class="title">{{this.$route.name}}</div>
    <div>
      <el-tabs v-model="activeName" class="mt">
        <!-- 修改信息 -->
        <el-tab-pane label="修改信息" name="info">
          <div class="info-container">
            <el-upload class="avatar-uploader"
                       action="/api/users/avatar"
                       :show-file-list="false"
                       :on-success="updateAvatar"
            >
              <img v-if="avatar" :src="avatar" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
            <el-form
                label-width="70px"
                :model="infoForm"
                style="width:320px;margin-left:3rem"
            >
              <el-form-item label="昵称">
                <el-input v-model="infoForm.nickname" size="small" />
              </el-form-item>
              <el-button
                  @click="updateInfo"
                  type="primary"
                  size="medium"
                  style="margin-left:10.375rem"
              >
                修改
              </el-button>
            </el-form>
          </div>
        </el-tab-pane>
        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <div class="info-container">
            <el-form label-width="70px" :model="passwordForm" style="width:320px">
              <el-form-item label="旧密码">
                <el-input v-model="passwordForm.oldPassword"
                          @keyup.enter.native="updatePassword"
                          size="small"
                          show-password />
              </el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="passwordForm.newPassword"
                          @keyup.enter.native="updatePassword"
                          size="small"
                          show-password />
              </el-form-item>
              <el-form-item label="确认密码">
                <el-input v-model="passwordForm.confirmPassword"
                          @keyup.enter.native="updatePassword"
                          size="small"
                          show-password />
              </el-form-item>
                <span style="margin-left: 70px;color: #ee3838" v-show="isError">❌两个密码不一致</span>
              <el-button
                  type="primary"
                  size="medium"
                  style="margin-left:10rem;margin-top: 1rem"
                  @click="updatePassword"
              >
                修改
              </el-button>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

  </el-card>
</template>

<script>
export default {

  created() {
    this.getData();
  },

  data: function () {
    return {
      isError: false,
      activeName: "info",
      infoForm: {
        userId: null,
        nickname: ""
      },
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
    }
  },

  methods: {

    getData() {
      this.infoForm.userId = this.$store.state.userId;
      this.infoForm.nickname = this.$store.state.nickname;
    },

    updatePassword() {
      if (this.passwordForm.oldPassword.trim() == "") {
        this.$message.error("旧密码不能为空");
        return false;
      }
      if (this.passwordForm.newPassword.trim() == "") {
        this.$message.error("新密码不能为空");
        return false;
      }
      if (this.passwordForm.newPassword.length < 6) {
        this.$message.error("新密码不能少于6位");
        return false;
      }
      if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
        this.isError = true;
      } else {
        this.isError = false;
        this.axios
            .put("/api/users/updatePassword", this.passwordForm)
            .then(({ data }) => {
              if (data.flag) {
                this.passwordForm.oldPassword = "";
                this.passwordForm.newPassword = "";
                this.passwordForm.confirmPassword = "";
                this.$message.success(data.message);
              } else {
                this.$message.error(data.message);
              }
            });
      }
    },

    updateAvatar(response) {
      if (response.flag) {
        this.$message.success(response.message);
        this.$store.commit("updateAvatar", response.data.path);
      } else {
        this.$message.error(response.message);
      }
    },

    updateInfo() {
      if (this.infoForm.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      this.axios.put("/api/users/info", this.infoForm).then(({ data }) => {
        if (data.flag) {
          this.$message.success(data.message);
          this.$store.commit("updateUserInfo", this.infoForm);
        } else {
          this.$message.error(data.message);
        }
      });
    },

  },

  computed: {
    avatar() {
      return this.$store.state.avatar;
    }
  }
}
</script>

<style scoped>
.mt {
  margin-top: 20px;
}

.avatar-container {
  text-align: center;
}
.el-icon-message-solid {
  color: #f56c6c;
  margin-right: 0.3rem;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 10px;
  border: 3px solid #abb9c2;
  display: block;
}

.avatar:hover {
  border: 3px solid #65abea;
}

.info-container {
  display: flex;
  align-items: center;
  margin-left: 20%;
  margin-top: 5rem;
}
</style>