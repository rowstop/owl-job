import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/owl/',
  plugins: [
    vue(),
    // ...
    AutoImport({
      resolvers: [
        ElementPlusResolver({
          importStyle: 'sass'
        })
      ]
    }),
    Components({
      resolvers: [
        ElementPlusResolver({
          importStyle: 'sass'
        })
      ]
    }),
    //svg icon
    createSvgIconsPlugin({
      // 指定需要缓存的图标文件夹
      iconDirs: [fileURLToPath(new URL('./src/assets/svg', import.meta.url))],
      // 指定symbolId格式
      symbolId: 'OwlIcon-[dir]-[name]',
      inject: 'body-last'
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      owl: fileURLToPath(new URL('./src/util/owl', import.meta.url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/css/global.scss" as *;`
      }
    }
  }
})
