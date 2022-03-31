<template>
  <el-card class="main-card">
    <div class="title">{{this.$route.name}}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button size="small" icon="el-icon-plus"
                 type="primary" @click="openMenuModel(null)"
      >
        新增
      </el-button>
      <el-button size="small" icon="el-icon-delete"
                 type="danger" :disabled="this.roleIdList.length == 0"
                 @click="isDelete = true"
      >
        批量删除
      </el-button>
      <!-- 条件筛选 -->
      <div style="margin-left: auto">
        <el-input size="small" style="width:200px"
                  prefix-icon="el-icon-search" placeholder="请输入角色名"
                  @keyup.enter.native="searchRoles" />
        <el-button size="small" style="margin-left:1rem"
                   icon="el-icon-search" type="primary"
                   @click="searchRoles"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="roleList" v-loading="loading"
              @selection-change="selectionChange">
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" />
      <el-table-column prop="roleName" label="角色名" align="center" />
      <el-table-column prop="roleLabel" label="权限标签" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.roleLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column  prop="isDisable" label="禁用" align="center" width="100">
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
          width="150"
          align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="scope">
          <el-button type="text" size="mini"
                     @click="openMenuModel(scope.row)"
          >
            <i class="el-icon-edit" /> 菜单权限
          </el-button>
          <el-button type="text" size="mini"
              @click="openResourceModel(scope.row)"
          >
            <i class="el-icon-folder-checked" /> 资源权限
          </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left:10px"
              @confirm="deleteRoles(scope.row.id)"
          >
            <el-button size="mini" type="text" slot="reference">
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
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
    <!-- 菜单对话框 -->
    <el-dialog :visible.sync="roleMenu" width="30%">
      <div class="dialog-title-container" slot="title" ref="roleTitle" />
      <el-form label-width="80px" size="medium" :model="roleForm">
        <el-form-item label="角色名">
          <el-input v-model="roleForm.roleName" style="width:250px" />
        </el-form-item>
        <el-form-item label="权限标签">
          <el-input v-model="roleForm.roleLabel" style="width:250px" />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
              :data="menuList"
              :default-checked-keys="roleForm.menuIdList"
              check-strictly
              show-checkbox
              node-key="id"
              ref="menuTree"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleMenu = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateRoleMenu">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 资源对话框 -->
    <el-dialog :visible.sync="roleResource" width="30%" top="9vh">
      <div class="dialog-title-container" slot="title">修改资源权限</div>
      <el-form label-width="80px" size="medium" :model="roleForm">
        <el-form-item label="角色名">
          <el-input v-model="roleForm.roleName" style="width:250px" />
        </el-form-item>
        <el-form-item label="权限标签">
          <el-input v-model="roleForm.roleLabel" style="width:250px" />
        </el-form-item>
        <el-form-item label="资源权限">
          <el-tree
              :data="resourceList"
              :default-checked-keys="roleForm.resourceIdList"
              show-checkbox
              node-key="id"
              ref="resourceTree"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleResource = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateRoleResource">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 批量删除对话框 -->
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteRoles(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {

  created() {
    this.listRoles();
  },

  data: function () {
    return {
      loading: true,
      roleMenu: false,
      roleResource: false,
      isDelete: false,
      roleList: [],
      roleIdList: [],
      resourceList: [],
      menuList: [],
      condition : {
        id:"",
        keywords: "",
        whether: null,
        count: 0,
        limit: 10,
        skip: 0
      },
      roleForm: {
        roleName: "",
        roleLabel: "",
        resourceIdList: [],
        menuIdList: []
      },
    }
  },

  methods: {

    searchRoles() {
      this.condition.skip = 0;
      this.listRoles();
    },

    sizeChange(size) {
      this.condition.limit = size;
      this.listRoles();
    },

    currentChange(current) {
      this.condition.skip = (current-1)*10;
      this.listRoles();
    },

    selectionChange(roleList) {
      this.roleIdList = [];
      roleList.forEach(item => {
        this.roleIdList.push(item.id);
      });
    },

    listRoles() {
      this.axios.get("/api/admin/roles",{params: {
          limit: this.condition.limit,
          keywords: this.condition.keywords,
          skip: this.condition.skip
        }
      }).then(({ data }) => {
        if(data.flag === false) {
          this.$message.error("用户长时间未操作，请重新登录");
        }
        this.roleList = data.data.recordList;
        this.condition.count = data.data.count;
        this.loading = false;
      });
      this.axios.get("/api/admin/role/menus").then(({ data }) => {
        this.menuList = data.data;
      });
      this.axios.get("/api/admin/role/resources").then(({ data }) => {
        this.resourceList = data.data;
      });
    },

    changeDisable(role) {
      this.axios.put("/api/admin/roles/disable",{
        id: role.id,
        whether: role.isDisable
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

    openMenuModel(role) {
      this.$nextTick(function() {
        this.$refs.menuTree.setCheckedKeys([]);
      });
      this.$refs.roleTitle.innerHTML = role ? "修改角色" : "新增角色";
      if (role != null) {
        this.roleForm = JSON.parse(JSON.stringify(role));
      } else {
        this.roleForm = {
          roleName: "",
          roleLabel: "",
          resourceIdList: [],
          menuIdList: []
        };
      }
      this.roleMenu = true;
    },

    openResourceModel(role) {
      this.$nextTick(function() {
        this.$refs.resourceTree.setCheckedKeys([]);
      });
      this.roleForm = JSON.parse(JSON.stringify(role));
      this.roleResource = true;
    },

    saveOrUpdateRoleMenu() {
      if (this.roleForm.roleName.trim() == "") {
        this.$message.error("角色名不能为空");
        return false;
      }
      if (this.roleForm.roleLabel.trim() == "") {
        this.$message.error("权限标签不能为空");
        return false;
      }
      this.roleForm.resourceIdList = null;
      this.roleForm.menuIdList = this.$refs.menuTree
          .getCheckedKeys()
          .concat(this.$refs.menuTree.getHalfCheckedKeys());
      this.axios.post("/api/admin/role", this.roleForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listRoles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.roleMenu = false;
      });
    },

    saveOrUpdateRoleResource() {
      this.roleForm.menuIdList = null;
      this.roleForm.resourceIdList = this.$refs.resourceTree.getCheckedKeys();
      this.axios.post("/api/admin/role", this.roleForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listRoles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.roleResource = false;
      });
    },

    deleteRoles(id) {
      var param = {};
      if (id == null) {
        param = { data: this.roleIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/admin/roles", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listRoles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.isDelete = false;
      });
    },

  },


  computed: {
    current() {
      var current = 1;
      current += this.condition.skip / 10;
      return current;
    }
  },
}
</script>

<style scoped>

</style>