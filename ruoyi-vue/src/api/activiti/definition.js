import request from '@/utils/request'

// 查询流程定义列表
export function listDefinition(query) {
  return request({
    url: '/activiti/definition/list',
    method: 'get',
    params: query
  })
}

// 删除流程定义
export function delDefinition(deploymentId) {
  return request({
    url: '/activiti/definition/' + deploymentId,
    method: 'delete'
  })
}

// 挂起或激活流程定义
export function suspendOrActiveApply(data) {
  return request({
    url: '/activiti/definition/suspendOrActiveApply',
    method: 'put',
    data: data
  })
}

// 导出流程定义
export function exportDefinition(query) {
  return request({
    url: '/activiti/definition/export',
    method: 'get',
    params: query
  })
}