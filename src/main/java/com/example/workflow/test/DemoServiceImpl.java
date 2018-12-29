/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.example.workflow.test;

import com.example.workflow.test.dto.AuditDto;
import com.example.workflow.test.dto.LeaveDto;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: 请添加描述
 *
 * @author WANGYULU025
 * @date 2018/12/25
 * @since 1.0.0
 */
@Service
public class DemoServiceImpl {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    public LeaveDto startProcess(LeaveDto dto) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startDate", dto.getStartDate());
        map.put("endDate", dto.getEndDate());
        map.put("reason", dto.getReason());
        map.put("applyUserId", dto.getUserId());
        //根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/test_myprocess.bpmn").deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程定义，返回流程实例
        //ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());

        // 创建流程引擎
        //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test_myprocess", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), map);

        dto.setProcessId(processInstance.getId());

        return dto;
    }

    public AuditDto auditLeader(AuditDto dto) {
        Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).taskDefinitionKey("leaderAudit").singleResult();
        if (task == null) {
            dto.setMsg("no task");
            return dto;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("leaderAudit", dto.isStatus());

        taskService.complete(task.getId(), map);
        dto.setTaskId(task.getId());

        return dto;
    }

    public AuditDto hrLeader(AuditDto dto) {
        Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).taskDefinitionKey("hrAudit").singleResult();
        if (task == null) {
            dto.setMsg("no task");
            return dto;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hrAudit", dto.isStatus());

        taskService.complete(task.getId(), map);
        dto.setTaskId(task.getId());

        return dto;
    }

    public AuditDto editLeave(AuditDto dto) {
        Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).taskDefinitionKey("isEdit").singleResult();
        if (task == null) {
            dto.setMsg("no task");
            return dto;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isEdit", dto.isStatus());

        taskService.complete(task.getId(), map);
        dto.setTaskId(task.getId());

        return dto;
    }

    public LeaveDto oneself(LeaveDto dto) {
        Task task = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).taskDefinitionKey("oneselfAudit").singleResult();
        if (task == null) {
            dto.setMsg("no task");
            return dto;
        }
        taskService.complete(dto.getTaskId());

        return dto;
    }

    public List<Task> query(LeaveDto dto) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(dto.getProcessId()).processInstanceBusinessKey(dto.getUserId().toString()).list();

        return tasks;
    }

    /**
     * <p>描述:  生成流程图
     * 首先启动流程，获取processInstanceId，替换即可生成</p>
     *
     * @param processInstanceId
     * @throws Exception
     * @author 范相如
     * @date 2018年2月25日
     */
    public void queryProImg(String processInstanceId) throws Exception {
        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //根据流程定义获取输入流
        InputStream is = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        BufferedImage bi = ImageIO.read(is);
        File file = new File("demo2.png");
        if (!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(bi, "png", fos);
        fos.close();
        is.close();
        System.out.println("图片生成成功");

        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("userId").list();
        for (Task t : tasks) {
            System.out.println(t.getName());
        }
    }


    /**
     * 流程图高亮显示
     * 首先启动流程，获取processInstanceId，替换即可生成
     *
     * @throws Exception
     */
    public void queryProHighLighted(String processInstanceId) throws Exception {
        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();

        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);

        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        //配置字体
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "宋体", "微软雅黑", "黑体", null, 2.0);
        BufferedImage bi = ImageIO.read(imageStream);
        File file = new File("demo2.png");
        if (!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(bi, "png", fos);
        fos.close();
        imageStream.close();
        System.out.println("图片生成成功");
    }

    /**
     * 获取需要高亮的线
     *
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private List<String> getHighLightedFlows(
            ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1)
                            .getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances
                        .get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances
                        .get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(
                        activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl
                    .getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
                        .getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}
