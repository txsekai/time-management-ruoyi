package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

public class TaskRepeat extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long taskRepeatId;

    private String repeatValue;

    private String endRepeat;

    private LocalDateTime endRepeatDate;

    private Integer num;

    private String frequencyValue;

    private String selectedItem;

    public String getRepeatValue() { return repeatValue; }

    public void setRepeatValue(String repeatValue) { this.repeatValue = repeatValue; }

    public String getEndRepeat() { return endRepeat; }

    public void setEndRepeat(String endRepeat) { this.endRepeat = endRepeat; }

    public LocalDateTime getEndRepeatDate() { return endRepeatDate; }

    public void setEndRepeatDate(LocalDateTime endRepeatDate) { this.endRepeatDate = endRepeatDate; }

    public Integer getNum() { return num; }

    public void setNum(Integer num) { this.num = num; }

    public String getFrequencyValue() { return frequencyValue; }

    public void setFrequencyValue(String frequencyValue) { this.frequencyValue = frequencyValue; }

    public String getSelectedItem() { return selectedItem; }

    public void setSelectedItem(String selectedItem) { this.selectedItem = selectedItem; }

    public Long getTaskRepeatId() { return taskRepeatId; }

    public void setTaskRepeatId(Long taskRepeatId) { this.taskRepeatId = taskRepeatId; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("repeatValue", getRepeatValue())
                .append("endRepeat" , getEndRepeat())
                .append("endRepeatDate", getEndRepeatDate())
                .append("num", getNum())
                .append("frequencyValue", getFrequencyValue())
                .append("selectedItem", getSelectedItem())
                .append("taskId", getTaskRepeatId())
                .toString();
    }
}
