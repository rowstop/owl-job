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
  persist: false
})

function storeSetup() {
  //owl-job-token:
  // owl-job-token=d0c60b3e-1763-498b-9392-2acdedb8e879
  const token = ref<Token>({
    name: 'owl-job-token',
    value: 'd0c60b3e-1763-498b-9392-2acdedb8e879'
  })
  const authed = computed(() => !!(token.value.name && token.value.value))
  return {
    token,
    authed
  }
}
