<script lang="ts" setup>
import HeaderTools from './header-tools/index.vue'
import OwlIcon from '../tools/OwlIcon.vue'
</script>

<template>
  <div class="header">
    <div class="header-logo">
      <el-icon size="50px">
        <owl-icon name="logo-logo" />
      </el-icon>
    </div>
    <div class="header-tools">
      <header-tools />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.header {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: space-between; /* 左右对齐 */
  align-items: center; /* 垂直居中 */
  .header-logo {
    display: flex;
  }

  .header-tools {
    display: flex;
    height: 50px;
    align-items: center;
  }
}
</style>
