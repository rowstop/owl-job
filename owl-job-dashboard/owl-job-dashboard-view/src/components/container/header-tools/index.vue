<script lang="ts" setup>
import I18nTool from './I18nTool.vue'
import OutsideLink from './OutsideLink.vue'
import DarkTool from './DarkTool.vue'
</script>

<template>
  <div class="tools">
    <dark-tool />
    <i18n-tool />
    <outside-link icon="logo-github" link="https://github.com/rowstop/owl-job" title="github" />
    <outside-link
      icon="logo-gitee"
      link="https://gitee.com/rowstop/owl-job"
      size="22px"
      title="码云"
    />
  </div>
</template>

<style lang="scss" scoped>
.tools {
  display: flex;
  flex-direction: row;
  gap: 10px;
}
</style>
