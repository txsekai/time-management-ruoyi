<template>
  <el-row>
    <el-row>
            <span>日程将每{{
                customResult.num
              }}{{ customResult.frequencyValue == REPEAT_SELECT.MONTH ? '个' : '' }}{{ getFrequencyLabel }}{{
                formattedSelectedLabel
              }}重复</span>
    </el-row>

    <el-row class="mt12">
      <el-col :span="12">
        <span>每</span>
        <el-input v-model="customResult.num" @input="handleInput" @blur="handleBlur"></el-input>

        <el-select v-model="customResult.frequencyValue">
          <el-option
            v-for="item in frequencyOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>

    <el-row class="mt12" v-show="customResult.frequencyValue==REPEAT_SELECT.WEEK">
      <!--            TODO 把tagDialog里面的tag抽成公共组件？-->
      <el-tag v-for="(week, index) in formattedShowWeek(weekOptions)"
              :key="index"
              class="option-button user-select-none"
              @click="handleSelectOrCancelWeek(index)"
              :class="{selected: isSelected(customResult.selectedItem, index)}"
      >{{ week }}
      </el-tag>
    </el-row>

    <div class="mt12 month-grid" v-show="customResult.frequencyValue==REPEAT_SELECT.MONTH">
      <el-tag v-for="month in monthOptions"
              :key="month"
              class="option-button user-select-none"
              @click="handleSelectOrCancelMonth(month)"
              :class="{selected: isSelected(customResult.selectedItem, month)}"
      >{{ month + 1 }}
      </el-tag>
    </div>

    <div class="mt12 year-grid" v-show="customResult.frequencyValue==REPEAT_SELECT.YEAR">
      <el-tag v-for="year in yearOptions"
              :key="year"
              class="option-button user-select-none"
              @click="handleSelectOrCancelYear(year)"
              :class="{selected: isSelected(customResult.selectedItem, year)}"
      >{{ year + 1 }}
      </el-tag>
    </div>
  </el-row>
</template>

<script>
import RepeatMixin from "../../../mixins/formatRepeat";
import {convertLabel, REPEAT_SELECT} from "../../../constants/dateSelectedConstants";

export default {
  name: 'customRepeatItem',
  mixins: [RepeatMixin],

  props: {
    customResult: {
      type: Object,
    },
  },

  data() {
    return {
      frequencyOptions: [
        {
          value: REPEAT_SELECT.DAY,
          label: convertLabel(REPEAT_SELECT.DAY)
        }, {
          value: REPEAT_SELECT.WEEK,
          label: convertLabel(REPEAT_SELECT.WEEK)
        }, {
          value: REPEAT_SELECT.MONTH,
          label: convertLabel(REPEAT_SELECT.MONTH)
        }, {
          value: REPEAT_SELECT.YEAR,
          label: convertLabel(REPEAT_SELECT.YEAR)
        },
      ],

      weekOptions: [],

      monthOptions: [],

      yearOptions: [],
    }
  },

  created() {
    this.initWeekOptions()
    this.initMonthOptions()
    this.initYearOptions()
  },

  /* TODO
  问题1：task1选择的frequencyValue和task2的frequencyValue不一样，就会走进这个watch，就清空selectedItem
   */

  watch: {
    'customResult.frequencyValue'() {
      while (this.customResult.selectedItem.length > 0) {
        this.customResult.selectedItem.pop();
      }
      this.labelFormatHolder.frequencyValue = this.customResult.frequencyValue
      this.labelFormatHolder.selectedRepeatList = this.customResult.selectedItem
    },
  },

  computed: {
    REPEAT_SELECT() {
      return REPEAT_SELECT
    },
    getFrequencyLabel() {
      const selectedOption = this.frequencyOptions.find(
        option => option.value === this.customResult.frequencyValue
      )

      return selectedOption ? selectedOption.label : ''
    },
  },

  methods: {
    /*
    TODO 如果选择的自定义可以在预设里面找到，联动到预设
     */
    isSelected(selectionArray, item) {
      return selectionArray.includes(item);
    },
    initWeekOptions() {
      for (let i = 0; i <= 6; i++) {
        this.weekOptions.push(i)
      }
    },
    initMonthOptions() {
      for (let i = 0; i <= 30; i++) {
        this.monthOptions.push(i)
      }
    },
    initYearOptions() {
      for (let i = 0; i <= 11; i++) {
        this.yearOptions.push(i)
      }
    },
    handleSelectOrCancel(selectionArray, item) {
      if (this.isSelected(selectionArray, item)) {
        this.deselect(selectionArray, item)
      } else {
        this.select(selectionArray, item)
      }
    },
    select(selectionArray, item) {
      if (!selectionArray.includes(item)) {
        selectionArray.push(item)
      }
    },
    deselect(selectionArray, item) {
      const index = selectionArray.indexOf(item)
      if (index !== -1) {
        selectionArray.splice(index, 1)
      }
    },
    handleSelectOrCancelWeek(index) {
      this.handleSelectOrCancel(this.customResult.selectedItem, index)
    },
    handleSelectOrCancelMonth(month) {
      this.handleSelectOrCancel(this.customResult.selectedItem, month)
    },
    handleSelectOrCancelYear(year) {
      this.handleSelectOrCancel(this.customResult.selectedItem, year)
    },
    handleInput() {
      this.customResult.num = this.customResult.num.replace(/[^1-9]/g, '')
    },
    handleBlur() {
      if (this.customResult.num == '') {
        this.customResult.num = 1
      }
    }
  },
}
</script>

<style scoped>
.el-select,
.el-input {
  margin-left: 1rem;
  width: 7rem;
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

.option-button {
  border-radius: 2rem;
}

.el-tag {
  margin-left: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
  background-color: #fff;
  border-color: #fff;
  width: 3rem;
  text-align: center;
  height: 3rem;
  padding: 0;
}

.selected {
  border-color: #e67e22;
  background-color: #fdf2e9;
  color: #cf711f;
}

.month-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}

.year-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  width: 30rem;
}
</style>
