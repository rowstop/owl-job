<script lang="ts" setup>
import { SupportLang } from '@/util/i18n/model'
import { useI18nStore } from '@/stores/i18n'
import OwlIcon from '../../tools/OwlIcon.vue'

const langDesc: Record<SupportLang, string> = {
  [SupportLang.en]: 'English',
  [SupportLang.zh]: '中文'
}

const i18n = useI18nStore()

/**
 *
 * @param command
 */
const updateLocale = (command: SupportLang) => {
  i18n.lang = command
}
</script>

<template>
  <el-link>
    <el-dropdown @command="updateLocale">
      <span>
        <el-icon class="locale" size="22px">
          <owl-icon name="tool-locale" />
        </el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="key in Object.values(SupportLang)"
            :key="key"
            :command="key"
            :disabled="i18n.lang == key"
          >
            {{ langDesc[key] }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-link>
</template>

<style lang="scss" scoped>
.locale {
  //color: $owl-color-primary;
}
</style>
