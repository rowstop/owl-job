<script lang="ts" setup>
import { onMounted, type PropType, ref, watch } from 'vue'

import { Refresh } from '@element-plus/icons-vue'

defineProps({
  total: {
    type: Number,
    default: 0
  }
})

const reload = defineModel({
  type: Object as PropType<PageReload>,
  default() {
    return {
      current: 1,
      reload: false
    }
  }
})

const pageParam = ref<PageParam>({
  current: 1,
  size: 20
})
const emits = defineEmits(['loadData'])
watch(
  () => reload.value,
  ({ current, reload }) => {
    console.log(`watch=> current:${current},reload:${reload}`)
    if (current != pageParam.value.current) {
      pageParam.value.current = current
      loaData(false, pageParam.value)
      return
    }
    if (reload) {
      loaData(false, pageParam.value)
    }
  },
  {
    deep: true
  }
)

onMounted(() => loaData(false, pageParam.value))

function loaData(update: boolean, param: PageParam) {
  console.log('loaData,loaData,loaData')
  resetLoaded()
  if (update) {
    pageParam.value = param
  }
  emits('loadData', param)
}

function resetLoaded() {
  reload.value.reload = false
}
</script>
<template>
  <el-pagination
    v-model:current-page="pageParam.current"
    v-model:page-size="pageParam.size"
    :page-sizes="[20, 40, 80]"
    :total="total"
    layout="total, slot, prev, pager, next, jumper, sizes"
    @size-change="(size: number) => loaData(true, { current: pageParam.current, size })"
    @current-change="(current: number) => loaData(true, { size: pageParam.size, current })"
  >
    <el-button :icon="Refresh" link @click="loaData(false, pageParam)" />
  </el-pagination>
</template>

<style lang="scss" scoped></style>
