<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { reactive, ref, watch } from 'vue'
import { NamespaceSelect, TaskForm } from '@/components/task'
import { ArrowDown } from '@element-plus/icons-vue'
import {
  del,
  page,
  type TaskAddDTO,
  TaskDesc,
  type TaskPageDTO,
  TaskType,
  type TaskVO
} from '@/api/task'
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
const isNewTask = ref(true)

const taskDraw = reactive({
  show: false,
  closed() {
    taskFormData.value = {
      namespace: taskFormData.value.namespace,
      group: taskFormData.value.group,
      type: TaskType.DISPOSABLE
    }
  }
})

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
  () => taskFormData.value.namespace,
  () => {
    reload.value = {
      current: 1,
      reload: true
    }
  }
)

const reload = ref<PageReload>({
  current: 1,
  reload: false
})

const loadData = (param: PageParam) => {
  const namespaceName = taskFormData.value.namespace
  if (!namespaceName) {
    return
  }
  searchParam.value = { namespace: namespaceName, ...param }
  page(searchParam.value).then((result) => {
    if (result.success) pageData.value = result.data
  })
}

const operate = (type: OpType | '', row: TaskVO) => {
  if (type === '') {
    return
  }
  if (type === OpType.EDIT) {
    return
  }
  del({ namespace: taskFormData.value.namespace, group: row.group, taskId: row.taskId }).then(
    (result) => {
      if (result.success) {
        ElMessage.success('ok')
        reload.value.reload = true
      }
    }
  )
}

const showAddTask = () => {
  taskDraw.show = true
  isNewTask.value = true
}

const taskFormSaved = () => {
  taskDraw.show = false
  reload.value.reload = true
}
</script>

<template>
  <table-page v-model="reload" :page="pageData" @load-data="loadData">
    <template #search>
      <el-form inline label-width="auto">
        <el-form-item label="命名空间">
          <namespace-select v-model="taskFormData.namespace" style="width: 200px" />
        </el-form-item>
      </el-form>
      <div>
        <el-button v-show="taskFormData.namespace" size="small" type="primary" @click="showAddTask"
          >{{ $t('page.task.form.newButton') }}
        </el-button>
      </div>
    </template>
    <el-table-column :label="$t('page.task.form.type')">
      <template #default="{ row }: { row: TaskVO }">
        {{ $t(TaskDesc[row.type]) }}
      </template>
    </el-table-column>
    <el-table-column :label="$t('page.task.form.execTime')" prop="nextTime" />
    <el-table-column :label="$t('page.task.form.param')" prop="param" show-overflow-tooltip />
    <el-table-column :label="$t('page.task.form.operate')">
      <template #default="{ row }: { row: TaskVO }">
        <el-dropdown :hide-on-click="false" @command="(type: OpType) => operate(type, row)">
          <el-button :icon="ArrowDown" size="small" type="primary"
            >{{ $t('page.task.form.operate') }}
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :command="OpType.EDIT" disabled
                >{{ $t('page.task.form.edit') }}
              </el-dropdown-item>
              <el-dropdown-item :command="''">
                <el-popconfirm
                  :title="$t('page.task.form.delConfirm')"
                  @confirm="operate(OpType.DELETE, row)"
                >
                  <template #reference>
                    {{ $t('page.task.form.del') }}
                  </template>
                </el-popconfirm>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </el-table-column>
  </table-page>
  <el-drawer
    v-model="taskDraw.show"
    :title="$t('page.task.form.newTitle', { namespace: taskFormData.namespace })"
    direction="rtl"
    size="50%"
    @closed="taskDraw.closed"
  >
    <task-form v-model="taskFormData" @saved="taskFormSaved" />
  </el-drawer>
</template>

<style lang="scss" scoped>
.notice {
  color: var(--el-color-warning);
  font-size: var(--el-font-size-small);
}
</style>
