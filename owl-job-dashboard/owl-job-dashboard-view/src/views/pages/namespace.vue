<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { page } from '@/api/namespace'

import type { NamespaceVO } from '@/api/namespace/model'
import OwlTablePage from '@/components/tools/OwlTablePage.vue'

const pageParam = ref<PageParam>({
  page: 1,
  size: 20
})

const pageResult = ref<Page<NamespaceVO>>()

onMounted(() => pageInit())

const pageInit = () => {
  page(pageParam.value)
    .then((result) => (pageResult.value = result.data))
    .then(() => console.log(pageResult.value))
}
</script>

<template>
  <div>
    <p>1111</p>
    <owl-table-page />
  </div>
</template>

<style scoped></style>
