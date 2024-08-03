<script lang="ts" setup>
import { type PropType } from 'vue'

type PropFunc = (item: any) => string

defineProps({
  page: {
    type: Object as PropType<Page<any>>,
    required: true
  },
  val: {
    type: [String, Function] as PropType<string | PropFunc>,
    required: false
  },
  label: {
    type: [String, Function] as PropType<string | PropFunc>,
    required: false
  }
})

const selected = defineModel<any>({
  default: () => null
})

const current = defineModel('current', {
  type: Number,
  default: 1
})

const propBind = (prop: string | PropFunc | undefined, item: any) => {
  if (!prop) {
    return item
  }
  if (typeof prop === 'string') {
    return item[prop]
  }
  return prop(item)
}
</script>

<template>
  <el-select v-model="selected" fit-input-width>
    <el-option
      v-for="(item, index) in page.records"
      :key="index"
      :label="propBind(label, item)"
      :value="propBind(val, item)"
    />
    <template #footer>
      <el-pagination
        v-model:current-page="current"
        :total="page.total"
        hide-on-single-page
        layout="prev, pager, next"
      />
    </template>
  </el-select>
</template>

<style lang="scss" scoped></style>
