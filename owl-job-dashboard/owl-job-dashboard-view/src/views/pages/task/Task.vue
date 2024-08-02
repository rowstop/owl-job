<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { reactive, ref, watch } from 'vue'
import { NamespaceSelect, TaskForm } from '@/components/task'
import { ArrowDown } from '@element-plus/icons-vue'
import type { NamespaceVO } from '@/api/namespace/model'
import { del, page, type TaskAddDTO, type TaskPageDTO, TaskType, type TaskVO } from '@/api/task'
import { ElMessage } from 'element-plus'

const enum OpType {
  DELETE,
  EDIT
}

const taskFormData = ref<TaskAddDTO>({
  namespace: '',
  group: '',
  type: TaskType.DISPOSABLE
})

const taskDraw = reactive({
  show: false,
  closed() {
    taskFormData.value = {
      namespace: '',
      group: '',
      type: TaskType.DISPOSABLE
    }
  }
})

const namespace = ref<NamespaceVO>()
const searchParam = ref<TaskPageDTO>({
  namespace: '',
  size: 20,
  current: 1
})
const pageData = ref<Page<TaskVO>>({
  records: [],
  total: 0
})

watch(
  () => namespace.value?.name,
  (ns) => {
    reload({ current: 1, size: 20 })
    taskFormData.value.namespace = ns!
  }
)

const reload = (param: PageParam) => {
  const namespaceName = namespace.value?.name
  if (!namespaceName) {
    return
  }
  searchParam.value = { namespace: namespaceName, ...param }
  page(searchParam.value).then((result) => {
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

const showAddTask = () => {
  taskDraw.show = true
}
</script>

<template>
  <table-page :page="pageData" @reload="reload">
    <template #search>
      <namespace-select v-model="namespace" style="width: 20%" />
      <div style="width: 100%; padding: 5px 0">
        <el-button size="small" type="primary" @click="showAddTask">新增</el-button>
      </div>
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
  <el-drawer
    v-model="taskDraw.show"
    direction="rtl"
    size="50%"
    title="新增任务"
    @closed="taskDraw.closed"
  >
    <task-form v-model="taskFormData" />
  </el-drawer>
</template>

<style lang="scss" scoped>
.notice {
  color: var(--el-color-warning);
  font-size: var(--el-font-size-small);
}
</style>
