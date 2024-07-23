import type { Component } from 'vue'

export interface MenuItemInfo {
  title: string
  path: string
  icon: Component
  children: MenuItemInfo[]
}
