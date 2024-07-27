<script lang="ts" setup>
import { defineEmits, defineProps, onMounted, type PropType, ref, useSlots, watch } from 'vue'
import Page from '@/components/container/Page.vue'
import Pagination from '@/components/tools/Pagination.vue'

const props = defineProps({
  page: {
    type: Object as PropType<Page<any>>,
    required: true
  }
})
const slots = useSlots()
const emits = defineEmits(['reload'])

const pageParam = defineModel({
  type: Object as PropType<PageParam>,
  required: true,
  default: () => {
    return {
      current: 1,
      size: 20
    }
  }
})

const pageInfo = ref<PageInfo>({
  current: 1,
  size: 20,
  total: 0
})

onMounted(() => reload(pageParam.value))

watch(
  () => props.page.total,
  (total) => (pageInfo.value.total = total)
)

/**
 * 重新加载数据
 * @param param
 */
const reload = (param: PageParam) => {
  pageParam.value = { ...pageParam.value, ...param }
  emits('reload')
}
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
      <pagination v-model="pageInfo" @reload="reload" />
    </template>
  </page>
</template>

<style scoped></style>
