<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { GroupVO, NamespaceVO } from '@/api/namespace/model'
import { groupPage, page } from '@/api/namespace'

import TablePage from '@/components/container/TablePage.vue'

import { List } from '@element-plus/icons-vue'

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
  } as Page<GroupVO>
})

const reloadData = (pageParam: PageParam) => {
  page(pageParam).then((result) => {
    if (result.success) pageData.value = result.data
  })
}
const showNamespaceGroup = (namespace: string) => {
  groupDraw.namespace = namespace
  groupDraw.show = true
}
const reloadGroup = (pageParam: PageParam) => {
  groupPage({ namespace: groupDraw.namespace, ...pageParam }).then((result) => {
    if (result.success) groupDraw.pageData = result.data
  })
}
</script>

<template>
  <table-page :page="pageData" @load-data="reloadData">
    <template #search>
      <div>111</div>
      <div>111</div>
    </template>
    <el-table-column :label="$t('page.namespace.tableColumns.namespace')" prop="name" />
    <el-table-column :label="$t('page.namespace.tableColumns.regTime')" prop="time" />
    <el-table-column :label="$t('page.namespace.tableColumns.groupCount')">
      <template #default="{ row }: { row: NamespaceVO }">
        <el-button :icon="List" text type="primary" @click="showNamespaceGroup(row.name)"
          >{{ row.groupCount }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column :label="$t('page.namespace.tableColumns.curTaskCount')" prop="curTaskCount" />
  </table-page>
  <el-drawer
    v-model="groupDraw.show"
    :title="$t('page.namespace.group.title', { namespace: groupDraw.namespace })"
    direction="rtl"
    size="50%"
  >
    <table-page :page="groupDraw.pageData" @reload="reloadGroup">
      <el-table-column :label="$t('page.namespace.group.name')" prop="name" />
    </table-page>
  </el-drawer>
</template>

<style scoped></style>
