<script lang="ts" setup>
import { type PropType } from 'vue'
import type { MenuItemInfo } from '../types/menu'

defineProps({
  menus: {
    type: Object as PropType<MenuItemInfo[]>
  }
})
</script>

<template>
  <template v-for="menu in menus" :key="menu.index">
    <el-sub-menu v-if="menu.children.length" :index="menu.path">
      <template #title>
        <el-icon>
          <component :is="menu.icon" />
        </el-icon>
        <span>{{ $t(menu.title) }}</span>
      </template>
      <menu-item :menus="menu.children" />
    </el-sub-menu>
    <el-menu-item v-else :index="menu.path">
      <el-icon>
        <component :is="menu.icon" />
      </el-icon>
      <template #title>
        <span>{{ $t(menu.title) }}</span>
      </template>
    </el-menu-item>
  </template>
</template>

<style lang="scss" scoped></style>
