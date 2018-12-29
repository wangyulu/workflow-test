/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.workflow.test.dto;

import java.util.Date;

/**
 * TODO: 请添加描述
 *
 * @author WANGYULU025
 * @date 2018/12/25
 * @since 1.0.0
 */
public class LeaveDto {
    public Date startDate;

    public Date endDate;

    public String reason;

    public Long userId;

    public String processId;

    public String taskId;

    public String msg;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LeaveDto{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", userId=" + userId +
                ", processId='" + processId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
