<template>
  <div ref="graph" :id="this.uuid" :style="style"></div>
</template>

<script>
import * as echarts from 'echarts';

const idGen = () => {
  return new Date().getTime();
}

export default {

  props: {
    height: {
      type: String,
      default: '700px'
    },
    width: {
      type: String,
      default: '1570px'
    },
    // options: {
    //   type:Object,
    //   default: null
    // }
  },

  data() {
    return {
      uuid: null,
      myChart: null,
      flag: false,
      options: {
        title: { text: '计算机网络知识图谱' },
        tooltip: {
          formatter: function (x) {
            return x.data.des;
          }
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            symbolSize: 80,
            roam: true,
            edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [4, 10],
            edgeLabel: {
              normal: {
                show: true,
                formatter: function (x) {
                  return x.data.name;
                },
                textStyle: {
                  fontSize: 20
                }
              }
            },
            force: {
              repulsion: 1150,
              edgeLength: [200, 300],
            },
            draggable: true,
            label: {
              normal: {
                show: true,
              }
            },
            itemStyle: {
              normal: {
                color: '#4b565b'
              }
            },
            lineStyle: {
              normal: {
                width: 1,
                color: '#4b565b'
              }
            },
            data: [
              {
                name:"111"
              }
            ],
            links: []
          }
        ]
      }
    }
  },

  mounted() {
    const that = this;
    // 基于准备好的dom，初始化echarts实例
    this.axios.get("api/knowledge/node").then(({data}) => {
      data.data.forEach(item => {
        this.options.series[0].data.push({

        });
      })
      this.myChart = echarts.init(document.getElementById(this.uuid));
      this.myChart.setOption(this.options);
      this.myChart.on('click',function (params){
        that.$store.commit("selectNode",params);
      })
    })
  },

  created() {
    this.uuid = idGen();
  },



  methods: {

  },

  computed: {

    style() {
      return {
        height: this.height,
        width: this.width
      }
    }

  }

}
</script>

<style scoped>

</style>