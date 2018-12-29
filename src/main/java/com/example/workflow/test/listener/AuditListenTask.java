/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.workflow.test.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * TODO: 请添加描述
 *
 * @author WANGYULU025
 * @date 2018/12/25
 * @since 1.0.0
 */
public class AuditListenTask implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(delegateTask.getName());
    }
}
