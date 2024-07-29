<script lang="ts" setup>
import { useI18nStore } from '@/stores/i18n'
import { ref, watch } from 'vue'

import zh from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import { SupportLang } from '@/util/i18n/model'
import OwlHeader from '@/components/container/OwlHeader.vue'

const i18n = useI18nStore()
const locale = ref(zh)
watch(
  () => i18n.lang,
  (newLang) => {
    locale.value = SupportLang.zh == newLang ? zh : en
  },
  { immediate: true }
)
</script>

<template>
  <el-config-provider :locale="locale">
    <el-container>
      <el-header>
        <owl-header />
      </el-header>
      <router-view />
    </el-container>
  </el-config-provider>
</template>

<style lang="scss" scoped>
.el-container {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

.el-header {
  border-bottom: 1px solid var(--el-menu-border-color);
}
</style>
