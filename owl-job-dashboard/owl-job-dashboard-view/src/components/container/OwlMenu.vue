<script lang="ts" setup>
import { Expand, Fold } from '@element-plus/icons-vue'
import { type Component, markRaw, ref, watch } from 'vue'
import router, { mainRoute } from '@/router'
import type { RouteRecordRaw } from 'vue-router'
import type { MenuItemInfo } from '../types/menu'
import MenuItem from '@/components/container/MenuItem.vue'

const menus = ref<MenuItemInfo[]>([])
const collapse = ref(false)
watch(
  () => router.getRoutes(),
  () => {
    menus.value = menuRender('/', mainRoute.children)
    console.log(menus.value)
  },
  {
    immediate: true
  }
)

function menuRender(menuFullPath: string, children?: RouteRecordRaw[]) {
  if (!children?.length) {
    return [] as MenuItemInfo[]
  }
  return children.map((child) => {
    const meta = child.meta
    const curMenuFullPath = menuFullPath + child.path
    const curMenu: MenuItemInfo = {
      title: meta?.title as string,
      path: curMenuFullPath,
      icon: markRaw(meta?.icon as Component),
      children: menuRender(curMenuFullPath, child.children)
    }
    return curMenu
  })
}

const handleCollapse = () => (collapse.value = !collapse.value)
</script>

<template>
  <el-menu
    :collapse="collapse"
    :default-active="$router.currentRoute.value.path"
    class="el-menu-vertical-demo"
    router
  >
    <el-button
      :icon="collapse ? Expand : Fold"
      class="collapse-button"
      size="large"
      style="width: 100%"
      text
      @click="handleCollapse"
    />
    <menu-item :menus="menus" />
  </el-menu>
</template>

<style lang="scss" scoped>
.el-menu-vertical-demo {
  border: none;

  .collapse-button {
    color: $owl-color-primary;
    width: 100%;
  }
}
</style>
