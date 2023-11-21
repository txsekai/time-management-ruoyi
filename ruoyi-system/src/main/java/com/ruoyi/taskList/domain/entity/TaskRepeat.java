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

    private Integer customNum;

    private String repeatFrequencyValue;

    private String repeatSelectedItem;

    public String getRepeatValue() { return repeatValue; }

    public void setRepeatValue(String repeatValue) { this.repeatValue = repeatValue; }

    public String getEndRepeat() { return endRepeat; }

    public void setEndRepeat(String endRepeat) { this.endRepeat = endRepeat; }

    public LocalDateTime getEndRepeatDate() { return endRepeatDate; }

    public void setEndRepeatDate(LocalDateTime endRepeatDate) { this.endRepeatDate = endRepeatDate; }

    public Integer getCustomNum() { return customNum; }

    public void setCustomNum(Integer customNum) { this.customNum = customNum; }

    public String getRepeatFrequencyValue() { return repeatFrequencyValue; }

    public void setRepeatFrequencyValue(String repeatFrequencyValue) { this.repeatFrequencyValue = repeatFrequencyValue; }

    public String getRepeatSelectedItem() { return repeatSelectedItem; }

    public void setRepeatSelectedItem(String repeatSelectedItem) { this.repeatSelectedItem = repeatSelectedItem; }

    public Long getTaskRepeatId() { return taskRepeatId; }

    public void setTaskRepeatId(Long taskRepeatId) { this.taskRepeatId = taskRepeatId; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("repeatValue", getRepeatValue())
                .append("endRepeat" , getEndRepeat())
                .append("endRepeatDate", getEndRepeatDate())
                .append("customNum", getCustomNum())
                .append("repeatFrequencyValue", getRepeatFrequencyValue())
                .append("repeatSelectedItem", getRepeatSelectedItem())
                .append("taskId", getTaskRepeatId())
                .toString();
    }
}
