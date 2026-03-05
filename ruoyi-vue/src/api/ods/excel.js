import request from '@/utils/request'

// 查询Excel上传表列表
export function listExcelTable(query) {
  return request({
    url: '/ods/excel/list',
    method: 'get',
    params: query
  })
}

// 查询Excel上传表详细
export function getExcelTable(tableId) {
  return request({
    url: '/ods/excel/' + tableId,
    method: 'get'
  })
}

// 上传Excel文件
export function uploadExcel(data) {
  return request({
    url: '/ods/excel/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 修改Excel上传表
export function updateExcelTable(data) {
  return request({
    url: '/ods/excel',
    method: 'put',
    data: data
  })
}

// 删除Excel上传表
export function delExcelTable(tableId) {
  return request({
    url: '/ods/excel/' + tableId,
    method: 'delete'
  })
}

// 获取表数据（分页）
export function getTableData(tableId, query) {
  return request({
    url: '/ods/excel/data/' + tableId,
    method: 'get',
    params: query
  })
}
