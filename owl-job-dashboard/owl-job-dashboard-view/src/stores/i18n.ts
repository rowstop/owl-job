import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import i18n from '@/util/i18n'
import { SupportLang } from '@/util/i18n/model'

export const useI18nStore = defineStore(
  'i18n',
  () => {
    const lang = ref<SupportLang>(SupportLang.en)
    //监听 语言变化 同步设置系统语言
    watch(
      () => lang.value,
      (newLang) => {
        i18n.global.locale.value = newLang
      },
      { immediate: true }
    )
    //获取系统语言
    const systemLang = navigator.language?.split('-')[0].toLowerCase()
    if (systemLang && Object.values(SupportLang).includes(systemLang as SupportLang)) {
      lang.value = systemLang as SupportLang
    }
    return {
      lang
    }
  },
  {
    persist: true
  }
)
