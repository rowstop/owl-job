<script lang="ts" setup>
import { ref, watch } from 'vue'
import type { NamespaceVO } from '@/api/namespace/model'
import { page } from '@/api/namespace'
import PageSelect from '@/components/tools/PageSelect.vue'

const namespace = defineModel({
  type: String,
  default: () => ''
})
const current = ref(1)
const pageData = ref<Page<NamespaceVO>>({
  records: [],
  total: 0
})

watch(
  () => current.value,
  (cur) => reload({ size: 10, current: cur }),
  { immediate: true }
)

function reload(pageParam: PageParam) {
  page(pageParam).then((result) => {
    if (!result.success) {
      return
    }
    const namespacePage = result.data
    pageData.value = namespacePage
    if (namespacePage.records.length) {
      namespace.value = namespacePage.records[0].name
    }
  })
}
</script>

<template>
  <PageSelect
    v-model="namespace"
    v-model:current="current"
    :label="(item: NamespaceVO) => item.name"
    :page="pageData"
    :val="(item: NamespaceVO) => item.name"
  />
</template>

<style lang="scss" scoped></style>
