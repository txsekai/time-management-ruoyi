<template>
  <el-date-picker
    v-model="stateValue"
    :align="align"
    type="date"
    :picker-options="pickerOptions"
    :clearable="false"
    @change="handleChange"
  ></el-date-picker>
</template>

<script>
export default {
  name: 'DateItem',

  props: {
    value: {
      type: Date
    },
    align: {
      type: String,
      default: 'left'
    },
    pickerOptions: {
      type: Object,
      default: null
    }
  },

  watch: {
    value(val) {
      if(val !== null) {
        this.stateValue = new Date(val.getTime());
      }
    }
  },

  data() {
    return {
      stateValue: null,
    }
  },

  created() {
    if(this.value !== null) {
      this.stateValue = new Date(this.value.getTime());
    }
  },

  methods: {
    handleChange() {
      let tmpDateTime = new Date(this.stateValue.getTime());
      tmpDateTime.setHours(this.value.getHours());
      tmpDateTime.setMinutes(this.value.getMinutes());
      tmpDateTime.setSeconds(this.value.getSeconds())
      this.$emit('input', tmpDateTime)
    }
  }
}
</script>

<style scoped>
.el-date-editor.el-input, .el-date-editor.el-input__inner {
  width: 13.5rem;
}

.el-date-editor /deep/ .el-input__inner {
  height: 2.5rem;
  line-height: 2.5rem;
}

.el-date-editor /deep/ .el-input__icon {
  line-height: 2.5rem;
}
</style>
