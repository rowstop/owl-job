<script lang="ts" setup>
import { type PropType, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { GroupSelect } from '@/components/task'
import { add, type TaskAddDTO, TaskDesc, TaskType } from '@/api/task'
import i8n from '@/util/i18n'

defineProps({
  newTask: {
    type: Boolean,
    default: true
  }
})
const emits = defineEmits(['saved'])
const formRef = ref<FormInstance>()
const wrongCron = ref('')
const taskFormData = defineModel({
  required: true,
  type: Object as PropType<TaskAddDTO>
})

const taskRules = reactive<FormRules<TaskAddDTO>>({
  group: [
    { required: true, message: () => i8n.global.t('page.task.form.groupValid'), trigger: 'blur' }
  ],
  execTime: [
    { required: true, message: () => i8n.global.t('page.task.form.execTimeValid'), trigger: 'blur' }
  ],
  type: [{ required: true, message: '', trigger: 'blur' }],
  cron: [
    { required: true, message: () => i8n.global.t('page.task.form.cronValid'), trigger: 'blur' }
  ],
  fixedRateSeconds: [
    {
      required: true,
      message: () => i8n.global.t('page.task.form.fixedRateValid'),
      trigger: 'blur'
    }
  ]
})

const disableDate = (date: Date) => {
  return new Date().setDate(new Date().getDate() - 1) > date.getTime()
}
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return
  }
  wrongCron.value = ''
  await formEl.validate((valid) => {
    if (valid) {
      add(taskFormData.value).then((result) => {
        if (result.success) {
          emits('saved')
          return
        }
        const code = result.code
        if (code === 4) {
          wrongCron.value = i8n.global.t('page.task.form.wrongCronValid')
        }
      })
    }
  })
}
</script>

<template>
  <el-form ref="formRef" :model="taskFormData" :rules="taskRules" label-width="auto">
    <el-form-item :label="$t('page.task.form.namespace')">
      <span>{{ taskFormData.namespace }}</span>
    </el-form-item>
    <el-form-item :label="$t('page.task.form.group')" prop="group">
      <group-select
        v-if="newTask"
        v-model="taskFormData.group"
        :namespace="taskFormData.namespace"
      />
      <span v-else>
        {{ taskFormData.group }}
      </span>
    </el-form-item>
    <el-form-item :label="$t('page.task.form.type')" prop="type">
      <el-select v-model="taskFormData.type">
        <el-option v-for="type in TaskType" :key="type" :label="$t(TaskDesc[type])" :value="type" />
      </el-select>
    </el-form-item>
    <el-form-item :label="$t('page.task.form.taskId')" prop="taskId">
      <el-input v-model="taskFormData.taskId" />
    </el-form-item>
    <el-form-item
      v-if="TaskType.CRON !== taskFormData.type"
      :label="$t('page.task.form.execTime')"
      prop="execTime"
    >
      <el-date-picker
        v-model="taskFormData.execTime"
        :default-value="new Date()"
        :disabled-date="disableDate"
        format="YYYY-MM-DD HH:mm:ss"
        type="datetime"
        value-format="YYYY-MM-DD HH:mm:ss"
      />
    </el-form-item>
    <el-form-item
      v-if="TaskType.CRON == taskFormData.type"
      :error="wrongCron"
      :label="$t('page.task.form.cron')"
      prop="cron"
    >
      <el-input v-model="taskFormData.cron" :rows="2" type="textarea" />
    </el-form-item>
    <el-form-item
      v-if="TaskType.FIXED_RATE == taskFormData.type"
      :label="$t('page.task.form.fixedRate')"
      prop="fixedRateSeconds"
    >
      <el-input-number
        v-model="taskFormData.fixedRateSeconds"
        :min="1"
        :precision="0"
        :rows="2"
        :step="3600"
      />
      <p class="notice">{{ $t('page.task.form.fixedRateNotice') }}</p>
    </el-form-item>
    <el-form-item :label="$t('page.task.form.param')" prop="param">
      <el-input v-model="taskFormData.param" rows="5" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(formRef)">
        {{ $t('page.task.form.submitButton') }}
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style lang="scss" scoped></style>
