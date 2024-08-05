export interface OverviewDTO {
  namespace?: string
  start?: string
  end?: string
}

export interface DateCount {
  date: string
  error: number
  success: number
}

export interface OverviewVO {
  error: number
  success: number
  counts: DateCount[]
}
