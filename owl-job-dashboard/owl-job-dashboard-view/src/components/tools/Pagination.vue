<script lang="ts" setup>
import type { PropType } from 'vue'

import { Refresh } from '@element-plus/icons-vue'

const emits = defineEmits(['reload'])
const model = defineModel({
  type: Object as PropType<PageInfo>,
  required: true
})
const reload = (param: PageParam) => {
  emits('reload', param)
}
</script>
<template>
  <el-pagination
    v-model:current-page="model.current"
    v-model:page-size="model.size"
    :page-sizes="[20, 40, 80]"
    :total="model.total"
    layout="total, slot, prev, pager, next, jumper, sizes"
    @size-change="(size: number) => reload({ current: model.current, size })"
    @current-change="(current: number) => reload({ size: model.size, current })"
  >
    <el-button :icon="Refresh" link @click="reload({ current: model.current, size: model.size })" />
  </el-pagination>
</template>

<style lang="scss" scoped></style>
