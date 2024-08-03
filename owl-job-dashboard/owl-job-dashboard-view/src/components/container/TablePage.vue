<script lang="ts" setup>
import { type PropType, useSlots, watch } from 'vue'
import Page from '@/components/container/Page.vue'
import Pagination from '@/components/tools/Pagination.vue'

defineProps({
  page: {
    type: Object as PropType<Page<any>>,
    required: true
  }
})
const slots = useSlots()

const emits = defineEmits(['loadData'])

const reload = defineModel({
  type: Object as PropType<PageReload>,
  default() {
    return {
      current: 1,
      reload: false
    }
  }
})

watch(
  () => reload.value.current,
  () => console.log('table page reload:' + reload.value.current)
)
</script>

<template>
  <page>
    <template v-if="slots.search" #search>
      <slot name="search" />
    </template>
    <template v-slot="data">
      <el-table :data="page?.records" :height="data.height" stripe>
        <slot />
      </el-table>
    </template>
    <template #footer>
      <pagination
        v-model="reload"
        :total="page?.total"
        @load-data="(curParam) => emits('loadData', curParam)"
      />
    </template>
  </page>
</template>

<style scoped></style>
