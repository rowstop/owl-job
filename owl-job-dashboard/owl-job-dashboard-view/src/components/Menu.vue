<script lang="ts" setup>
import { Document, Expand, Fold, Menu as IconMenu } from '@element-plus/icons-vue'
import { ref } from 'vue'

const collapse = ref(false)
const handleOpen = (key: string, keyPath: string[]) => {
  console.log('open')
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log('close')
  console.log(key, keyPath)
}
</script>

<template>
  <el-menu
    :collapse="collapse"
    class="el-menu-vertical-demo"
    default-active="/"
    router
    @close="handleClose"
    @open="handleOpen"
  >
    <el-button
      :icon="collapse ? Expand : Fold"
      class="collapse-button"
      size="large"
      style="width: 100%"
      text
      @click="collapse = !collapse"
    />
    <el-menu-item index="/">
      <el-icon>
        <icon-menu />
      </el-icon>
      <template #title>看板</template>
    </el-menu-item>
    <el-menu-item index="/namespace">
      <el-icon>
        <document />
      </el-icon>
      <template #title>命名空间</template>
    </el-menu-item>
  </el-menu>
</template>

<style lang="scss" scoped>
.collapse-button {
  color: $owl-color-primary;
  width: 100%;
}
</style>
