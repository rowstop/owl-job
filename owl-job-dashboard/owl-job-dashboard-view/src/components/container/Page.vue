<script lang="ts" setup>
import { computed, onMounted, type Ref, ref, useSlots } from 'vue'
import { useResizeObserver } from '@vueuse/core'

const slots = useSlots()
const hashSearch = !!slots.search
const hashFooter = !!slots.footer
const container = ref()
const search = ref()
const footer = ref()

const containerHeight = ref(0)
const searchHeight = ref(0)
const footerHeight = ref(0)

const mainHeight = computed(() => {
  console.log('containerHeight=' + containerHeight.value)
  console.log('searchHeight=' + searchHeight.value)
  console.log('footerHeight=' + footerHeight.value)
  console.log(containerHeight.value - searchHeight.value - footerHeight.value)
  return containerHeight.value - searchHeight.value - footerHeight.value
})
//计算 container 及相关组件高度
onMounted(() => {
  const dispatch: Ref<any>[] = []
  const observerTargets = [container]
  dispatch.push(containerHeight)
  if (hashSearch) {
    observerTargets.push(search)
    dispatch.push(searchHeight)
  }
  if (hashFooter) {
    observerTargets.push(footer)
    dispatch.push(footerHeight)
  }
  useResizeObserver(observerTargets, (entries) => {
    console.log(entries)
    for (let order = 0; order < entries.length; order++) {
      dispatch[order].value = entries[order].contentRect.height
    }
  })
})
</script>

<template>
  <el-container ref="container">
    <div v-if="hashSearch" ref="search">
      <slot name="search" />
    </div>
    <el-main :style="{ height: mainHeight + 'px' }">
      <el-scrollbar>
        <slot :height="mainHeight" />
      </el-scrollbar>
    </el-main>
    <el-footer v-if="hashFooter" ref="footer" class="base-box footer">
      <slot name="footer" />
    </el-footer>
  </el-container>
</template>

<style lang="scss" scoped>
.el-container {
  padding: 8px;
  margin: 0;
  height: 100%;
  width: 100%;

  .el-main {
    height: 100%;
    padding: 0;
    margin: 0;
  }

  .base-box {
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: row;
  }

  .footer {
    justify-content: flex-end;
  }
}
</style>
