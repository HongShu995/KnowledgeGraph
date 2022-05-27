<template>
  <el-card>
    <div class="title">{{this.$route.name}}</div>
    <div class="operation-container">
      <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="openModel(null)"
      >
        新增测试题
      </el-button>

      <div style="margin-left: auto">
        <el-input v-model="condition.keywords"
                  prefix-icon="el-icon-search"
                  size="small"
                  placeholder="请输入试题名称"
                  style="width:200px"
        />
        <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            style="margin-left:1rem"
            @click="refresh"
        >
          搜索
        </el-button>
      </div>
    </div>

    <!--表格区域-->
    <div>
      <el-tabs v-model="activeName" class="mt">
        <el-tab-pane label="选择题" name="select">

          <el-table
              border
              :data="this.choiceData"
              style="width: 100%">
            <el-table-column
                prop="questionName"
                label="题目"
            />

            <el-table-column
                prop="optionList"
                label="所有选项">
              <template slot-scope="scope">
                <el-select :value="getOptionSelectValue(scope.row)">
                  <el-option
                      v-for="item in scope.row.optionList"
                      :key="item"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>

            <el-table-column
                prop="correctAnswerList"
                label="正确答案"
            >
              <template slot-scope="scope">
                <el-tag
                    v-for="(item, index) of scope.row.correctAnswerList"
                    :key="index"
                    style="margin-right:4px;margin-top:4px"
                >
                  {{ item }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
                prop="createTime"
                label="创建时间"
                width="100"
                align="center"
            />

            <el-table-column
                prop="id"
                label="操作"
                width="60"
                align="center"
            >
              <template slot-scope="scope">
                <el-button type="text" @click="openModel(scope.row)" size="small">修改</el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="判断题" name="judgment">
          <el-table
              border
              :data="this.estimateData"
              style="width: 100%"
          >
            <el-table-column
                prop="questionName"
                label="题目"
            />

            <el-table-column
                prop="judgmentAnswer"
                label="正确答案"
                width="80"
                align="center"
            >
              <template slot-scope="scope">
                <el-tag v-if="scope.row.judgmentAnswer" type="success">
                  正确
                </el-tag>
                <el-tag v-if="!scope.row.judgmentAnswer" type="danger">
                  错误
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
                prop="createTime"
                label="创建时间"
                width="100"
                align="center"
            />

            <el-table-column
                prop="id"
                label="操作"
                width="60"
                align="center"
            >
              <template slot-scope="scope">
                <el-button type="text" @click="openModel(scope.row)" size="small">修改</el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>

        <el-tab-pane label="填空题" name="completion">
          <el-table
              border
              :data="this.fillData"
              style="width: 100%"
          >
            <el-table-column
                prop="questionName"
                label="题目"
            />

            <el-table-column
                prop="correctAnswerList"
                label="正确答案"
            >
              <template slot-scope="scope">
                <el-tag
                    v-for="(item, index) of scope.row.correctAnswerList"
                    :key="index"
                    style="margin-right:4px;margin-top:4px"
                >
                  {{ item }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
                prop="createTime"
                label="创建时间"
                width="100"
                align="center"
            />

            <el-table-column
                prop="id"
                label="操作"
                width="60"
                align="center"
            >
              <template slot-scope="scope">
                <el-button type="text" @click="openModel(scope.row)" size="small">修改</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="简答题" name="answer">
          <el-table
              border
              :data="this.answerData"
              style="width: 100%"
          >
            <el-table-column
                prop="questionName"
                label="题目"
            />

            <el-table-column
                prop="keywordList"
                label="关键词"
            >
              <template slot-scope="scope">
                <el-tag
                    v-for="(item, index) of scope.row.keywordList"
                    :key="index"
                    style="margin-right:4px;margin-top:4px"
                >
                  {{ item }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
                prop="correctAnswerList"
                label="正确答案"
            />

            <el-table-column
                prop="createTime"
                label="创建时间"
                width="100"
                align="center"
            />

            <el-table-column
                prop="id"
                label="操作"
                width="60"
                align="center"
            >
              <template slot-scope="scope">
                <el-button type="text" @click="openModel(scope.row)" size="small">修改</el-button>
              </template>
            </el-table-column>

          </el-table>

        </el-tab-pane>
      </el-tabs>

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
    </div>

    <!--新增或修改对话框-->
    <el-dialog
        :visible.sync="isEditQuestion"
        width="30%">
      <div class="dialog-title-container" slot="title" ref="questionTitle" />
      <el-form label-width="100px" class="demo-dynamic" :model="questionForm">

        <!--id-->
        <el-form-item v-if="questionForm.id" label="题目 ID">
          {{questionForm.id}}
        </el-form-item>

        <!--题型-->
        <el-form-item
            prop="email"
            label="题型">
          <el-select v-model="questionForm.typeCode">
            <el-option
                v-for="item in allQuestionTypeArr"
                :key="item.typeCode"
                :label="item.name"
                :value="item.typeCode">
            </el-option>
          </el-select>
        </el-form-item>

        <!--题目-->
        <el-form-item
            prop="email"
            label="题目"
        >
          <el-input
              type="textarea"
              autosize
              v-model="questionForm.questionName"
              placeholder="请输入题目内容">
          </el-input>
        </el-form-item>

        <!--分数-->
        <el-form-item
            prop="email"
            label="分数">
          <el-input v-model="questionForm.perAnswerScore" placeholder="请输入分数" type="number" />
          <div v-if="questionForm.typeCode === 3 " style="color: #ee3838">PS：填空题分数为每空分数</div>
        </el-form-item>

        <!--选择题选项-->
        <el-form-item
            v-if="questionForm.typeCode === 1"
            prop="email"
            label="选项">
          <el-tag
              :key="tag"
              v-for="tag in questionForm.optionList"
              closable
              :disable-transitions="false"
              @close="handleClose(tag,1)">
            {{tag}}
          </el-tag>
          <el-input
              class="input-new-tag"
              v-if="inputSelectVisible"
              v-model="inputSelectValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm(1)"
              @blur="handleInputConfirm(1)"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput(1)">+ 新选项</el-button>
        </el-form-item>

        <!--判断题答案-->
        <el-form-item v-if="questionForm.typeCode === 2" label="参考答案">
          <el-radio v-model="questionForm.judgmentAnswer" :label = "true">正确</el-radio>
          <el-radio v-model="questionForm.judgmentAnswer" :label = "false">错误</el-radio>
        </el-form-item>

        <!--填空题答案-->
        <el-form-item
            v-if="questionForm.typeCode === 3"
            prop="email"
            label="参考答案">
          <el-tag
              :key="tag"
              v-for="tag in tempCompletionList"
              closable
              :disable-transitions="false"
              @close="handleClose(tag,2)">
            {{tag}}
          </el-tag>
          <el-input
              class="input-new-tag"
              v-if="inputCompletionVisible"
              v-model="inputCompletionValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm(2)"
              @blur="handleInputConfirm(2)"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput(2)">+ 答案</el-button>
        </el-form-item>

        <!--简答题答案-->
        <el-form-item label="参考答案" v-if="questionForm.typeCode === 4">
          <el-input
              type="textarea"
              :rows="2"
              :autosize="{minRows:4}"
              v-model="questionForm.correctAnswerList[0]"
              placeholder="请输入答案内容">
          </el-input>
        </el-form-item>

        <!--选择题答案-->
        <el-form-item
            v-if="questionForm.typeCode === 1"
            prop="email"
            label="参考答案"
        >
          <el-checkbox-group v-model="questionForm.correctAnswerList">
            <el-checkbox v-for="item in questionForm.optionList" :label="item" :key="item">{{item}}</el-checkbox>
          </el-checkbox-group>

        </el-form-item>

        <!--关键词-->
        <el-form-item
            v-if="questionForm.typeCode === 4"
            prop="email"
            label="关键词">
          <el-tag
              :key="tag"
              v-for="tag in tempKeywordTagList"
              closable
              :disable-transitions="false"
              @close="handleClose(tag,3)">
            {{tag}}
          </el-tag>
          <el-input
              class="input-new-tag"
              v-if="inputKeywordListVisible"
              v-model="inputKeywordListValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm(3)"
              @blur="handleInputConfirm(3)"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput(3)">+ 关键词</el-button>
        </el-form-item>

        <!--所属知识点-->
        <el-form-item
            prop="email"
            label="所属知识点">
          <el-select v-model="questionForm.knowledgeId" filterable clearable placeholder="请选择">
            <el-option
                v-for="item in allNodeArr"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <!--题目分析-->
        <el-form-item label="题目分析">
          <el-input
              type="textarea"
              :rows="2"
              :autosize="{minRows:4}"
              v-model="questionForm.analysis"
              placeholder="请输入内容">
          </el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="clearDialog">取 消</el-button>
        <el-button type="danger" v-if="questionForm.id" @click="deleteQuestion">删除</el-button>
        <el-button type="primary" @click="saveOrUpdateQuestion">确 定</el-button>
<!--        <el-button type="primary" @click="test">测试</el-button>-->
      </span>

    </el-dialog>
  </el-card>
</template>

<script>
export default {

  created() {
    this.getAllData();
    this.getNodeOption();
  },

  data: function () {
    return {
      loading: false,
      activeName: "select",
      isEditQuestion: false,

      tempCompletionList: [],
      tempKeywordTagList: [],

      currentDialogIsInsert: false,
      allNodeArr: [],

      inputSelectVisible: false,
      inputSelectValue: '',

      inputCompletionVisible: false,
      inputCompletionValue: '',

      inputKeywordListVisible: false,
      inputKeywordListValue: '',

      choiceData: [],
      estimateData: [],
      fillData: [],
      answerData: [],

      questionForm: {
        id: null,
        typeCode: null,
        questionName: "",
        perAnswerScore: null,
        analysis: "",
        optionList: [],
        knowledgeId: null,
        judgmentAnswer: null,
        correctAnswerList: [],
        keywordList: [],
      },
      condition : {
        id:"",
        keywords: "",
        whether: null,
        count: 0,
        limit: 10,
        skip: 0
      },
      allQuestionTypeArr: [
        {
          typeCode: 1,
          name: "选择题"
        },
        {
          typeCode: 2,
          name: "判断题"
        },
        {
          typeCode: 3,
          name: "填空题"
        },
        {
          typeCode: 4,
          name: "简答题"
        },
      ],
    }
  },

  methods: {

    openModel(question) {
      if (question) {
        this.$refs.questionTitle.innerHTML = "修改测试题"
        this.questionForm = JSON.parse(JSON.stringify(question))
        if (question.typeCode === 3) {
          this.tempCompletionList = question.correctAnswerList;
        }
        if (question.typeCode === 4) {
          this.tempKeywordTagList = question.keywordList;
        }
      } else {
        this.$refs.questionTitle.innerHTML = "新增测试题"
        this.clearDialog();
      }
      this.isEditQuestion = true
    },

    getNodeOption() {
      this.axios
          .get("api/knowledge/option")
          .then(({ data }) => {
            if (data.data !== null) {
              this.allNodeArr = data.data
            }
          });
    },

    getAllData() {
      this.getSelectQuestion();
      this.getCompletionQuestion();
      this.getJudgmentQuestion();
      this.getAnswerQuestion();
    },

    getSelectQuestion() {
      this.axios.get("/api/admin/question/select",{
        params: {
          limit: this.condition.limit,
          keywords: this.condition.keywords,
          skip: this.condition.skip
        }
      }).then(({ data }) => {
        if (data.flag) {
          this.choiceData = data.data;
        }
      })
    },

    getJudgmentQuestion() {
      this.axios.get("/api/admin/question/judgment",{
        params: {
          limit: this.condition.limit,
          keywords: this.condition.keywords,
          skip: this.condition.skip
        }
      }).then(({ data }) => {
        if (data.flag) {
          this.estimateData = data.data;
        }
      })
    },

    getCompletionQuestion() {
      this.axios.get("/api/admin/question/completion",{
        params: {
          limit: this.condition.limit,
          keywords: this.condition.keywords,
          skip: this.condition.skip
        }
      }).then(({ data }) => {
        if (data.flag) {
          this.fillData = data.data;
        }
      })
    },

    getAnswerQuestion() {
      this.axios.get("/api/admin/question/answer",{
        params: {
          limit: this.condition.limit,
          keywords: this.condition.keywords,
          skip: this.condition.skip
        }
      }).then(({ data }) => {
        if (data.flag) {
          this.answerData = data.data;
        }
      })
    },

    saveOrUpdateQuestion() {

      if (this.questionForm.typeCode === 3) {
        this.questionForm.correctAnswerList = this.tempCompletionList;
      }
      if (this.questionForm.typeCode === 4) {
        this.questionForm.keywordList = this.tempKeywordTagList;
      }
      this.axios.post("/api/admin/question/saveOrUpdate",this.questionForm)
          .then(({data}) => {
            if (data.flag) {
              this.refresh();
              this.isEditQuestion = false;
              this.$notify.success({
                title: "成功",
                message: data.message,
              });

            }else {
              this.$notify.error({
                title: '失败',
                message: data.message,
              });
            }
          })
    },

    deleteQuestion() {
      this.axios
          .delete("/api/admin/question/deleteById/" + this.questionForm.id)
          .then(({ data }) => {
            if (data.flag === true) {
              this.isEditQuestion = false;
              this.refresh();
              this.$notify({
                title: '成功',
                message: '删除成功',
                type: 'success'
              });
            }else {
              this.$notify.error({
                title: '失败',
                message: data.message,
              });
            }
          });
    },

    handleClose(tag,index) {
      switch (index) {
        case 1:
          this.questionForm.optionList.splice(this.questionForm.optionList.indexOf(tag),1)
          this.questionForm.correctAnswerList.splice(this.questionForm.correctAnswerList.indexOf(tag),1)
          break

        case 2:
          this.tempCompletionList.splice(this.tempCompletionList.indexOf(tag),1)
          break

        case 3:
          this.tempKeywordTagList.splice(this.tempKeywordTagList.indexOf(tag),1)
          break
      }
    },

    showInput(index) {
      switch (index) {
        case 1:
          this.inputSelectVisible = true
          break

        case 2:
          this.inputCompletionVisible = true
          break

        case 3:
          this.inputKeywordListVisible = true
          break
      }
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm(index) {
      let inputValue;
      switch (index) {
        case 1:
          inputValue = this.inputSelectValue;
          if (inputValue) {
            this.questionForm.optionList.push(inputValue);
          }
          this.inputSelectVisible = false;
          this.inputSelectValue = "";
          break

        case 2:
          inputValue = this.inputCompletionValue;
          if (inputValue) {
            this.tempCompletionList.push(inputValue);
          }
          this.inputCompletionVisible = false;
          this.inputCompletionValue = "";
          break

        case 3:
          inputValue = this.inputKeywordListValue;
          if (inputValue) {
            this.tempKeywordTagList.push(inputValue);
          }
          this.inputKeywordListVisible = false;
          this.inputKeywordListValue = "";
          break
      }
    },

    clearDialog() {
      this.isEditQuestion = false;
      this.questionForm.id = null;
      this.questionForm.typeCode = null;
      this.questionForm.questionName = "";
      this.questionForm.perAnswerScore = null;
      this.questionForm.analysis = "";
      this.questionForm.optionList = [];
      this.questionForm.knowledgeId = null;
      this.questionForm.judgmentAnswer = null;
      this.questionForm.correctAnswerList = [];
      this.questionForm.keywordList = [];
      this.tempCompletionList = [];
      this.tempKeywordTagList = [];
    },

    sizeChange(size) {
      this.condition.limit = size;
      this.refresh(true);
    },

    currentChange(current) {
      this.condition.skip = (current-1)*10;
      this.getAllData();
    },

    refresh() {
      switch (this.activeName) {
        case "select":
          this.getSelectQuestion();
          break
        case "judgment":
          this.getJudgmentQuestion();
          break
        case "completion":
          this.getCompletionQuestion();
          break
        case "answer":
          this.getAnswerQuestion();
          break
      }
    }
  },

  computed: {
    current() {
      var current = 1;
      current += this.condition.skip/10;
      return current;
    },

    getOptionSelectValue() {
      return (item) => {
        return item.optionList[0]
      }
    }
  }
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.mt {
  margin-top: 20px;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>