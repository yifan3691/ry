import request from '@/utils/request'

// 查询ods列表
export function listOds(query) {
  return request({
    url: '/ods/ods/list',
    method: 'get',
    params: query
  })
}

// 查询ods详细
export function getOds(id) {
  return request({
    url: '/ods/ods/' + id,
    method: 'get'
  })
}

// 新增ods
export function addOds(data) {
  return request({
    url: '/ods/ods',
    method: 'post',
    data: data
  })
}

// 修改ods
export function updateOds(data) {
  return request({
    url: '/ods/ods',
    method: 'put',
    data: data
  })
}

// 删除ods
export function delOds(ids) {
  return request({
    url: '/ods/ods/delete/' + ids,
    method: 'post',
  })
}

// 查询sqlGrop列表
export function sqlGropList() {
  return request({
    url: '/ods/ods/sqlGropList',
    method: 'get',
  })
}

// 查询sqlGroupCode列表
export function sqlGroupCodeList() {
  return request({
    url: '/ods/ods/sqlGroupCodeList',
    method: 'get',
  })
}

// 查询procedureGroup列表
export function procedureGroupList() {
  return request({
    url: '/ods/ods/procedureGroupList',
    method: 'get',
  })
}
