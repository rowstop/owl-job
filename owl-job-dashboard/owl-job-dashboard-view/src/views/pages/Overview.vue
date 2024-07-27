<script lang="ts" setup>
import Page from '@/components/container/Page.vue'
</script>

<template>
  <page>
    <template #search>
      <p>111</p>
      <p>111</p>
      <p>111</p>
    </template>
    <div class="flex flex-wrap gap-4">
      <el-card
        v-for="item in 200"
        :key="item"
        shadow="always"
        style="width: 480px; background-color: aquamarine"
      >
        {{ item }}Always
      </el-card>
    </div>
  </page>
</template>

<style scoped></style>
