<script lang="ts" setup>
import OwlIcon from '../../icon/OwlIcon.vue'

defineProps({
  title: {
    required: false,
    type: String
  },
  icon: {
    type: String
  },
  link: {
    type: String
  },
  size: {
    type: String,
    default: () => '25px'
  }
})
</script>

<template>
  <el-link :href="link" :title="title" class="outside-linked" target="_blank">
    <template #icon>
      <el-icon :size="size">
        <owl-icon :name="icon" />
      </el-icon>
    </template>
  </el-link>
</template>

<style lang="scss" scoped>
.outside-linked {
}
</style>
