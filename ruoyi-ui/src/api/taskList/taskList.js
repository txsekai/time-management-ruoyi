import request from "../../utils/request";

export function listToDoTask(query) {
  return request({
    url: '/taskList/todo/list',
    method: 'get',
    params: query
  })
}

// export function listDoingTask(query) {
//   return request({
//     url: 'taskList/doing/list',
//     method: 'get',
//     params: query
//   })
// }
//
// export function listDoneTask(query) {
//   return request({
//     url: 'taskList/done/list',
//     method: 'get',
//     params: query
//   })
// }
