<script lang="ts" setup>
import { ref } from 'vue'

defineProps()
const page = ref(1)
const size = ref(20)
const pageSizes = ref([20])

const sizeChange = (curSize: number) => {
  size.value = curSize
}
const currentChange = (curSize: number) => {
  size.value = curSize
}
</script>

<template>
  <el-pagination
    v-model:current-page="page"
    v-model:page-size="size"
    :page-sizes="pageSizes"
    :total="400"
    layout="total, sizes, prev, pager, next, jumper"
    @size-change="sizeChange"
    @current-change="currentChange"
  />
</template>

<style lang="scss" scoped></style>
