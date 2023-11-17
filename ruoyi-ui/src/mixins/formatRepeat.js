
import {convertLabel, REPEAT_SELECT} from "../constants/dateSelectedConstants";
import DateMixin from "./formatDate";

const RepeatMixin = {
  mixins: [DateMixin],
  data:function(){
    return {
      labelFormatHolder: {
        selectedRepeatList:[],
        frequencyValue: '',
      }
    }
  },
  computed: {
    formattedSelectedLabel() {
      return this.formatSelectedRepeatOption(this.labelFormatHolder.frequencyValue,this.labelFormatHolder.selectedRepeatList)
    }
  },

  methods: {
    formatSelectedRepeatOption(frequencyValue, selectedRepeatList){
      const sortedIndex = [...(selectedRepeatList)].sort((a, b) => a - b)
      switch (frequencyValue) {
        case REPEAT_SELECT.DAY:
          return ''
        case REPEAT_SELECT.WEEK:
          return this.formattedShowWeek(sortedIndex).map(week => `星期${week}`).join('、');
        case REPEAT_SELECT.MONTH:
          return sortedIndex.map(day => `${day + 1}日`).join('、')
        case REPEAT_SELECT.YEAR:
          return sortedIndex.map(month => `${month + 1}月`).join('、')
        default:
          return null;
      }
    },

    formattedShowWeek(weekOptions) {
      const weekDays = ['一', '二', '三', '四', '五', '六', '日']
      return weekOptions.map(option => weekDays[option])
    },
    getRepeatValueLabel(key) {
      return convertLabel(key)
    },
    formattedRepeatResult(localRepeatResult) {
      let formattedRepeat = ''

      if (localRepeatResult.repeatValue !== null) {
        formattedRepeat = `重复：${this.getRepeatValueLabel(localRepeatResult.repeatValue)}`

        if (localRepeatResult.endRepeat !== null) {
          formattedRepeat = formattedRepeat + ` 结束重复： ${this.getRepeatValueLabel(localRepeatResult.endRepeat)}`

          if (localRepeatResult.endRepeat == REPEAT_SELECT.ENDREPEATSELECTEDDATE) {
            formattedRepeat = `重复：${this.getRepeatValueLabel(localRepeatResult.repeatValue)} 结束重复： ${this.formatDate(localRepeatResult.endRepeatDate)}`
          }
        }

        if (localRepeatResult.customResult.num !== null) {
          formattedRepeat = formattedRepeat +
            `<br>每${localRepeatResult.customResult.num}`+
            `${localRepeatResult.customResult.frequencyValue == REPEAT_SELECT.MONTH ? '个' : ''}`+
            `${this.getRepeatValueLabel(localRepeatResult.customResult.frequencyValue)}`+
            `${this.formatSelectedRepeatOption(localRepeatResult.customResult.frequencyValue, localRepeatResult.customResult.selectedItem)}重复`
        }
      }

      return localRepeatResult.repeatValue == null ? '' : formattedRepeat
    },
  },
}

export default RepeatMixin
