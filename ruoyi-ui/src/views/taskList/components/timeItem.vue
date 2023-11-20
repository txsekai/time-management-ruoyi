<template>
  <div class="time-select">
    <el-select v-model="time.hour" @change="handleChange">
      <el-option
        v-for="hour in hours"
        :key="hour"
        :label="hour"
        :value="hour"
      ></el-option>
    </el-select>
    <span> : </span>
    <el-select v-model="time.minute" @change="handleChange">
      <el-option
        v-for="minute in minutes"
        :key="minute"
        :label="minute"
        :value="minute"
      ></el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  name: "TimeItem",

  props: {
    value: {
      type: Date
    }
  },

  watch: {
    value(val) {
      if(val !== null) {
        this.stateValue = new Date(val.getTime());
        this.time.hour = val.getHours().toString().padStart(2, '0');
        this.time.minute = val.getMinutes().toString().padStart(2, '0');
      }
    }
  },

  data() {
    return {
      time: {hour: 0, minute: 0},
      stateValue: null,
      hours: [],
      minutes: [],
    }
  },

  created() {
    this.initOptions()
    if(this.value !== null) {
      this.stateValue = new Date(this.value.getTime());
      this.time.hour = this.stateValue.getHours().toString().padStart(2, '0');
      this.time.minute = this.stateValue.getMinutes().toString().padStart(2, '0');
    }
  },

  methods: {
    initOptions(){
      for (let i = 0; i <= 23; i++) {
        const hour = i.toString().padStart(2, '0')
        this.hours.push(hour)
      }

      for (let i = 0; i <= 59; i++) {
        const minute = i.toString().padStart(2, '0')
        this.minutes.push(minute)
      }
    },
    handleChange() {
      this.stateValue.setHours(this.time.hour)
      this.stateValue.setMinutes(this.time.minute)
      this.$emit('input', this.stateValue)
    },
  }
}
</script>

<style scoped>
/deep/ .el-input,
/deep/ .el-select .el-input .el-select__caret {
  font-size: 1.4rem;
}

.time-select {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

/deep/ .el-input--suffix .el-input__inner {
  padding-right: 1rem;
}

/deep/ .el-input__inner {
  height: 2rem;
  line-height: 2rem;
  padding: 0 0.75rem;
}

/deep/ .el-input__icon {
  line-height: 2rem;
  width: 2rem;
}

.el-select-dropdown__item {
  font-size: 1.4rem;
  padding: 0 1rem;
  height: 2rem;
  line-height: 2rem;
}
</style>
