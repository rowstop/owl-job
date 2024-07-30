<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { NamespaceVO } from '@/api/namespace/model'

import { page } from '@/api/namespace'
import TablePage from '@/components/container/TablePage.vue'

const pageData = ref<Page<NamespaceVO>>({
  records: [],
  total: 0
})

const groupDraw = reactive({
  show: false,
  namespace: '',
  pageData: {
    records: [],
    total: 0
  } as Page<any>
})

const reload = (pageParam: PageParam) => {
  page(pageParam).then((result) => {
    pageData.value = result.data
  })
}
const showNamespaceGroup = (namespace: string) => {
  groupDraw.namespace = namespace
}
const reloadGroup = (pageParam: PageParam) => {
  console.log(1)
}
</script>

<template>
  <table-page :page="pageData" @reload="reload">
    <template #search>
      <div>111</div>
      <div>111</div>
    </template>
    <el-table-column :label="$t('page.namespace.tableColumns.namespace')" prop="name" />
    <el-table-column :label="$t('page.namespace.tableColumns.regTime')" prop="time" />
    <el-table-column :label="$t('page.namespace.tableColumns.groupCount')">
      <template #default="{ row }: { row: NamespaceVO }">
        <el-link @click="showNamespaceGroup(row.name)">{{ row.groupCount }}</el-link>
      </template>
    </el-table-column>
    <el-table-column :label="$t('page.namespace.tableColumns.curTaskCount')" prop="curTaskCount" />
    <el-drawer v-model="groupDraw.show" direction="rtl" size="80%" title="任务分组列表">
      <table-page :page="groupDraw.pageData" @reload="reloadGroup"></table-page>
    </el-drawer>
  </table-page>
</template>

<style scoped></style>
