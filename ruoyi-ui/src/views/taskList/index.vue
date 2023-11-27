<template>
  <div class="app-container">
        <el-form
          :model="queryParams"
          ref="queryParams"
          size="small"
          :inline="true"
        >
          <el-form-item label="任务名称">
            <el-input
              v-model="queryParams.taskName"
              placeholder="请输入任务名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            ></el-input>
          </el-form-item>
          <el-form-item label="标签名称">
            <el-input
              v-model="queryParams.tagName"
              placeholder="请输入标签名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            ></el-input>
          </el-form-item>
<!--          TODO 优先级做成下拉框-->
          <el-form-item label="任务优先级">
            <el-input
              v-model="queryParams.taskPriority"
              placeholder="请输入任务优先级"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="container grid">
          <el-card class="card">
            <div class="list-title">TO DO</div>
            <div class="list-details">
              <task-list-item :todo-list="todoList"/>
            </div>
          </el-card>

          <el-card class="card">
          </el-card>

          <el-card class="card">

          </el-card>
        </div>
  </div>
</template>

<script>

import TaskListItem from "./components/taskListItem.vue";
import {listDoingTask, listDoneTask, listToDoTask} from "../../api/taskList/taskList";

export default {
  name: 'List',
  components: {TaskListItem},

  data() {
    return {
      queryParams: {
        taskName: "",
        tagName: "",
        taskPriority: 0
      },
      todoList: [],
      doingList: {},
      doneList: {},
    }
  },

  created() {
    this.getList();
  },

  methods: {
    getList() {
      listToDoTask(this.queryParams).then(res => {
        this.todoList = res.data
      })
      // listDoingTask(this.queryParams).then(res => {
      //   this.doneList = res
      // })
      // listDoneTask(this.queryParams).then(res => {
      //   this.doneList = res
      // })
    },
    handleQuery(){
      this.getList();
    },
    resetQuery() {

    },
  },
}
</script>

<style scoped>
.grid {
  display: grid;
  column-gap: 5.6rem;
  row-gap: 1.6rem;

  grid-template-columns: repeat(3, 1fr);
}

.list-title {
  font-weight: bold;
  color: #cf711f;
  font-size: 1.4rem;
}

.list-details {
  margin: 0.8rem 0;
}
</style>
