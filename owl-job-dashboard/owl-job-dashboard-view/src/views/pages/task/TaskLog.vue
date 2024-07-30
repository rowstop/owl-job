<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { reactive, ref } from 'vue'
import { page } from '@/api/job/log'
import type { JobLog } from '@/api/job/log/model'

const pageData = ref<Page<JobLog>>({
  records: [],
  total: 0
})

const errorDrawer = reactive({
  show: false,
  error: ''
})

const reload = (param: PageParam) => {
  page(param).then((result) => {
    if (result.success) {
      pageData.value = result.data
    }
  })
}

const showError = (error?: string) => {
  if (!error) {
    return
  }
  errorDrawer.error = error
  errorDrawer.show = true
}

const replay = (row: JobLog) => {
  console.log(row)
}
</script>

<template>
  <table-page :page="pageData" @reload="reload">
    <el-table-column
      :label="$t('page.task.log.tableColumns.namespace')"
      prop="namespace"
      show-overflow-tooltip
    />
    <el-table-column
      :label="$t('page.task.log.tableColumns.group')"
      prop="taskGroup"
      show-overflow-tooltip
    />
    <el-table-column
      :label="$t('page.task.log.tableColumns.resultOfExecution')"
      prop="taskId"
      show-overflow-tooltip
    >
      <template #default="{ row }: { row: JobLog }">
        <el-button v-if="row.error" size="small" type="danger" @click="showError(row.error)"
          >{{ $t('page.task.log.tools.fail') }}
        </el-button>
        <el-button v-else size="small" type="success"
          >{{ $t('page.task.log.tools.success') }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column
      :label="$t('page.task.log.tableColumns.execTime')"
      prop="execTime"
      show-overflow-tooltip
    />
    <el-table-column
      :label="$t('page.task.log.tableColumns.settingTime')"
      prop="settingTime"
      show-overflow-tooltip
    />
    <el-table-column label="操作">
      <template #default="{ row }: { row: JobLog }">
        <el-dropdown size="small" split-button type="primary">
          Dropdown List
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>{{ row.taskId }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </el-table-column>
  </table-page>
  <el-drawer
    v-model="errorDrawer.show"
    :title="$t('page.task.log.tools.failTitle')"
    direction="rtl"
    size="50%"
  >
    <el-scrollbar>
      <pre>
      <code class="error-box" v-text="errorDrawer.error" />
      </pre>
    </el-scrollbar>
  </el-drawer>
</template>

<style lang="scss" scoped>
.error-box {
  display: table;
  white-space: pre;
  color: var(--el-color-danger);
}
</style>
