<template>
  <el-dialog
    title="请设置日期、时间"
    :visible.sync="dateAndTimeDialogVisible"
    width="30%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <!--        TODO 不要这么多row-->
    <el-row>
      <el-col :span="12">
        <span class="content-font-size">开始日期</span>
      </el-col>

      <el-col :span="12">
        <span class="content-font-size">完成日期 </span>
        <el-switch
          style="width: 3.6rem"
          v-model="completedDateVisible"
          active-color="#cf711f"
        ></el-switch>
      </el-col>
    </el-row>

    <el-row class="mt6">
      <el-col :span="12">
        <date-item v-model="startTime" :align="'left'"
                   :picker-options="pickerOptionsStartDate"></date-item>
      </el-col>

      <el-col :span="12" v-show="completedDateVisible">
        <date-item v-model="completedTime" :align="'right'"
                   :picker-options="pickerOptionsCompletedDate"></date-item>
      </el-col>
    </el-row>

    <el-row class="mt24">
      <el-col :span="12">
        <span class="content-font-size">开始时间 </span>
        <el-switch
          style="width: 3.6rem"
          v-model="startTimeVisible"
          active-color="#cf711f"
        ></el-switch>
      </el-col>

      <el-col :span="12">
        <span class="content-font-size">完成时间 </span>
        <el-switch
          style="width: 3.6rem"
          v-model="completedTimeVisible"
          active-color="#cf711f"
        ></el-switch>
      </el-col>
    </el-row>

    <el-row class="mt3">
      <el-col :span="6" v-show="startTimeVisible">
        <time-item v-model="startTime"></time-item>
      </el-col>

      <el-col v-show="startTimeVisible" :span="6" style="min-height: 1rem"></el-col>
      <el-col v-show="!startTimeVisible" :span="12" style="min-height: 1rem"></el-col>

      <el-col :span="6" v-show="completedTimeVisible">
        <time-item v-model="completedTime"></time-item>
      </el-col>
    </el-row>

    <el-row class="mt12">
      <el-col :span="24" style="min-height: 1rem">
        <el-col :span="12"><span class="content-font-size" v-if="completedTimeVisible">所用时长：{{ timeDiff }} 分钟</span>
        </el-col>

        <el-col :span="12"><span>{{ formattedTimeDiff }}</span></el-col>

      </el-col>
    </el-row>

    <div class="spend-time-grid mt12">
      <el-button v-for="t in timeSpentOptions" :key="t" class="spend-time-padding"
                 :disabled="timeSpentBtnDisabled"
                 @click="handlePlanTime(t)"
                 :class="{defaultSelectTimeStyle: (timeDiff == t)}"
      >{{ t }}分钟
      </el-button>

    </div>

    <el-row class="mt24">
      <el-button @click="handleOpenRepeatDialog">重复</el-button>
      <el-row class="mt8" v-html="formattedRepeatResult(repeat)"></el-row>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button v-show="this.task.dateAndTime.startTime!==null" class="button-padding"
                 @click="handleDateDelete">删除日程
      </el-button>
      <el-button class="button-padding" @click="handleDateConfirm">确认</el-button>
      <el-button class="button-padding" @click="handleDateCancel">取消</el-button>
    </div>

    <repeat-dialog :repeat-dialog-visible="repeatDialogVisible" @repeatConfirm="handleRepeatConfirm"
                   @repeatCancel="repeatDialogVisible=false"
                   :title="repeatDialogTitle"
                   :repeat-result="repeat"
    ></repeat-dialog>
  </el-dialog>
</template>

<script>

import RepeatDialog from "./repeatDialog.vue";
import DateItem from "../components/dateItem.vue";
import TimeItem from "../components/timeItem.vue";
import DateMixin from "../../../mixins/formatDate";
import RepeatMixin from "../../../mixins/formatRepeat";

export default {
  name: 'DateAndTimeDialog',
  components: {RepeatDialog, DateItem, TimeItem},
  mixins: [DateMixin, RepeatMixin],

  props: {
    dateAndTimeDialogVisible: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object,
      default: function () {
        return {
          dateAndTime: {startTime: null, completedTime: null},
          repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
        }
      }
    },
    dateAndTimeBk: {
      type: Object,
      default: function () {
        return {startTime: null, completedTime: null}
      }
    },
  },

  data() {
    return {
      completedDateVisible: false,

      startTimeVisible: false,
      startTime: new Date(),
      completedTime: new Date(),
      completedTimeVisible: false,

      timeSpentOptions: [10, 15, 20, 30, 40, 60, 90, 120],

      pickerOptionsStartDate: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }]
      },
      pickerOptionsCompletedDate: {
        disabledDate: time => {
          const startOfDay = new Date(time.getFullYear(), time.getMonth(), time.getDate()); // 获取传入时间的凌晨时间
          const copyOfStartTime = new Date(this.startTime.getTime());
          copyOfStartTime.setHours(0, 0, 0, 0);
          return startOfDay.getTime() < copyOfStartTime;
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }]
      },

      repeatDialogVisible: false,
      repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
    }
  },

  created() {
  },

  watch: {
    task: {
      handler(newTask) {
        if (newTask.dateAndTime.startTime == null) {
          this.startTimeVisible = false
          this.completedDateVisible = false
          this.completedTimeVisible = false

          this.startTime = new Date(new Date().setHours(0, 0, 0, 0));
        } else {
          if (newTask.dateAndTime.startTime.getHours() == 0 && newTask.dateAndTime.startTime.getMinutes() == 0) {
            this.startTimeVisible = false
            this.startTime = new Date(newTask.dateAndTime.startTime.setHours(0, 0, 0, 0))
          } else {
            this.startTimeVisible = true
            this.startTime = new Date(newTask.dateAndTime.startTime.setSeconds(0, 0))
          }
          if (newTask.dateAndTime.completedTime !== null) {

            if (newTask.dateAndTime.completedTime.getHours() == 23 && newTask.dateAndTime.completedTime.getMinutes() == 59) {
              this.completedDateVisible = true
              this.completedTimeVisible = false

              this.completedTime = new Date(newTask.dateAndTime.completedTime.setHours(23, 59, 59, 999))
            } else {
              this.completedDateVisible = true
              this.completedTimeVisible = true
              this.completedTime = new Date(newTask.dateAndTime.completedTime.setSeconds(0, 0))
            }
          } else {
            this.completedDateVisible = false
            this.completedTimeVisible = false
          }
        }

        this.repeat = newTask.repeat
      },
      deep: true
    },
    /*
    1. 源头确保时间选择关闭的时候dateTime对象的时分是00
    2. 编辑时候，判断父组件传过来的值来控制开关
     */
    startTime(newVal) {
      if (!this.completedDateVisible) {
        if (this.completedTimeVisible) {
          let copy = new Date(this.completedTime)
          this.$set(this, 'completedTime', new Date(newVal.getFullYear(), newVal.getMonth(), newVal.getDate(), copy.getHours(), copy.getMinutes()))
        } else {
          this.completedTime = null
        }
      }
    },
    startTimeVisible(newVal) {
      if (!newVal) {
        let copyStartTime = new Date(this.startTime)
        this.startTime = new Date(copyStartTime.setHours(0, 0, 0, 0))

        if (this.completedDateVisible) {
          let copyCompletedTime = new Date(this.completedTime)
          this.completedTime = new Date(copyCompletedTime.setHours(23, 59, 59, 999))
        } else {
          this.completedTime = null
        }
        this.completedTimeVisible = false
      } else {
        let copy = new Date()
        this.$set(this, 'startTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()))
      }
    },
    completedDateVisible(newVal) {
      if (newVal) {
        if (!this.completedTimeVisible) {
          let copy = new Date(this.startTime)
          this.completedTime = new Date(copy.setHours(23, 59, 59, 999))
        } else {
          let copy = new Date(this.completedTime)
          this.$set(this, 'completedTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()))
        }
      } else {
        if (this.completedTimeVisible) {
          let copy = new Date(this.completedTime)
          this.$set(this, 'completedTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()))
        } else {
          this.completedTime = null
        }
      }
    },
    completedTimeVisible(newVal) {
      if (newVal) {
        if (this.completedDateVisible) {
          let copy = new Date(this.completedTime);
          if (this.startTimeVisible) {
            copy.setHours(this.startTime.getHours(), this.startTime.getMinutes(), 0, 0);
          } else {
            const current = new Date()
            copy.setHours(current.getHours(), current.getMinutes(), 0, 0);
          }
          this.$set(this, 'completedTime', new Date(copy.getTime() + 30 * 60000))
        } else {
          if (this.startTimeVisible) {
            this.$set(this, 'completedTime', new Date(this.startTime.getTime() + 30 * 60000))
          } else {
            const current = new Date()
            this.$set(this, 'completedTime', new Date(current.getTime() + 30 * 60000))
          }
        }
        this.startTimeVisible = true;
      } else {
        if (this.completedDateVisible) {
          let copy = new Date(this.completedTime)
          this.completedTime = new Date(copy.setHours(23, 59, 59, 999))
        } else {
          this.completedTime = null
        }
      }
    },
  },

  computed: {
    timeDiff() {
      return this.completedTimeVisible ? (this.completedTime - this.startTime) / 60000 : ''
    },
    formattedTimeDiff() {
      const temp = this.timeDiff;
      const dayDiff = Math.floor(temp / 1440);
      const hourDiff = Math.floor((temp % 1440) / 60);
      const minDiff = temp % 60;

      if (temp <= 120) {
        return '';
      } else if (temp < 1440) {
        if (minDiff === 0) {
          return `${hourDiff}小时`;
        } else {
          return `${hourDiff}小时${minDiff}分钟`;
        }
      } else {
        if (hourDiff === 0) {
          if (minDiff === 0) {
            return `${dayDiff}天`;
          } else {
            return `${dayDiff}天${minDiff}分钟`;
          }
        } else {
          if (minDiff === 0) {
            return `${dayDiff}天${hourDiff}小时`;
          } else {
            return `${dayDiff}天${hourDiff}小时${minDiff}分钟`;
          }
        }
      }
    },
    timeSpentBtnDisabled() {
      return !this.completedTimeVisible;
    },
    repeatDialogTitle() {
      if (this.validateTime()) {
        if (this.completedDateVisible || this.completedTimeVisible) {
          return `开始：${this.formatDate(this.startTime)} ~ 完成：${this.formatDate(this.completedTime)}`
        }
        return `开始：${this.formatDate(this.startTime)}`
      }
      return ''
    }
  },

  methods: {
    handlePlanTime(plan) {
      this.$set(this, 'completedTime', new Date(this.startTime.getTime() + plan * 60000));
    },
    validateTime() {
      if (this.timeDiff < 0 || (this.completedTime !== null && this.completedTime < this.startTime)) {
        this.$message({
          message: '完成时间需大于开始时间',
          type: "warning"
        })
        return false
      } else {
        return true
      }
    },
    handleDateConfirm() {
      if (this.validateTime()) {
        this.task.dateAndTime.startTime = this.startTime;
        this.task.dateAndTime.completedTime = this.completedTimeVisible || this.completedDateVisible ? this.completedTime : null;
        // TODO deep clone customResult
        this.task.repeat = JSON.parse(JSON.stringify(this.repeat))
        console.log(this.task.repeat === this.repeat)
        this.$emit("dateConfirm")
      }
    },
    handleDateCancel() {
      this.task.dateAndTime.startTime = this.dateAndTimeBk.startTime
      this.task.dateAndTime.completedTime = this.dateAndTimeBk.completedTime
      this.$emit("dateCancel")
    },
    handleDateDelete() {
      this.$confirm("确认要删除日程吗？", "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.startTime = null
        this.completedTime = null
        this.task.dateAndTime.startTime = this.startTime
        this.task.dateAndTime.completedTime = this.completedTime
        this.$emit("delete")
      }).catch(() => {
      })
    },
    handleOpenRepeatDialog() {
      if (this.validateTime()) {
        this.repeatDialogVisible = true
      } else {
        this.repeatDialogVisible = false
      }
    },
    handleRepeatConfirm(repeat) {
      this.repeatDialogVisible = false
      this.labelFormatHolder.selectedRepeatList = repeat.customResult.selectedItem
      this.labelFormatHolder.frequencyValue = repeat.customResult.frequencyValue
      this.repeat = repeat
    },
  },
}
</script>

<style scoped>
/deep/ .el-button,
/deep/ .el-dropdown-menu__item, .el-menu-item,
.content-font-size,
/deep/ .el-input,
/deep/ .el-select .el-input .el-select__caret {
  font-size: 1.4rem;
}

.spend-time-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 0.8rem;
}

.spend-time-padding {
  padding: 0.6rem 1rem;
}

.spend-time-padding + .spend-time-padding {
  margin-left: 0;
}

.defaultSelectTimeStyle {
  border-color: #e67e22;
  background-color: #fdf2e9;
  color: #cf711f;
}
</style>
