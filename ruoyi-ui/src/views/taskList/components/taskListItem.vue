<template>
  <div>
    <el-row v-for="(task, index) in taskList" :key="index">
      <div class="task">
        <input style="margin: 0.2rem" type="checkbox" v-model="task.complete"/>
        <div class="task-detail">
          <template v-if="!task.editing">
            <!--                        TODO 整个task的内容应该都可以点击-->
            <div class="edit-task" @click="startEditing(task, index)">{{ task.content }}</div>
            <el-row class="tag-row">
              <el-tag class="tag-group"
                      v-for="tag in task.tags"
                      :key="tag"
              >{{ tag }}
              </el-tag>
            </el-row>
            <el-row v-if="task.dateAndTime.startTime">
              <span>开始：{{ formatDate(task.dateAndTime.startTime) }}</span>
              <span v-if="task.dateAndTime.completedTime"> ~ </span>
              <span v-if="task.dateAndTime.completedTime">完成：{{
                  formatDate(task.dateAndTime.completedTime)
                }}</span>
            </el-row>

            <el-row v-html="formattedRepeatResult(task.localRepeatResult)"></el-row>

            <el-row v-if="task.selectedPriority!==''">
              <i v-for="starCount in task.selectedPriority" :key="starCount" class="el-icon-star-on"></i>
            </el-row>
          </template>
          <template v-else>
            <div class="input-and-settings">
              <el-input :id="'task_input_' + index" v-model="task.content" ref="taskInputs"
                        @blur="inputBlur(task, index)"
                        @keyup.native="handleShowSettings(task)"></el-input>
              <el-row v-if="showSettings" style="display: flex;align-items: center;">
                <el-tooltip content="标签" placement="bottom-start">
                  <el-button class="setting-icon" icon="el-icon-discount"
                             @click="openTagDialog(task)"></el-button>
                </el-tooltip>
                <el-tooltip content="日期、时间" placement="bottom-start">
                  <el-button class="setting-icon" icon="el-icon-time"
                             @click="openDateAndTimeDialog(task)"></el-button>
                </el-tooltip>

                <el-tooltip content="开始专注" placement="bottom-start">
                  <el-button class="setting-icon" icon="el-icon-timer"></el-button>
                </el-tooltip>

                <el-dropdown @command="handlePriorityCommand(task, $event)" class="priority-style">
                  <el-tooltip content="优先级" placement="bottom-start">
                    <el-button class="setting-icon" icon="el-icon-star-on"></el-button>
                  </el-tooltip>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="option in priorityOptions" :key="option.value"
                                      :command="option.value">{{ option.label }}
                      <i v-for="starCount in option.value" :key="starCount"
                         class="el-icon-star-on"></i>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-row>
            </div>
          </template>
        </div>
      </div>
    </el-row>

    <el-button class="add-list-button" @click="addTask">+ 添加事项</el-button>

    <tag-dialog :tag-dialog-visible="tagDialogVisible" :task="currentTask" @tagConfirm="tagDialogVisible=false"
                @tagCancel="tagDialogVisible=false;currentTask.tags=tagsBk"
                :tags-bk="tagsBk"></tag-dialog>

    <date-and-time-dialog :date-and-time-dialog-visible="dateAndTimeDialogVisible"
                          :task="currentTask"
                          @dateConfirm="dateAndTimeDialogVisible=false"
                          @dateCancel="dateAndTimeDialogVisible=false"
                          :date-and-time-bk="dateAndTimeBk"
                          @delete="dateAndTimeDialogVisible=false"
    ></date-and-time-dialog>


  </div>
</template>

<script>

import DateAndTimeDialog from "../dialogs/dateAndTimeDialog.vue";
import TagDialog from "../dialogs/tagDialog.vue";
import DateMixin from "../../../mixins/formatDate";
import RepeatMixin from "../../../mixins/formatRepeat";

export default {
  name: 'TaskListItem',
  components: {DateAndTimeDialog, TagDialog},
  mixins: [DateMixin, RepeatMixin],

  data() {
    return {
      taskList: [{
        complete: false,
        content: '123',
        editing: false,
        tags: [],
        dateAndTime: {startTime: null, completedTime: null},
        selectedPriority: '',
        localRepeatResult: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
      }],
      tagDialogVisible: false,
      currentTask: {
        content: '',
        tags: [],
        dateAndTime: {startTime: null, completedTime: null},
        localRepeatResult: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
      },
      tagsBk: [],

      priorityOptions: [
        {
          value: 3,
          label: '高'
        }, {
          value: 2,
          label: '中'
        }, {
          value: 1,
          label: '低'
        },
      ],

      dateAndTimeDialogVisible: false,
      dateAndTimeBk: {startTime: null, completedTime: null},
    }
  },

  computed: {
    showSettings() {
      return this.currentTask.content.length !== 0
    }
  },

  methods: {
    addTask() {
      const newTask = {
        complete: false,
        content: '',
        editing: true,
        tags: [],
        dateAndTime: {startTime: null, completedTime: null},
        selectedPriority: '',
        localRepeatResult: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
      }
      this.taskList.push(newTask);
      this.currentTask = newTask

      this.$nextTick(() => {
        const inputElements = this.$refs.taskInputs
        const lastIndex = inputElements.length - 1
        const lastInputElement = inputElements[lastIndex]
        lastInputElement.$refs.input.focus();
      })
    },
    startEditing(task, index) {
      task.editing = true

      this.currentTask = task

      this.$nextTick(() => {
        document.getElementById('task_input_' + index).focus()
      });
    },
    inputBlur(task, index) {
      if (task.content === '' || task.content === undefined) {
        this.taskList.splice(index, 1)
      } else {
        setTimeout(() => {
          task.editing = false
        }, 300)
      }
    },
    handleShowSettings(task) {
      this.currentTask = task
    },
    openTagDialog(task) {
      // TODO 定义共同openDialog的方法

      // TODO 在编辑时候已经赋值给currentTask了
      this.currentTask = task;
      this.tagsBk = Object.assign([], task.tags);
      this.tagDialogVisible = true
    },
    openDateAndTimeDialog(task) {
      this.dateAndTimeDialogVisible = true
      this.currentTask = task
      this.dateAndTimeBk = Object.assign({}, task.dateAndTime)
      // this.dateAndTimeBk = {
      //     startTime: new Date(task.dateAndTime.startTime.getTime()),
      //     completedTime: new Date(task.dateAndTime.completedTime.getTime())
      // }
    },
    handlePriorityCommand(task, command) {
      task.selectedPriority = command
    },
  },
}
</script>

<style scoped>
.el-row {
  margin: 0.6rem;
}

.task {
  display: flex;
  gap: 0.4rem;
  align-items: start;
}

.task-detail {
  font-size: 1.4rem;
  flex-grow: 1;
}

.edit-task {
  cursor: pointer;
}

.input-and-settings {
  display: flex;
  flex-direction: column;
}

.setting-icon {
  border: none;
  padding: 0 0;
  background: none;
}

.priority-style {
  margin-left: 1rem;
  display: flex;
}

.el-dropdown-menu {
  padding: 0.5rem 0;
  border-radius: 11px;
}

.el-dropdown-menu__item {
  line-height: 3rem;
}

.el-dropdown-menu__item, .el-menu-item {
  padding: 0 1rem;
}

.tag-row {
  margin: 0.6rem 0;
}

.tag-group {
  margin-left: 0.3rem;
  margin-bottom: 0.5rem;
  height: 2rem;
  line-height: 2rem;
}

.add-list-button {
  border: none;
  padding: 0.5rem 1rem;
  font-size: 1.4rem;
  color: #000;
}

.add-list-button:hover,
.add-list-button:active,
.add-list-button:focus {
  background-color: #fff;
}
</style>
