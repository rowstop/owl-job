<script lang="ts" setup>
import { onMounted, ref } from 'vue'

import type { NamespaceVO } from '@/api/namespace/model'
import Page from '@/components/container/Page.vue'
import Pagination from '@/components/tools/Pagination.vue'

import { page } from '@/api/namespace'

const pageParam = ref<PageParam>({
  current: 1,
  size: 20
})

const pageInfo = ref<PageInfo>({
  current: 1,
  size: 20,
  total: 0
})

const pageResult = ref<Page<NamespaceVO>>()

onMounted(() => pageInit())

const pageInit = () => {
  page(pageParam.value).then((result) => {
    const data = result.data
    pageResult.value = data
    pageInfo.value = {
      ...pageInfo.value,
      total: data.total
    }
  })
}

/**
 * 重新加载数据
 * @param param
 */
const reload = (param: PageParam) => {
  pageParam.value = param
  pageInit()
}
</script>

<template>
  <page>
    <template #search></template>
    <template v-slot="data">
      <el-table :data="pageResult?.records" :height="data.height" stripe>
        <el-table-column label="名称" prop="name" />
        <el-table-column label="注册时间" prop="time" />
      </el-table>
    </template>
    <template #footer>
      <pagination v-model="pageInfo" @reload="reload" />
    </template>
  </page>
</template>

<style scoped></style>
