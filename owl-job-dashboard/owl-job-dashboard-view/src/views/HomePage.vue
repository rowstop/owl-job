<script lang="ts" setup>
import Menu from '@/components/Menu.vue'
</script>

<template>
  <el-container style="">
    <el-header>Header</el-header>
    <el-container class="owl-container">
      <el-aside width="200px">
        <el-scrollbar>
          <Menu />
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar>
          <router-view />
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.el-container {
  border-top: 1px solid $owl-color-primary;
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

.el-main {
  padding: 0;
}
</style>
