import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
  return request({
    url: '/activiti/task/list',
    method: 'get',
    params: query
  })
}

// 获取表单数据
export function formDataShow(taskId) {
  return request({
    url: '/activiti/task/formDataShow/' + taskId,
    method: 'get'
  })
}

// 保存表单数据
export function formDataSave(data) {
  return request({
    url: '/activiti/task/formDataSave',
    method: 'post',
    data: data
  })
}