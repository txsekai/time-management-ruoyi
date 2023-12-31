package com.ruoyi.taskList.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class TaskList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    private String taskName;

    private Integer taskStatus;

    private LocalDateTime taskStartTime;

    private LocalDateTime taskCompletedTime;

    private Long taskRepeatId;

    private Integer taskPriority;

    private Long userId;

    private TaskRepeat repeat;

    private List<Tag> tags;

    public Long getTaskId() { return taskId; }

    public void setTaskId(Long taskId) { this.taskId = taskId; }

    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 100, message = "任务名称长度不能超过100个字符")
    public String getTaskName() { return taskName; }

    public void setTaskName(String taskName) { this.taskName = taskName; }

    public Integer getTaskStatus() { return taskStatus; }

    public void setTaskStatus(Integer taskStatus) { this.taskStatus = taskStatus; }

    public LocalDateTime getTaskStartTime() { return taskStartTime; }

    public void setTaskStartTime(LocalDateTime taskStartTime) { this.taskStartTime = taskStartTime; }

    public LocalDateTime getTaskCompletedTime() { return taskCompletedTime; }

    public void setTaskCompletedTime(LocalDateTime taskCompletedTime) { this.taskCompletedTime = taskCompletedTime; }

    public Long getTaskRepeatId() { return taskRepeatId; }

    public void setTaskRepeatId(Long taskRepeatId) { this.taskRepeatId = taskRepeatId; }

    public Integer getTaskPriority() { return taskPriority; }

    public void setTaskPriority(Integer taskPriority) { this.taskPriority = taskPriority; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public TaskRepeat getRepeat() { return repeat; }

    public void setRepeat(TaskRepeat repeat) { this.repeat = repeat; }

    public List<Tag> getTags() { return tags; }

    public void setTags(List<Tag> tags) { this.tags = tags; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("taskName", getTaskName())
                .append("taskStatus", getTaskStatus())
                .append("taskStartTime", getTaskStartTime())
                .append("taskCompletedTime", getTaskCompletedTime())
                .append("taskRepeatId", getTaskRepeatId())
                .append("taskPriority", getTaskPriority())
                .append("userId", getUserId())
                .append("repeat", getRepeat())
                .append("tags", getTags())
                .toString();
    }
}
