<script lang="ts" setup>
import { onMounted, provide, reactive, ref, watch } from 'vue'
import { count as getCount, namespace, type OverviewVO } from '@/api/overview'
import i18n from '@/util/i18n'
import { useI18nStore } from '@/stores/i18n'

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
const i18nStore = useI18nStore()
const curNamespace = ref()
const namespaces = ref<string[]>([])
const count = ref<OverviewVO>({
  success: 0,
  error: 0,
  counts: []
})

const pieOption = reactive({
  title: {
    text: i18n.global.t('page.overview.pieTitle'),
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: [i18n.global.t('page.overview.success'), i18n.global.t('page.overview.error')]
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
      name: i18n.global.t('page.overview.pieTitle'),
      type: 'pie',
      data: [
        {
          name: i18n.global.t('page.overview.success'),
          value: 0
        },
        {
          name: i18n.global.t('page.overview.error'),
          value: 0
        }
      ]
    }
  ]
})

const lineOption = reactive({
  title: {
    text: i18n.global.t('page.overview.lineTitle'),
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
    data: [i18n.global.t('page.overview.success'), i18n.global.t('page.overview.error')]
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
      name: i18n.global.t('page.overview.success'),
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
      data: [] as number[]
    },
    {
      name: i18n.global.t('page.overview.error'),
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
      data: [] as number[]
    }
  ]
})

onMounted(() => {
  initNamespace()
  initCount(undefined)
})

watch(
  () => i18nStore.lang,
  () => {
    pieOption.title.text = i18n.global.t('page.overview.pieTitle')
    pieOption.legend.data = [
      i18n.global.t('page.overview.success'),
      i18n.global.t('page.overview.error')
    ]
    pieOption.series[0].name = i18n.global.t('page.overview.pieTitle')
    pieOption.series[0].data[0].name = i18n.global.t('page.overview.success')
    pieOption.series[0].data[1].name = i18n.global.t('page.overview.error')
    lineOption.title.text = i18n.global.t('page.overview.lineTitle')
    lineOption.legend.data = [
      i18n.global.t('page.overview.success'),
      i18n.global.t('page.overview.error')
    ]
    lineOption.series[0].name = i18n.global.t('page.overview.success')
    lineOption.series[1].name = i18n.global.t('page.overview.error')
  }
)

watch(
  () => count.value,
  (curCount) => {
    pieOption.series[0].data[0].value = curCount.success
    pieOption.series[0].data[1].value = curCount.error
    lineOption.xAxis[0].data = curCount.counts.map((ct) => ct.date)
    lineOption.series[0].data = curCount.counts.map((ct) => ct.success)
    lineOption.series[1].data = curCount.counts.map((ct) => ct.error)
  }
)

const initNamespace = () => {
  namespace().then((result) => {
    if (result.success) {
      namespaces.value = result.data
    }
  })
}

const initCount = (namespace: string | undefined) => {
  getCount({ namespace }).then((result) => {
    if (result.success) {
      count.value = result.data
    }
  })
}

const namespaceChange = (val: string) => {
  initCount(val)
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
        <el-form-item :label="$t('common.namespace')">
          <el-select
            v-model="curNamespace"
            :placeholder="$t('common.all')"
            clearable
            @change="namespaceChange"
          >
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
