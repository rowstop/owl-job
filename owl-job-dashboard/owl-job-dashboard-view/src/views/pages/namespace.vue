<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { page } from '@/api/namespace'

import type { NamespaceVO } from '@/api/namespace/model'

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
  <div>1111</div>
</template>

<style scoped></style>
