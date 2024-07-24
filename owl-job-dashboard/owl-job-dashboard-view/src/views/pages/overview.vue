<script lang="ts" setup>
import PageContainer from '@/components/tools/PageContainer.vue'
</script>

<template>
  <page-container>
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
  </page-container>
</template>

<style scoped></style>
