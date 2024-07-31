<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { ref, watch } from 'vue'
import NamespaceSelect from '@/components/tools/NamespaceSelect.vue'
import { ArrowDown } from '@element-plus/icons-vue'
import type { NamespaceVO } from '@/api/namespace/model'
import { del, page, type TaskVO } from '@/api/task'
import { ElMessage } from 'element-plus'

const enum OpType {
  DELETE,
  EDIT
}

const namespace = ref<NamespaceVO>()
const pageData = ref<Page<TaskVO>>({
  records: [],
  total: 0
})

watch(
  () => namespace.value?.name,
  () => reload({ current: 1, size: 20 })
)

const reload = (param: PageParam) => {
  const namespaceName = namespace.value?.name
  if (!namespaceName) {
    return
  }
  page({ namespace: namespaceName, ...param }).then((result) => {
    if (result.success) pageData.value = result.data
  })
}

const operate = (type: OpType, row: TaskVO) => {
  if (type === OpType.EDIT) {
    return
  }
  del({ namespace: namespace.value!.name, group: row.group, taskId: row.taskId }).then((result) => {
    if (result.success) {
      ElMessage.success('已删除')
    }
  })
}
</script>

<template>
  <table-page :page="pageData" @reload="reload">
    <template #search>
      <namespace-select v-model="namespace" />
    </template>
    <el-table-column label="任务运行模式" prop="type" />
    <el-table-column label="下次运行时间" prop="nextTime" />
    <el-table-column label="任务参数" prop="param" show-overflow-tooltip />
    <el-table-column label="操作">
      <template #default="{ row }: { row: TaskVO }">
        <el-dropdown @command="(type: OpType) => operate(type, row)">
          <el-button :icon="ArrowDown" size="small" type="primary">操作</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :command="OpType.DELETE">删除</el-dropdown-item>
              <el-dropdown-item :command="OpType.EDIT">编辑</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </el-table-column>
  </table-page>
</template>

<style lang="scss" scoped></style>
