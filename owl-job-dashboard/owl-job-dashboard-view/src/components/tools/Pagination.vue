<script lang="ts" setup>
import type { PropType } from 'vue'

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
    layout="total, sizes, prev, pager, next, jumper"
    @size-change="(size: number) => reload({ current: model.current, size })"
    @current-change="(current: number) => reload({ size: model.size, current })"
  />
</template>

<style lang="scss" scoped></style>
