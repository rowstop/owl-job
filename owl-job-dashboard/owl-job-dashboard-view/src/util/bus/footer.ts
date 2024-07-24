import { useEventBus } from '@vueuse/core'

export const eventBus = useEventBus<string>('footer')
