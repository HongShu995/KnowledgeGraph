<template>
  <el-card class="main-card">
    <div class="title">知识图谱</div>
    <div class="operation-container">
      <div>
        <span v-if="nodeForm.id">
          <el-button type="primary" icon="el-icon-warning-outline" size="small"
                     @click="openInfoModel(false)">
            节点信息
          </el-button>

          <span v-if="nodeForm.isHaveTest">
            &nbsp&nbsp
            <el-button type="info" size="small"
                                icon="el-icon-s-claim"
                                @click="test"
            >
              试题测试
            </el-button>
          </span>

          <span>
          &nbsp&nbsp<i class="el-icon-star-off" style="margin-right:5px" />重要等级：<span style="color: #FFB200FF">{{ this.star(this.nodeForm.level) }}</span>
          </span>
          <span v-if="nodeForm.url && nodeForm.url!='NULL'">
          &nbsp&nbsp<i class="el-icon-link" style="margin-right:5px" />相关链接：<el-link :href="this.nodeForm.url"
                                                                                     target="_blank" type="primary" style="font-size: 15px;margin-bottom: 3px">点击前往</el-link>
          </span>
          <span>
          &nbsp&nbsp<i class="el-icon-time" style="margin-right:5px" />创建时间：{{ this.nodeForm.createTime }}
          </span>
        </span>

        <span v-if="linkForm.id">
          <el-button type="success" icon="el-icon-sort" size="small" @click="openRelationModel(false)">
            关系信息
          </el-button>
          <span>
            &nbsp&nbsp<i class="el-icon-time" style="margin-right:5px" />创建时间：{{ this.linkForm.createTime }}
          </span>
        </span>


      </div>
      <!-- 条件筛选 -->
      <div style="margin-left: auto">
        <el-button type="primary" icon="el-icon-plus" size="small"
                   @click="openInfoModel(true)">
          新增节点
        </el-button>
        <el-button type="success" icon="el-icon-share" size="small"
                    style="margin-right: 10px" @click="openRelationModel(true)">
          新建关系
        </el-button>
        <el-input-number v-model="condition.depth" style="width: 75px;margin-right:0.5rem"
                         :min="1" :max="5"
                         controls-position="right" size="small" />
        <el-input size="small" style="width:200px" v-model="condition.keywords"
                  prefix-icon="el-icon-search" placeholder="请输入知识点名称" />
        <el-button size="small" style="margin-left:0.5rem"
                   icon="el-icon-search" type="primary"
                   @click="listNode"
        >
          搜索
        </el-button>
      </div>
    </div>

    <!--图表-->
    <div>
      <el-card class="graph-card">
        <div ref="graph" id="main" style="width: 1570px;height: 700px"></div>
      </el-card>
    </div>

    <!--节点信息-->
    <el-dialog :visible.sync="isEdit" width="30%">
      <div class="dialog-title-container" slot="title" ref="nodeMenu"></div>
      <el-form label-width="100px" size="medium" :model="nodeForm">
        <el-form-item v-if="nodeForm.id" label="节点ID:">
          {{nodeForm.id}}
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="nodeForm.name" style="width: 220px" placeholder="请输入知识点名称" />
          <span class="must-remind">*</span>
        </el-form-item>
        <el-form-item label="节点颜色">
          <el-color-picker v-model="nodeForm.color"></el-color-picker>
        </el-form-item>
        <el-form-item label="重要等级">
          <el-select v-model="nodeForm.level" placeholder="请选择">
            <el-option
                v-for="item in levels"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
          <span class="must-remind">*</span>
        </el-form-item>
        <el-form-item label="节点大小">
          <el-input-number v-model="nodeForm.symbolSize"
                           :min="50" :max="150" :step="10" />
        </el-form-item>
        <el-form-item label="节点内容">
          <el-input type="textarea" :autosize="{minRows:5}"
                    v-model="nodeForm.des" placeholder="请输入内容(换行请使用<br/>)" />
        </el-form-item>
        <el-form-item label="相关链接">
          <el-input v-model="nodeForm.url" placeholder="请输入知识点相关内容链接(请填入完整链接包含协议)" />
        </el-form-item>
      </el-form>
      <div class="tips">ps：输入内容需要换行时请使用&lt;/br&gt;</div>
      <div slot="footer">
        <el-button type="danger" v-if="nodeForm.id" @click="isDelete=true">删除</el-button>
        <el-button type="primary" @click="editNode">确 定</el-button>
        <el-button @click="isEdit = false">取 消</el-button>
      </div>
      <el-dialog :visible.sync="isDelete" width="25%" append-to-body style="margin-top: 250px">
        <div class="dialog-title-container" slot="title">
          删除
        </div>
        <h2 style="margin-left: 130px">是否删除该节点？</h2>
        <div slot="footer">
          <el-button @click="isDelete=false">取 消</el-button>
          <el-button type="danger" @click="deleteNode">
            确 定
          </el-button>
        </div>
      </el-dialog>
    </el-dialog>

    <!--连接信息-->
    <el-dialog :visible.sync="isConnect" width="30%">
      <div class="dialog-title-container" slot="title" ref="linkMenu"></div>
      <el-form label-width="100px" size="medium" :model="linkForm">
        <el-form-item v-if="linkForm.id" label="连线ID:">
          {{linkForm.id}}
        </el-form-item>
        <el-form-item label="连线名称">
          <el-input v-model="linkForm.name" style="width: 200px" placeholder="请输入连线名称" />
          <span class="must-remind">*</span>
        </el-form-item>
        <el-form-item v-if="!linkForm.id"  label="起点节点">
<!--          <el-input v-model="linkForm.source" style="width: 200px" :disabled="linkForm.id" placeholder="请输入连线名称" />-->
          <el-select v-model="linkForm.source" filterable clearable placeholder="请选择起始节点" style="width: 200px">
            <el-option
                v-for="item in nodeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
          <span class="must-remind">*</span>
        </el-form-item>
        <el-form-item v-if="!linkForm.id" label="终点节点">
<!--          <el-input v-model="linkForm.target" style="width: 200px" :disabled="linkForm.id" placeholder="请输入连线名称" />-->
          <el-select v-model="linkForm.target" filterable clearable placeholder="请选择结束节点" style="width: 200px">
            <el-option
                v-for="item in nodeOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
          <span class="must-remind">*</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button v-if="linkForm.id" @click="isDeleteLink=true" type="danger" >删除</el-button>
        <el-button type="primary" @click="editLink">确定</el-button>
        <el-button @click="isConnect = false">取消</el-button>
      </div>
      <el-dialog :visible.sync="isDeleteLink" width="25%" append-to-body style="margin-top: 100px">
        <div class="dialog-title-container" slot="title">
          删除
        </div>
        <h2 style="margin-left: 130px">是否删除该节点关系？</h2>
        <div slot="footer">
          <el-button @click="isDeleteLink=false">取 消</el-button>
          <el-button type="danger" @click="deleteLink">
            确 定
          </el-button>
        </div>
      </el-dialog>
    </el-dialog>

    <!--习题测试-->
    <el-dialog :visible.sync="isTest" width="60%"
               title="知识点测试题">
      <el-card v-if="choiceData.length > 0">
        <div slot="header">
          <span>选择题</span>
        </div>
        <div v-for="(item,index) in choiceData">
          <div class="mb">
            {{index + 1}}、{{item.questionName}} <span>({{item.perAnswerScore}}分)</span>
          </div>
          <div class="doAnswer-area">
            <el-checkbox-group v-model="choiceList">
              <el-checkbox v-for="object in item.optionList" :label="object"/>
            </el-checkbox-group>
            <div v-if="isSubmit">
              <div class="answer-area">
                正确答案为：{{item.correctAnswerList}}
              </div>
              <div class="analyse-area">
                解析：{{item.analysis}}
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <el-card v-if="estimateData.length > 0" class="mt">
        <div slot="header">
          <span>判断题</span>
        </div>
        <div v-for="(item,index) in estimateData">
          <div class="mb">
            {{index + 1}}、{{item.questionName}} <span>({{item.perAnswerScore}}分)</span>
          </div>
          <div class="doAnswer-area">
            <el-radio v-model="estimateList[index]" label="true">正确</el-radio>
            <el-radio v-model="estimateList[index]" label="false">错误</el-radio>
            <div v-if="isSubmit">
              <div class="answer-area">
                <div>正确答案为：
                  <span v-if="item.judgmentAnswer">
                  ✔️
                </span>
                  <span v-if="!item.judgmentAnswer">
                  ❌
                </span>
                </div>
              </div>
              <div class="analyse-area">
                解析：{{item.analysis}}
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <el-card v-if="fillData.length > 0" class="mt">
        <div slot="header">
          <span>填空题</span>
        </div>
        <div v-for="(item,index) in fillData">
          <div class="mb">
            {{index + 1}}、{{item.questionName}} <span>({{item.perAnswerScore}}分)</span>
          </div>
          <div class="doAnswer-area">
            <el-input :placeholder="'请输入第'+(index2 + 1)+'空答案'"
                      v-model="fillList[index2]"
                      v-for="(item2,index2) in item.correctAnswerList"
                      class="mb"
            />
            <div v-if="isSubmit">
              <div class="answer-area">
                正确答案为：{{item.correctAnswerList}}
              </div>
              <div class="analyse-area">
                解析：{{item.analysis}}
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <el-card v-if="answerData.length > 0" class="mt">
        <div slot="header">
          <span>简答题</span>
        </div>
        <div v-for="(item,index) in answerData">
          <div class="mb">
            {{index + 1}}、{{item.questionName}} <span>({{item.perAnswerScore}}分)</span>
          </div>
          <div class="doAnswer-area">
            <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 6}"
                placeholder="请输入内容"
                v-model="answer">
            </el-input>
            <div v-if="isSubmit">
              <div class="answer-area">
                <div>正确答案为：{{item.correctAnswerList[0]}}</div>
                <div>得分关键词：{{item.keywordList}}</div>
              </div>
              <div class="analyse-area">
                解析：{{item.analysis}}
              </div>
            </div>
          </div>
        </div>
      </el-card>
      <div>
        <el-button type="primary"
            style="margin-left: 470px;margin-top: 20px"
            @click="submitTest">提交</el-button>
        <el-button @click="clearTest">取消</el-button>
      </div>
    </el-dialog>

  </el-card>

</template>

<script>
import echarts from 'echarts';
// const idGen = () => {
//   return new Date().getTime();
// }
export default {

  created() {
    this.nodeForm = this.$store.state.node;
    this.linkForm = this.$store.state.link;
  },

  mounted() {
    this.drewGraph();
    this.listOption();
  },

  data() {
    return {
      uuid: null,
      myChart: null,
      isEdit: false,
      isDelete: false,
      isDeleteLink: false,
      isConnect: false,
      isTest: false,
      isComputed: false,
      isSubmit: false,

      data: [],
      links: [],
      nodeOptions: [],
      choiceData: [],
      estimateData: [],
      fillData: [],
      answerData: [],
      choiceList: [],
      estimateList: [],
      fillList:[],
      answer: "",

      levels: [
        {
          value: 1,
          label: '⭐'
        },
        {
          value: 2,
          label: '⭐⭐'
        },
        {
          value: 3,
          label: '⭐⭐⭐'
        },
        {
          value: 4,
          label: '⭐⭐⭐⭐'
        },
        {
          value: 5,
          label: '⭐⭐⭐⭐⭐'
        }],
      options: {
        title: { text: '计算机网络知识图谱' },
        tooltip: {
          show : true,
          formatter: function (x) {
            return x.data.des;
          }
        },
        series: [{
          type: 'graph', //关系图
          name: "",//系列名称，用于tooltip的显示，legend 的图例筛选，在 setOption 更新数据和配置项时用于指定对应的系列。
          layout: "force",//图的布局，类型为力导图，'circular' 采用环形布局
          legendHoverLink : true,//是否启用图例 hover(悬停) 时的联动高亮
          hoverAnimation : true,//是否开启鼠标悬停节点的显示动画
          coordinateSystem : null,//坐标系可选
          edgeSymbol: ['circle', 'arrow'],
          edgeSymbolSize: [4, 10],
          force : { //力引导图基本配置
            repulsion : 1000,//节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
            //gravity : 0.01,//节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
            edgeLength :[150,250],//边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
            layoutAnimation : true
            //因为力引导布局会在多次迭代后才会稳定，这个参数决定是否显示布局的迭代动画，在浏览器端节点数据较多（>100）的时候不建议关闭，布局过程会造成浏览器假死。
          },
          roam : true,//是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
          nodeScaleRatio : 0.6,//鼠标漫游缩放时节点的相应缩放比例，当设为0时节点不随着鼠标的缩放而缩放
          draggable : true,//节点是否可拖拽，只在使用力引导布局的时候有用。
          focusNodeAdjacency : true,//是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。
          itemStyle : { //===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
            normal : { //默认样式
              color: "#4b565b",
              label : {
                show : true
              },
              borderType : 'solid', //图形描边类型，默认为实线，支持 'solid'（实线）, 'dashed'(虚线), 'dotted'（点线）。
              borderColor : 'rgba(42,245,255,0.79)', //设置图形边框为淡金色,透明度为0.4
              borderWidth : 1, //图形的描边线宽。为 0 时无描边。
            },
            emphasis : {//高亮状态
              borderWidth : 3
            }
          },
          lineStyle : { //==========关系边的公用线条样式。
            normal : {
              color : '#4b565b',
              width : '2',
              type : 'solid', //线的类型 'solid'（实线）'dashed'（虚线）'dotted'（点线）
              opacity : 1
              // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
            },
            emphasis : {//高亮状态
              color:'red'

            }
          },
          label : { //=============图形上的文本标签
            normal : {
              show : true,//是否显示标签。
              position : 'inside',//标签的位置。['50%', '50%'] [x,y]
              textStyle : { //标签的字体样式
                color : 'black', //字体颜色
                fontStyle : 'normal',
                fontWeight : 'bold',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                fontFamily : 'sans-serif', //文字的字体系列
                fontSize : 15, //字体大小
              }
            },
            emphasis : {//高亮状态
            }
          },
          edgeLabel : {//==============线条的边缘标签
            normal : {
              show : true,
              formatter: function (x) {
                return x.data.name;
              }
            }
          },
          nodes: [],
          links: []
        }]
      },
      nodeForm: {
        id: null,
        name: null,
        color: null,
        des: null,
        level: null,
        url: null,
        symbolSize: null,
        createTime: null,
        isHaveTest: null,
      },
      node: {
        id: null,
        name: null,
        itemStyle: {
          normal: {
            color: null
          }
        },
        des: null,
        level: null,
        url: null,
        symbolSize: null,
        createTime: null,
      },
      linkForm: {
        id: null,
        name: null,
        source: null,
        target: null,
        createTime: null
      },
      condition : {
        id:"",
        keywords: "",
        whether: null,
        count: 0,
        depth: 1,
      },
    }
  },

  methods: {

    submitTest() {
      this.isSubmit = true;
      console.log(this.choiceList)
      console.log(this.estimateList)
      console.log(this.fillList)
      console.log(this.answer)
    },

    listNode() {
      this.$store.commit("clear")
      this.options.series[0].nodes = [];
      this.options.series[0].links = [];
      this.listOption()
      this.myChart.dispose();
      this.drewGraph()
    },

    listOption() {
      this.axios.get("api/knowledge/option").then(({data}) => {
        this.nodeOptions = data.data;
      })
    },

    openInfoModel(flag) {
      if (flag) {
        this.$refs.nodeMenu.innerHTML = "新增节点";
        this.clear();
      } else {
        this.$refs.nodeMenu.innerHTML = "节点信息";
      }
      this.isEdit = true;
    },

    openRelationModel(flag) {
      if (flag) {
        this.clear();
        this.$refs.linkMenu.innerHTML = "新建关系";
      } else {
        this.$refs.linkMenu.innerHTML = "关系信息";
      }
      this.isConnect = true;
    },

    editNode() {
      if (this.nodeForm.name.trim() === "") {
        this.$message.error("节点名不能为空");
        return false;
      }
      if (this.nodeForm.level === null) {
        this.$message.error("节点等级未选择");
        return false;
      }
      this.axios.post("api/admin/knowledge/update",this.nodeForm)
      .then(({data})=>{
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          })
          this.listNode();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          this.clear();
        }
      });
      this.isEdit = false;
    },

    editLink() {
      if (this.linkForm.name === null) {
        this.$message.error("连线名不能为空");
        return false;
      }
      if (this.linkForm.source === null) {
        this.$message.error("起始节点未选择");
        return false;
      }
      if (this.linkForm.target === null) {
        this.$message.error("结束节点未选择");
        return false;
      }
      if (this.linkForm.source === this.linkForm.target) {
        this.$message.error("不能与自己建立关系");
        return false;
      }
      this.axios.post("api/admin/link/update",this.linkForm).then(({data})=>{
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          })
          this.listNode();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          this.clear();
        }
      });
      this.isConnect = false;
    },

    drewGraph() {
      var that = this;
      this.axios.get("api/knowledge/node", {
        params: {
          keywords: this.condition.keywords,
          depth: this.condition.depth
        }
      }).then(({data}) => {
        if(data.flag === false) {
          this.$message.error("用户长时间未操作，请重新登录");
        }
        data.data.forEach(item => {
          this.options.series[0].nodes.push(item);
        })
        this.axios.get("api/knowledge/link").then(({data}) => {
          data.data.forEach(item => {
            this.options.series[0].links.push(item)
          })
          this.myChart = echarts.init(document.getElementById('main'));
          this.myChart.setOption(this.options);
          this.myChart.on('click',function (param) {
            that.clear()
            if (param.data.level !== undefined) {
              that.$store.commit("selectNode",param)
            } else {
              that.$store.commit("selectLink",param)
            }
          })
        })
      })
    },

    deleteNode() {
      this.axios.delete("api/admin/knowledge/"+this.nodeForm.id)
      .then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: "删除成功"
          });
          this.listNode();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.clear();
        this.isDelete = false;
        this.isEdit = false;
      })
    },

    deleteLink() {
      this.axios.delete("api/admin/link/"+this.linkForm.id)
          .then(({data}) => {
            if (data.flag) {
              this.$notify.success({
                title: "成功",
                message: "删除成功"
              });
              this.listNode();
            } else {
              this.$notify.error({
                title: "失败",
                message: data.message
              });
            }
            this.clear();
            this.isConnect = false;
            this.isDeleteLink = false;
          })
    },

    test() {
      this.clearTest();
      this.axios.get("/api/question/query/"+this.nodeForm.id)
      .then(({data}) => {
        if (data.flag) {
          for (let item of data.data) {
            switch (item.typeCode) {
              case 1:
                this.choiceData.push(item);
                break
              case 2:
                this.estimateData.push(item);
                break
              case 3:
                this.fillData.push(item);
                break
              case 4:
                this.answerData.push(item);
                break
            }
          }
          this.isTest = true
        }
      })
    },

//     for (let i = 0; i < data.data.length; i++) {
//   switch (data.data[i].typeCode) {
//     case 1:
//       this.choiceData.push(data.data[i]);
//       break
//     case 2:
//       this.estimateData.push(data.data[i]);
//       break
//     case 3:
//       this.fillData.push(data.data[i]);
//       break
//     case 4:
//       this.answerData.push(data.data[i]);
//       break
//   }
// }
// console.log(this.choiceData)

    star(level) {
      switch (level) {
        case 1:
          return "⭐"
        case 2:
          return "⭐⭐"
        case 3:
          return "⭐⭐⭐"
        case 4:
          return "⭐⭐⭐⭐"
        case 5:
          return "⭐⭐⭐⭐⭐"
      }
    },

    clear() {
      this.nodeForm.id = null;
      this.nodeForm.name = "";
      this.nodeForm.color = "#4b565b";
      this.nodeForm.des = "";
      this.nodeForm.level = null;
      this.nodeForm.url = "";
      this.nodeForm.symbolSize = null;
      this.nodeForm.createTime = "";
      this.linkForm.id = null;
      this.linkForm.name = null;
      this.linkForm.source = null;
      this.linkForm.target = null;
      this.linkForm.createTime = null;
    },

    clearTest() {
      this.isTest = false;
      this.isSubmit = false;
      this.choiceData = [];
      this.estimateData = [];
      this.fillData = [];
      this.answerData = [];
    }
  },

}
</script>

<style scoped>
.must-remind {
  color: #ee3838;
  font-size: 20px;
  margin-left: 10px;
}
.tips {
  color: #ee3838;
  margin-left: 320px;
  font-size: 10px;
}

.doAnswer-area {
  margin-left: 20px;
  margin-bottom: 10px
}

.answer-area {
  margin-left: 20px;
  margin-bottom: 10px;
  color: red;
}

.analyse-area {
  margin-left: 20px;
  margin-bottom: 10px;
  color: #0081ff;
}

.mb {
  margin-bottom: 10px
}

.mt {
  margin-top: 10px
}

</style>