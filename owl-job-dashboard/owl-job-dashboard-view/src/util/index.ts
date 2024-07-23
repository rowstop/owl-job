/**
 * 防抖函数
 * @param fn 原函数
 * @param delay 函数防抖时间
 */
export const debounce = <A extends any[]>(
  fn: (...args: A) => void,
  delay: number = 200
): ((...args: A) => void) => {
  let timer: ReturnType<typeof setTimeout> | null
  return (...args: A) => {
    timer && clearTimeout(timer)
    timer = setTimeout(() => {
      fn(...args)
    }, delay)
  }
}

/**
 * 限流
 * @param fn
 * @param delay
 */
export const throttle = <A extends any[], R>(
  fn: (...args: A) => R,
  delay: number = 500
): ((...args: A) => null | R) => {
  let timer: ReturnType<typeof setTimeout> | undefined
  return (...args: A) => {
    if (timer) {
      return null
    }
    timer = setTimeout(() => {
      if (!timer) {
        clearTimeout(timer)
      }
      timer = undefined
    }, delay)
    return fn(...args)
  }
}
