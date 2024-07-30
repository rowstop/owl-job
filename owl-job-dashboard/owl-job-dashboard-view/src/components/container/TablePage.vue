<script lang="ts" setup>
import { defineEmits, defineProps, type PropType, useSlots } from 'vue'
import Page from '@/components/container/Page.vue'
import Pagination from '@/components/tools/Pagination.vue'

defineProps({
  page: {
    type: Object as PropType<Page<any>>,
    required: true
  }
})
const slots = useSlots()
const emits = defineEmits(['reload'])
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
      <pagination :total="page?.total" @reload="(curParam) => emits('reload', curParam)" />
    </template>
  </page>
</template>

<style scoped></style>
