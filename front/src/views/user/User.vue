<template>
  <el-card class="main-card">
    <div class="title">{{this.$route.name}}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="openModel"
      >
        新增用户
      </el-button>
      <el-button
          type="success"
          size="small"
          icon="el-icon-upload"
          @click="openExcelModel"
      >
        通过Excel导入
      </el-button>
      <div style="margin-left: auto">
        <el-input
            v-model="condition.keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入昵称或学号"
            style="width:200px"
            @keyup.enter.native="searchUsers"
        />
        <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            style="margin-left:1rem"
            @click="searchUsers"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="userList" v-loading="loading">
      <!-- 表格列 -->
      <el-table-column
          prop="linkAvatar"
          label="头像"
          align="center"
          width="100"
      >
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>

      <el-table-column
          prop="nickname"
          label="姓名"
          align="center"
          width="140"
      />

      <el-table-column
          prop="username"
          label="学号"
          align="center"
          width="160"
      />

      <el-table-column prop="roleList" label="用户角色" align="center">
        <template slot-scope="scope">
          <el-tag
              v-for="(item, index) of scope.row.roleList"
              :key="index"
              style="margin-right:4px;margin-top:4px"
          >
            {{ item.roleName }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="isDisable" label="禁用" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.isDisable"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :active-value="true"
              :inactive-value="false"
              @change="changeDisable(scope.row)"
          />
        </template>
      </el-table-column>

      <el-table-column
          prop="createTime"
          label="创建时间"
          width="130"
          align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime }}
        </template>
      </el-table-column>

      <el-table-column
          prop="lastLoginTime"
          label="上次登录时间"
          width="130"
          align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.lastLoginTime }}
        </template>
      </el-table-column>

      <!-- 列操作 -->
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button
              type="primary"
              size="mini"
              @click="openEditModel(scope.row)"
          >
            编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        class="pagination-container"
        background
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page="current"
        :page-size="condition.limit"
        :total="condition.count"
        :page-sizes="[10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
    />
    <el-dialog :visible.sync="isAdd" width="30%">
      <div class="dialog-title-container" slot="title">
        新增用户
      </div>
      <el-form label-width="120px" size="medium" :model="userInfo">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" style="width: 220px" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="userInfo.password" type="password" style="width: 220px" />
          <span style="margin-left: 20px;color: #ee3838" v-show="isError">❌两个密码不一致</span>
        </el-form-item>
        <el-form-item label="再次输入密码">
          <el-input v-model="secondPassword" type="password" style="width: 220px" />
          <span style="margin-left: 20px;color: #ee3838" v-show="isError">❌两个密码不一致</span>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="userInfo.nickname" style="width: 220px" />
        </el-form-item>
        <el-form-item label="角色权限">
          <el-checkbox-group v-model="roleIdList">
            <el-checkbox v-for="item of userRoleList" :key="item.id" :label="item.id">
              {{item.roleName}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="isAdd = false">取 消</el-button>
        <el-button type="primary" @click="addUser">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 修改对话框 -->
    <el-dialog :visible.sync="isEdit" width="30%">
      <div class="dialog-title-container" slot="title">
        修改用户
      </div>
      <el-form label-width="60px" size="medium" :model="userForm">
        <el-form-item label="用户Id:">
          {{userForm.userId}}
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="userForm.nickname" style="width: 220px" />
        </el-form-item>
        <el-form-item label="权限">
          <el-checkbox-group v-model="roleIdList">
            <el-checkbox v-for="item of userRoleList" :key="item.id" :label="item.id">
              {{item.roleName}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="isEdit = false">取 消</el-button>
        <el-button type="primary" @click="editUserRole">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isExcelAdd" width="21%">
      <div class="dialog-title-container" slot="title">
        通过Excel表格导入用户信息
      </div>
      <el-form label-width="120px" size="medium" :model="userInfo">
        <el-upload
            class="upload-demo"
            drag
            action="/api/admin/users/excel"
            multiple
            :on-success="uploadFile"
            :show-file-list="false"
        >
<!--          <el-progress v-if="ExcelFlag" type="circle" :percentage="excelUploadPercent" style="margin-top:30px;" />-->
          <div>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          </div>
          <div class="el-upload__tip tips" slot="tip">提示：只能上传Excel文件</div>
        </el-upload>
      </el-form>
      <div slot="footer">
        <el-button @click="isExcelAdd = false">取 消</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {

  created() {
    this.listUsers();
    this.listRoles();
  },

  data() {
    return {
      loading: true,
      isAdd: false,
      isEdit: false,
      isError: false,
      isExcelAdd: false,
      unifiedPassword: "",
      secondPassword: "",
      condition : {
        id:"",
        keywords: "",
        whether: null,
        count: 0,
        limit: 10,
        skip: 0
      },
      userInfo: {
        username: "",
        password: "",
        nickname: "",
        roleList: []
      },
      userForm: {
        userId: null,
        nickname: "",
        roleList: []
      },
      userList: [],
      userRoleList: [],
      roleIdList: [],
    }
  },

  methods: {

    openModel() {
      this.roleIdList = [];
      this.userInfo.username = "";
      this.userInfo.password = "";
      this.secondPassword = "";
      this.userInfo.nickname = "";
      this.userInfo.roleList = [];
      this.isAdd = true;
    },

    openExcelModel() {
      this.isExcelAdd = true;
      this.unifiedPassword = "";
      this.secondPassword = "";
    },

    searchUsers() {
      this.condition.skip = 0;
      this.listUsers();
    },

    sizeChange(size) {
      this.condition.limit = size;
      this.listUsers();
    },

    currentChange(current) {
      this.condition.skip = (current-1)*10;
      this.listUsers();
    },

    listUsers() {
      this.axios
          .get("/api/admin/users", {
            params: {
              limit: this.condition.limit,
              keywords: this.condition.keywords,
              skip: this.condition.skip
            }
          })
          .then(({ data }) => {
            this.userList = data.data.recordList;
            this.condition.count = data.data.count;
            this.loading = false;
          });
    },

    listRoles() {
      this.axios.get("/api/admin/users/role").then(({ data }) => {
        this.userRoleList = data.data;
      });
    },

    changeDisable(user) {
      this.axios.put("/api/admin/users/disable", {
        id: user.id,
        whether: user.isDisable
      }).then(({data})=>{
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          })
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },

    openEditModel(user) {
      this.roleIdList = [];
      this.userForm = JSON.parse(JSON.stringify(user));
      this.userForm.userId = user.id;
      this.userForm.roleList.forEach(item => {
        this.roleIdList.push(item.id);
      });
      this.isEdit = true;
    },

    editUserRole() {
      if (this.userForm.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      this.userForm.roleList = this.roleIdList;
      this.axios.post("/api/admin/users/setRoles",this.userForm)
      .then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: "修改成功"
          })
          this.listUsers()
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          })
        }
        this.isEdit = false;
      });

    },

    addUser() {
      if (this.userInfo.username.trim() === "") {
        this.$message.error("用户名不能为空");
        return false;
      }
      if (this.userInfo.password.trim() === "" || this.secondPassword.trim() === "") {
        this.$message.error("密码不能为空");
        return false;
      }
      if (this.userInfo.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      if (this.userInfo.password !== this.secondPassword) {
        this.isError = true;
      } else {
        this.userInfo.roleList = this.roleIdList;
        this.axios.post("/api/admin/users/add",this.userInfo)
        .then(({data}) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: "添加成功"
            })
            this.listUsers();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            })
          }
          this.isAdd = false;
        });
      }
    },

    // uploadProcess(event, file, fileList){
    //   this.ExcelFlag = true;
    //   this.excelUploadPercent = file.percentage.toFixed(0);
    // },

    uploadFile(response) {
      if (response.flag) {
        this.$message.success(response.message)
        this.listUsers();
        this.isExcelAdd = false;
      } else {
        this.$message.error(response.message)
      }
    }

  },

  computed: {

    current() {
      var current = 1;
      current += this.condition.skip/10;
      return current;
    },

  }

}
</script>

<style scoped>
.tips {
  color: #ee3838;
  font-size: 15px;
}
</style>