/**
 * 用户认证权限
 */

import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

//model
interface Token {
  name: string
  value: string
}

export const useSecurityStore = defineStore('security', storeSetup, {
  persist: true
})

function storeSetup() {
  const token = ref<Token>({
    name: '',
    value: ''
  })
  const authed = computed(() => !!(token.value.name && token.value.value))
  return {
    token,
    authed
  }
}
