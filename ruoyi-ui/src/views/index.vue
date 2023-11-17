<template>
  <el-container>
    <el-main class="section-main">
      <div class="container grid">
        <el-card class="card">
          <div id="oneDayFocusTime" style="width: 100%; height: 23rem"></div>
          <!--                    TODO show more button && chart label-->
        </el-card>

        <el-card class="card">
          <!--                        TODO 合计总专注时间-->
          <el-table
            :data="focusOnRecordingData"
            show-summary
            style="width: 100%"
          >
            <!--                        TODO 想在第一列放专注类型的标签，标签和数据一样从数据库取-->
            <el-table-column label="已完成记录">
              <template slot-scope="scope">
                <i class="el-icon-alarm-clock"></i>
                <span style="margin-left: 1rem">{{ scope.row.focusType}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="timeSlot"></el-table-column>
            <el-table-column prop="time"></el-table-column>
            <el-table-column>
              <template slot="header">
                <el-date-picker
                  v-model="oneDayTotalTime"
                  align="right"
                  type="date"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!--                TODO 日历待办事项联动-->
        <el-card class="card calendar-card">
          <div class="calendar-flex">
            <el-calendar class="calendar-item" v-model="calendarDate">

            </el-calendar>

            <el-table
              class="to-do-detail"
              :data="toDoData"
            >
              <el-table-column label="选择日所有事项">
                <template slot-scope="scope">
                  <i class="el-icon-alarm-clock"></i>
                  <span style="margin-left: 1rem">{{ scope.row.toDoType}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="timeSlotDoing"></el-table-column>
              <el-table-column prop="timeDoing"></el-table-column>
              <el-table-column prop="priority">
                <template slot-scope="scope">
                  <div>
                    <i v-for="count in scope.row.priority" :key="count" class="el-icon-star-on"></i>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>

        </el-card>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "Index",
  data() {
    return {
// TODO 数据太多要用懒加载
      focusOnRecordingData: [{
        focusType: '运动',
        timeSlot: '20:00~21:00',
        time: '1小时'
      },{
        focusType: '学习',
        timeSlot: '21:00~22:30',
        time: '1.5小时'
      },{
        focusType: '运动',
        timeSlot: '20:00~21:00',
        time: '1小时'
      },{
        focusType: '学习',
        timeSlot: '21:00~22:30',
        time: '1.5小时'
      },{
        focusType: '运动',
        timeSlot: '20:00~21:00',
        time: '1小时'
      },{
        focusType: '学习',
        timeSlot: '21:00~22:30',
        time: '1.5小时'
      },{
        focusType: '运动',
        timeSlot: '20:00~21:00',
        time: '1小时'
      },{
        focusType: '学习',
        timeSlot: '21:00~22:30',
        time: '1.5小时'
      }
      ],
      oneDayTotalTime: new Date(),
      calendarDate: new Date(),
      toDoData: [
        {
          toDoType: '拍摄',
          timeSlotDoing: '20:00~21:00',
          timeDoing: '1小时',
          priority: 2
        },{
          toDoType: '读书',
          timeSlotDoing: '21:00~22:42',
          timeDoing: '1.42小时',
          priority: 3
        },{
          toDoType: '拍摄',
          timeSlotDoing: '20:00~21:00',
          timeDoing: '1小时',
          priority: 2
        },{
          toDoType: '读书',
          timeSlotDoing: '21:00~22:42',
          timeDoing: '1.42小时',
          priority: 3
        },{
          toDoType: '拍摄',
          timeSlotDoing: '20:00~21:00',
          timeDoing: '1小时',
          priority: 2
        },{
          toDoType: '读书',
          timeSlotDoing: '21:00~22:42',
          timeDoing: '1.42小时',
          priority: 3
        },{
          toDoType: '拍摄',
          timeSlotDoing: '20:00~21:00',
          timeDoing: '1小时',
          priority: 2
        },{
          toDoType: '读书',
          timeSlotDoing: '21:00~22:42',
          timeDoing: '1.42小时',
          priority: 3
        }
      ],
    };
  },

  mounted() {
    this.oneDayChartRender()
  },

  methods: {
    oneDayChartRender() {
      const chart = echarts.init(document.getElementById('oneDayFocusTime'))
      // TODO 数据从后端取
      const options = {
        xAxis: {
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun','Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {},
        series: [
          {
            type: 'bar',
            data: [23, 24, 18, 25, 27, 28, 25,90, 24, 18, 25, 27, 28, 110],
            color: "#cf711f"
          }
        ]
      }

      chart.setOption(options)
      window.addEventListener('resize', () => {chart.resize()})
      window.onresize = () => {
        clearTimeout(this.timer)
        this.timer = setTimeout(() => {
        }, 300)
      }
    },
  }
};
</script>

<style scoped>
.grid {
  display: grid;
  column-gap: 5.6rem;
  row-gap: 1.6rem;

  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: 40% 60%;
}

/deep/ .el-card__body {
  padding: 0.2rem;
}

/deep/ .el-table thead {
  color: #cf711f;
}

.el-date-editor.el-input, .el-date-editor.el-input__inner {
  width: 100%;
}

.calendar-card {
  grid-column: 1 / -1;
}

.calendar-flex {
  display: flex;
  gap: 2.4rem;
}

.calendar-item {
  width: 56rem;
}

/deep/ .el-calendar__header {
  border-bottom: 0;
  padding: 1rem 2rem 0.2rem;
}

/deep/ .el-calendar__body {
  padding: 0.6rem 2rem 3.5rem;
}

/deep/ .el-calendar-table thead th {
  padding: 0.8rem 0;
}

/deep/ .el-calendar-table .el-calendar-day {
  height: 4.8rem;
  padding: 0.6rem;
}
</style>

