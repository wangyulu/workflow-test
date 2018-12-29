/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.workflow.test.dto;

/**
 * TODO: 请添加描述
 *
 * @author WANGYULU025
 * @date 2018/12/25
 * @since 1.0.0
 */
public class AuditDto {

    public String processId;

    public String taskId;

    public boolean status;

    public String msg;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AuditDto{" +
                "processId='" + processId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
