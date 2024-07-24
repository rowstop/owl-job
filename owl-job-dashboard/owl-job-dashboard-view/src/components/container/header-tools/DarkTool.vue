<script lang="ts" setup>
import { useDark, useToggle } from '@vueuse/core'
import { Moon, Sunny } from '@element-plus/icons-vue'

const isDark = useDark()
const toggleDark = useToggle(isDark)
</script>

<template>
  <el-switch
    v-model="isDark"
    :active-action-icon="Moon"
    :inactive-action-icon="Sunny"
    @change="toggleDark"
  />
</template>

<style lang="scss" scoped></style>
