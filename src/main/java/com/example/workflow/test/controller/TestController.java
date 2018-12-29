/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.workflow.test.controller;

import com.example.workflow.test.DemoServiceImpl;
import com.example.workflow.test.dto.AuditDto;
import com.example.workflow.test.dto.LeaveDto;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO: 请添加描述
 *
 * @author WANGYULU025
 * @date 2018/12/26
 * @since 1.0.0
 */
@RestController
public class TestController {
    @Autowired
    DemoServiceImpl demoService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public LeaveDto submitAudit(@RequestBody LeaveDto dto) {
        System.out.println(dto);
        return demoService.startProcess(dto);
    }

    @RequestMapping(value = "/leaderCheck", method = RequestMethod.POST)
    public AuditDto auditLeader(@RequestBody AuditDto dto) {
        System.out.println(dto);
        return demoService.auditLeader(dto);
    }

    @RequestMapping(value = "/hrCheck", method = RequestMethod.POST)
    public AuditDto hrAudit(@RequestBody AuditDto dto) {
        System.out.println(dto);
        return demoService.hrLeader(dto);
    }

    @RequestMapping(value = "/oneself", method = RequestMethod.POST)
    public LeaveDto auditOneself(@RequestBody LeaveDto dto) {
        System.out.println(dto);
        return demoService.oneself(dto);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public AuditDto edit(@RequestBody AuditDto dto) {
        System.out.println(dto);
        return demoService.editLeave(dto);
    }

    @RequestMapping(value = "/querySubmit", method = RequestMethod.POST)
    public List<Task> querySubmit(@RequestBody LeaveDto dto) {
        System.out.println(dto);
        return demoService.query(dto);
    }

    @RequestMapping(value = "/queryImg/{processId}", method = RequestMethod.GET)
    public void queryImg(@PathVariable(name = "processId") String processId) throws Exception {
        demoService.queryProHighLighted(processId);
    }
}
