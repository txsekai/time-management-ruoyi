import request from "../../utils/request";

export function listTags() {
  return request({
    url: 'taskList/tag/list',
    method: 'get'
  })
}

export function addTag(data) {
  return request({
    url: 'taskList/tag/addTag',
    method: 'post',
    data: data
  })
}
