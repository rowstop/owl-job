export interface NamespaceVO {
  name: string
  time: string
  groupCount: number
  curTaskCount: number
}

export interface GroupPageDTO extends PageParam {
  namespace: string
}

export interface GroupVO {
  name: string
  // curTaskCount: number
}
