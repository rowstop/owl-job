<script lang="ts" setup>
import { ref } from 'vue'
import type { NamespaceVO } from '@/api/namespace/model'

import { page } from '@/api/namespace'
import TablePage from '@/components/container/TablePage.vue'

const pageParam = ref<PageParam>({
  current: 1,
  size: 20
})

const pageData = ref<Page<NamespaceVO>>({
  records: [],
  total: 0
})

const reload = () => {
  page(pageParam.value).then((result) => {
    pageData.value = result.data
  })
}
</script>

<template>
  <table-page v-model="pageParam" :page="pageData" @reload="reload">
    <template #search>
      <div>111</div>
      <div>111</div>
      <div>111</div>
      <div>111</div>
      <div>111</div>
    </template>
    <el-table-column label="名称" prop="name" />
    <el-table-column label="注册时间" prop="time" />
  </table-page>
</template>

<style scoped></style>
