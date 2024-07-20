<script lang="ts" setup></script>

<template>
  <div class="flex flex-wrap gap-4">
    <el-card v-for="item in 200" shadow="always" style="width: 480px; background-color: aquamarine">
      {{ item }}Always
    </el-card>
  </div>
</template>

<style scoped></style>
