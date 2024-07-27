<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { ref } from 'vue'
import { page } from '@/api/jobLog'
import type { JobLog } from '@/api/jobLog/model'

const param = ref<PageParam>({
  current: 1,
  size: 20
})

const pageData = ref<Page<JobLog>>({
  records: [],
  total: 0
})
const reload = () => {
  page(param.value).then((result) => (pageData.value = result.data))
}
</script>

<template>
  <table-page v-model="param" :page="pageData" @reload="reload">
    <el-table-column label="执行结果">
      <template #default="{ row }: { row: JobLog }">
        <el-tag v-if="row.error" type="danger">失败</el-tag>
        <el-tag v-else type="success">成功</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="命名空间" prop="namespace" show-overflow-tooltip />
    <el-table-column label="任务分组" prop="jobGroup" show-overflow-tooltip />
    <el-table-column label="任务Id" prop="taskId" show-overflow-tooltip />
    <el-table-column label="执行时间" prop="execTime" show-overflow-tooltip />
    <el-table-column label="设定时间" prop="settingTime" show-overflow-tooltip />
  </table-page>
</template>

<style lang="scss" scoped></style>
