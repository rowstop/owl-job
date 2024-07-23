<script lang="ts" setup>
import { computed } from 'vue'

const props = defineProps({
  prefix: {
    type: String,
    default: 'OwlIcon'
  },
  name: {
    type: String,
    required: true
  }
})
const symbolId = computed(() => `#${props.prefix}-${props.name}`)
</script>
<template>
  <svg aria-hidden="true">
    <use :xlink:href="symbolId" />
  </svg>
</template>
<style lang="scss" scoped></style>
