<script lang="ts" setup>
import TablePage from '@/components/container/TablePage.vue'
import { ref } from 'vue'

const pageData = ref<Page<any>>({
  records: [],
  total: 0
})

const reload = () => {}
</script>

<template>
  <table-page :page="pageData" @reload="reload">
    <el-table-column label="任务运行模式" />
    <el-table-column label="下次运行时间" />
    <el-table-column label="任务参数" />
    <el-table-column label="操作" />
  </table-page>
</template>

<style lang="scss" scoped></style>
