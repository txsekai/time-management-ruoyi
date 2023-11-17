<template>
  <el-dialog
    :title="title"
    :visible.sync="repeatDialogVisible"
    width="30%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    append-to-body
  >
    <el-row>
      <el-col :span="12">
        <span>重复</span>
        <!--                TODO 让两列的select对齐。 只能改为el-form是相对简单的？-->
        <el-select v-model="repeatValue">
          <el-option
            v-for="item in repeatOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>

          <el-option
            :key="REPEAT_SELECT.CUSTOM"
            :label="convertLabel(REPEAT_SELECT.CUSTOM)"
            :value="REPEAT_SELECT.CUSTOM"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>

    <el-row class="mt16" v-if="endRepeatVisible">
      <el-col :span="12">
        <span>结束重复</span>
        <el-select v-model="endRepeat">
          <el-option
            v-for="item in endRepeatOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>

          <el-option
            :key="REPEAT_SELECT.ENDREPEATSELECTEDDATE"
            :label="convertLabel(REPEAT_SELECT.ENDREPEATSELECTEDDATE)"
            :value="REPEAT_SELECT.ENDREPEATSELECTEDDATE"
          ></el-option>
        </el-select>
      </el-col>

      <el-col :span="12" v-if="endDateVisible">
        <span>结束日期</span>
        <date-item style="margin-left: 1rem"
                   v-model="endRepeatDate"
                   :align="'right'"
        ></date-item>
      </el-col>
    </el-row>

    <custom-repeat-item class="mt36" v-if="repeatValue===REPEAT_SELECT.CUSTOM"
                        :custom-result="customResult"></custom-repeat-item>

    <div slot="footer" class="dialog-footer">
      <el-button class="button-padding" @click="handleConfirm">确认</el-button>
      <el-button class="button-padding" @click="handleRepeatCancel">取消</el-button>
    </div>

  </el-dialog>
</template>

<script>

import CustomRepeatItem from "../components/customRepeatItem.vue";
import DateItem from "../components/dateItem.vue";
import {convertLabel, REPEAT_SELECT} from "../../../constants/dateSelectedConstants";

export default {
  name: 'RepeatDialog',
  components: {DateItem, CustomRepeatItem},

  props: {
    repeatDialogVisible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '请设置重复'
    },
    repeatResult: {
      type: Object,
    },
  },

  data() {
    return {
      repeatValue: REPEAT_SELECT.NEVER,
      repeatOptions: [{
        value: REPEAT_SELECT.NEVER,
        label: convertLabel(REPEAT_SELECT.NEVER)
      }, {
        value: REPEAT_SELECT.EVERYDAY,
        label: convertLabel(REPEAT_SELECT.EVERYDAY)
      }, {
        value: REPEAT_SELECT.EVERYWEEK,
        label: convertLabel(REPEAT_SELECT.EVERYWEEK)
      }, {
        value: REPEAT_SELECT.EVERYWORKDAY,
        label: convertLabel(REPEAT_SELECT.EVERYWORKDAY)
      }, {
        value: REPEAT_SELECT.EVERYMONTH,
        label: convertLabel(REPEAT_SELECT.EVERYMONTH)
      }, {
        value: REPEAT_SELECT.EVERYYEAR,
        label: convertLabel(REPEAT_SELECT.EVERYYEAR)
      }],

      endRepeat: REPEAT_SELECT.NEVER,
      endRepeatOptions: [{
        value: REPEAT_SELECT.NEVER,
        label: convertLabel(REPEAT_SELECT.NEVER)
      }],

      endRepeatVisible: false,

      endDateVisible: false,
      endRepeatDate: null,
      // pickerOptions: {
      //     // TODO 取dateAndTimeDialog的开始日期?
      //     disabledDate: time => {
      //         const startOfDay = new Date(time.getFullYear(), time.getMonth(), time.getDate())
      //         return startOfDay.getTime() < new Date().setHours(0, 0, 0, 0)
      //     }
      // },

      customResult: {num: null, frequencyValue: null, selectedItem: null},
      localRepeatResult: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
    }
  },

  created() {
    this.localRepeatResult = this.repeatResult

    this.initLocalVariables(this.repeatResult)
  },

  computed: {
    REPEAT_SELECT() {
      return REPEAT_SELECT
    }
  },

  watch: {
    repeatResult(val) {
      this.localRepeatResult = val

      this.initLocalVariables(val)
    },

    repeatValue(newValue) {
      if (newValue === REPEAT_SELECT.CUSTOM) {
        this.endRepeat = REPEAT_SELECT.NEVER
        this.endRepeatVisible = true;
        this.customResult = {num: 1, frequencyValue: 'day', selectedItem: []}
      } else {
        this.endRepeatVisible = newValue !== REPEAT_SELECT.NEVER;
        this.customResult = {num: null, frequencyValue: null, selectedItem: null}
        this.endRepeat = REPEAT_SELECT.NEVER
        if(newValue === REPEAT_SELECT.NEVER) {
          this.endRepeat = null
        }
      }
    },

    endRepeat(newValue) {
      if (newValue === REPEAT_SELECT.ENDREPEATSELECTEDDATE) {
        this.endDateVisible = true

        const currentDate = new Date()
        currentDate.setDate(currentDate.getDate() + 7)
        currentDate.setHours(23, 59, 59, 999)
        this.endRepeatDate = currentDate
      } else {
        this.endDateVisible = false
        this.endRepeatDate = null
      }
    }
  },

  methods: {
    convertLabel,
    /*TODO
    重复设置按照：把有打开的开始日期、时间+完成日期、时间当做一个整体来循环
    在重复的时间内，该事项一直展示在TO DO card里
        如果今天完成了，就移动到其他card里面；明天又出现在TO DO card里
    更改一个重复的事项，会提示只更改该事项还是将来的都改
     */

    initLocalVariables(value) {
      this.repeatValue = value.repeatValue !== null ? value.repeatValue : REPEAT_SELECT.NEVER
      this.endRepeat = value.endRepeat !== null ? value.endRepeat : REPEAT_SELECT.NEVER
      this.endRepeatDate = value.endRepeatDate !== null ? value.endRepeatDate : new Date()
      this.customResult = value.customResult !== null ? value.customResult : {}
    },
    handleConfirm() {
      const {repeatValue, endRepeat, endRepeatDate, customResult} = this
      this.localRepeatResult = {repeatValue, endRepeat, endRepeatDate, customResult}
      this.$emit("repeatConfirm", this.localRepeatResult)
    },
    handleRepeatCancel() {
      this.$emit("repeatCancel")
    },
  },
}
</script>

<style scoped>
.el-select {
  margin-left: 1rem;
  width: 12rem;
}

/deep/ .el-input--suffix .el-input__inner {
  padding-right: 1rem;
}

/deep/ .el-input__inner {
  height: 2.5rem;
  line-height: 2.5rem;
  padding: 0 0.75rem;
}

/deep/ .el-input__icon {
  line-height: 2.5rem;
  width: 2.5rem;
}

.el-select-dropdown__item {
  font-size: 1.4rem;
  padding: 0 1rem;
  height: 2.5rem;
  line-height: 2.5rem;
}
</style>
