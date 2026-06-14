import request from '@/utils/request'

// 获取服务信息
export function getServer() {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}

// 获取CPU压测状态
export function getCpuStressStatus() {
  return request({
    url: '/monitor/server/stress',
    method: 'get'
  })
}

// 启动CPU压测
export function startCpuStress(data) {
  return request({
    url: '/monitor/server/stress',
    method: 'post',
    data: data
  })
}

// 停止CPU压测
export function stopCpuStress() {
  return request({
    url: '/monitor/server/stress',
    method: 'delete'
  })
}
