<script lang="ts" setup>
import OwlMenu from '@/components/container/OwlMenu.vue'
</script>

<template>
  <el-container class="owl-container">
    <el-aside width="auto">
      <el-scrollbar>
        <owl-menu />
      </el-scrollbar>
    </el-aside>
    <el-main>
      <el-scrollbar>
        <router-view />
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.el-container {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

.el-aside {
  border-right: 1px solid var(--el-menu-border-color);
}

.el-main {
  padding: 0;
}
</style>
