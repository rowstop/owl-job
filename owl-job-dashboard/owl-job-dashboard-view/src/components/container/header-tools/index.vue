<script lang="ts" setup>
import I18nTool from './I18nTool.vue'
import OutsideLink from './OutsideLink.vue'
import DarkTool from './DarkTool.vue'
import UserTool from './UserTool.vue'
</script>

<template>
  <div class="tools">
    <user-tool />
    <dark-tool />
    <i18n-tool />
    <outside-link icon="logo-github" link="https://github.com/rowstop/owl-job" title="GitHub" />
    <outside-link
      icon="logo-gitee"
      link="https://gitee.com/rowstop/owl-job"
      size="22px"
      title="Gitee"
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
