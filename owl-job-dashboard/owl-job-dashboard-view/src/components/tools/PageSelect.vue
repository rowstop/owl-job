<script lang="ts" setup>
import { type PropType, ref } from 'vue'

type LabelFunc = (item: any) => string

const props = defineProps({
  page: {
    type: Object as PropType<Page<any>>,
    required: true
  },
  label: {
    type: [String, Function] as PropType<string | LabelFunc>,
    required: true
  }
})

const selectedItem = defineModel({
  type: Object,
  default: () => {}
})

const current = defineModel('current', {
  type: Number,
  default: 1
})

const selected = ref({})

const optionLabel = (item: any) => {
  const label = props.label
  if (typeof label === 'string') {
    return item[label]
  }
  return label(item)
}

const valueChange = (val: any) => {
  selectedItem.value = val
}
</script>

<template>
  <el-select v-model="selected" fit-input-width @change="valueChange">
    <el-option
      v-for="(item, index) in page.records"
      :key="index"
      :label="optionLabel(item)"
      :value="item"
    />
    <template #footer>
      <el-pagination
        v-model:current-page="current"
        :total="page.total"
        layout="prev, pager, next"
      />
    </template>
  </el-select>
</template>

<style lang="scss" scoped></style>
