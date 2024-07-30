<script lang="ts" setup>
import { onMounted, ref } from 'vue'

import { Refresh } from '@element-plus/icons-vue'

defineProps({
  total: {
    type: Number,
    default: 0
  }
})

const pageParam = ref<PageParam>({
  current: 1,
  size: 20
})
const emits = defineEmits(['reload'])

onMounted(() => reload(false, pageParam.value))
const reload = (update: boolean, param: PageParam) => {
  if (update) {
    pageParam.value = param
  }
  emits('reload', param)
}
</script>
<template>
  <el-pagination
    v-model:current-page="pageParam.current"
    v-model:page-size="pageParam.size"
    :page-sizes="[20, 40, 80]"
    :total="total"
    layout="total, slot, prev, pager, next, jumper, sizes"
    @size-change="(size: number) => reload(true, { current: pageParam.current, size })"
    @current-change="(current: number) => reload(true, { size: pageParam.size, current })"
  >
    <el-button :icon="Refresh" link @click="reload(false, pageParam)" />
  </el-pagination>
</template>

<style lang="scss" scoped></style>
