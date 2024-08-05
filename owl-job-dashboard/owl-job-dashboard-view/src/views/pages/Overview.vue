<script lang="ts" setup>
import Page from '@/components/container/Page.vue'
import ECharts from 'vue-echarts'
import { graphic, use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import {
  GridComponent,
  LegendComponent,
  TitleComponent,
  ToolboxComponent,
  TooltipComponent
} from 'echarts/components'
import { onMounted, provide, ref, watch } from 'vue'
import { count as getCount, namespace, type OverviewVO } from '@/api/overview'

use([
  CanvasRenderer,
  PieChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  ToolboxComponent,
  GridComponent
])

provide(ECharts.THEME_KEY, 'dark')
const curNamespace = ref()
const namespaces = ref<string[]>([])
const count = ref<OverviewVO>({
  success: 0,
  error: 0,
  counts: []
})

const pieOption = ref({
  title: {
    text: '运行结果比率',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: ['成功', '失败']
  },
  color: [
    new graphic.LinearGradient(0, 0, 0, 1, [
      {
        offset: 0,
        color: 'rgb(0, 221, 255)'
      },
      {
        offset: 1,
        color: 'rgb(77, 119, 255)'
      }
    ]),
    new graphic.LinearGradient(0, 0, 0, 1, [
      {
        offset: 0,
        color: 'rgb(135, 0, 157)'
      },
      {
        offset: 1,
        color: 'rgb(255, 0, 135)'
      }
    ])
  ],
  series: [
    {
      name: '运行结果比率',
      type: 'pie',
      data: [
        {
          name: '成功',
          value: 335
        },
        {
          name: '失败',
          value: 310
        }
      ]
    }
  ]
})

const lineOption = ref({
  title: {
    text: '运行结果',
    left: '10'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      label: {
        backgroundColor: '#6a7985'
      }
    }
  },
  color: ['#00DDFF', '#FF0087'],
  legend: {
    data: ['成功', '失败']
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    }
  ],
  yAxis: [
    {
      type: 'value'
    }
  ],
  series: [
    {
      name: '成功',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        width: 0
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
        color: new graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: 'rgb(0, 221, 255)'
          },
          {
            offset: 1,
            color: 'rgb(77, 119, 255)'
          }
        ])
      },
      emphasis: {
        focus: 'series'
      },
      data: [120, 282, 111, 234, 220, 340, 310]
    },
    {
      name: '失败',
      type: 'line',
      stack: 'Total',
      smooth: true,
      lineStyle: {
        width: 0
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
        color: new graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: 'rgb(255, 0, 135)'
          },
          {
            offset: 1,
            color: 'rgb(135, 0, 157)'
          }
        ])
      },
      emphasis: {
        focus: 'series'
      },
      data: [220, 402, 231, 134, 190, 230, 120]
    }
  ]
})

onMounted(() => {
  initNamespace()
  initCount()
})

watch(
  () => count.value,
  (curCount) => {
    pieOption.value.series[0].data = [
      {
        name: '成功',
        value: curCount.success
      },
      {
        name: '失败',
        value: curCount.error
      }
    ]
    const line = lineOption.value
    line.xAxis[0].data = curCount.counts.map((ct) => ct.date)
    line.series[0].data = curCount.counts.map((ct) => ct.success)
    line.series[1].data = curCount.counts.map((ct) => ct.error)
  }
)

const initNamespace = () => {
  namespace().then((result) => {
    if (result.success) {
      namespaces.value = result.data
    }
  })
}

const initCount = () => {
  getCount().then((result) => {
    if (result.success) {
      count.value = result.data
    }
  })
}

const namespaceChange = (val: string) => {
  console.log('namespace: ' + val)
}
</script>

<template>
  <page>
    <e-charts :option="lineOption" autoresize style="height: 500px" />
    <el-divider />
    <el-row>
      <el-col :span="12">
        <e-charts :option="pieOption" autoresize style="height: 500px" />
      </el-col>
      <el-col :span="12">
        <el-form-item label="命名空间">
          <el-select v-model="curNamespace" clearable placeholder="全部" @change="namespaceChange">
            <el-option
              v-for="namespace in namespaces"
              :key="namespace"
              :label="namespace"
              :value="namespace"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </page>
</template>

<style scoped>
.ep-bg-purple {
  height: 500px;
  background-color: chartreuse;
}
</style>
