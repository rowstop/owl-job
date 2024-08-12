<script lang="ts" setup>
import { ArrowDown } from '@element-plus/icons-vue'
import { useSecurityStore } from '@/stores/security'
import router from '@/router'

const security = useSecurityStore()
const loginOut = () => {
  security.token.value = ''
  router.push('/login').catch((e) => console.error('路由跳转失败', e))
}
</script>

<template>
  <el-link v-show="security.authed">
    <el-dropdown :hide-on-click="false">
      <el-button :icon="ArrowDown" size="small" text type="primary">0_0</el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :command="''">
            <el-popconfirm title="sure?" @confirm="loginOut">
              <template #reference>{{ $t('common.signOut') }}</template>
            </el-popconfirm>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-link>
</template>

<style lang="scss" scoped></style>
