<script lang="ts" setup>
import { add, type TaskAddDTO, TaskType } from '@/api/task'
import { GroupSelect } from '@/components/task'
import { type PropType, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { GroupVO } from '@/api/namespace/model'

defineProps({
  newTask: {
    type: Boolean,
    default: true
  }
})
const formRef = ref<FormInstance>()
const taskFormData = defineModel({
  required: true,
  type: Object as PropType<TaskAddDTO>
})

const taskRules = reactive<FormRules<TaskAddDTO>>({
  group: [{ required: true, message: '分组不能为空', trigger: 'blur' }],
  execTime: [{ required: true, message: '首次执行时间不能为空', trigger: 'blur' }],
  cron: [{ required: true, message: 'cron 表达式不能为空', trigger: 'blur' }],
  fixedRateSeconds: [{ required: true, message: '固定频率不能为空', trigger: 'blur' }]
})

const disableDate = (date: Date) => {
  return new Date().setDate(new Date().getDate() - 1) > date.getTime()
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return
  }
  await formEl.validate((valid) => {
    if (valid) {
      add(taskFormData.value).then((result) => {
        console.log(result)
      })
    }
  })
}
</script>

<template>
  <el-form ref="formRef" :model="taskFormData" :rules="taskRules" label-width="auto">
    <el-form-item label="命名空间">
      <span>{{ taskFormData.namespace }}</span>
    </el-form-item>
    <el-form-item label="任务组" prop="group">
      <group-select
        v-if="newTask"
        :namespace="taskFormData.namespace"
        @update:model-value="(group: GroupVO) => (taskFormData.group = group.name)"
      />
      <span v-else>
        {{ taskFormData.group }}
      </span>
    </el-form-item>
    <el-form-item label="任务类型" prop="type">
      <el-select v-model="taskFormData.type">
        <el-option v-for="type in TaskType" :key="type" :label="type" :value="type" />
      </el-select>
    </el-form-item>
    <el-form-item label="任务 id" prop="taskId">
      <el-input v-model="taskFormData.taskId" />
    </el-form-item>
    <el-form-item v-if="TaskType.CRON !== taskFormData.type" label="首次执行时间" prop="execTime">
      <el-date-picker
        v-model="taskFormData.execTime"
        :default-value="new Date()"
        :disabled-date="disableDate"
        format="YYYY-MM-DD HH:mm:ss"
        type="datetime"
        value-format="YYYY-MM-DD HH:mm:ss"
      />
    </el-form-item>
    <el-form-item v-if="TaskType.CRON == taskFormData.type" label="cron" prop="cron">
      <el-input v-model="taskFormData.cron" :rows="2" type="textarea" />
    </el-form-item>
    <el-form-item
      v-if="TaskType.FIXED_RATE == taskFormData.type"
      label="固定频率"
      prop="fixedRateSeconds"
    >
      <el-input-number
        v-model="taskFormData.fixedRateSeconds"
        :min="1"
        :precision="0"
        :rows="2"
        :step="3600"
      />
      <p class="notice">单位：秒，首次执行之后，以此固定频率循环执行</p>
    </el-form-item>
    <el-form-item label="任务参数" prop="param">
      <el-input v-model="taskFormData.param" rows="5" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(formRef)"> 保存</el-button>
    </el-form-item>
  </el-form>
</template>

<style lang="scss" scoped></style>
