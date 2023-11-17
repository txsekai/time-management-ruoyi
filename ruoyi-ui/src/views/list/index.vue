<template>
  <div class="app-container">
    <el-container>
      <el-main class="section-main">
        <!--          TODO 加过滤，如用标签过滤-->
        <div class="container grid">
          <el-card class="card">
            <div class="list-title">TO DO</div>
            <div class="list-details">
              <task-list />
            </div>
          </el-card>

          <el-card class="card">
          </el-card>

          <el-card class="card">

          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>

import TaskList from "./components/taskList.vue";

export default {
  name: 'List',
  components: {TaskList},

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
