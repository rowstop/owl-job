<script lang="ts" setup>
import { type PropType, ref, watch } from 'vue'
import type { GroupVO, NamespaceVO } from '@/api/namespace/model'
import { groupPage } from '@/api/namespace'
import PageSelect from '@/components/tools/PageSelect.vue'

const props = defineProps({
  namespace: {
    type: String,
    required: true
  }
})

const group = defineModel({
  type: Object as PropType<GroupVO>,
  required: false
})
const current = ref(1)
const pageData = ref<Page<GroupVO>>({
  records: [],
  total: 0
})

watch(
  () => current.value,
  (cur) => reload({ size: 10, current: cur }),
  { immediate: true }
)

function reload(pageParam: PageParam) {
  groupPage({ ...pageParam, namespace: props.namespace }).then((result) => {
    if (!result.success) {
      return
    }
    const groupPage = result.data
    pageData.value = groupPage
    if (groupPage.records.length) {
      group.value = groupPage.records[0]
    }
  })
}
</script>

<template>
  <PageSelect
    v-model="group"
    v-model:current="current"
    :label="(item: NamespaceVO) => item.name"
    :page="pageData"
  />
</template>

<style lang="scss" scoped></style>
