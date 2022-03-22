package com.flowable.process.controller;


import com.flowable.process.entity.MyEntity;
import com.flowable.process.service.MyService;
import liquibase.pro.packaged.A;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("flowable")
public class MyRestController {

    @Autowired
    private MyService myService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    private static final String HEAD = "apply-request";

    /**
     * @title
     * @description 添加一个初始请求
     * @author jiangyongtao
     * @updateTime 2022/3/22 15:12
     */
    @RequestMapping(value = "addReq", method = RequestMethod.POST)
    public String addYourReq(@RequestParam String name, @RequestParam String reqDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("studentName", name);
        map.put("reqInfo", reqDetails);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(HEAD, map);
        return "已经提交成功 : 该流程ID:" + processInstance.getId();
    }

    @RequestMapping(value = "dealReq", method = RequestMethod.POST)
    public String dealPeopleReq( String taskId) {
        Task task  = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null){
            return "流程已审批完毕或不存在";
        }
        Map<String,Object> map = new HashMap<>();
//        boolean ap = approved.equals("1")?true:false;
        map.put("approved",true);
        taskService.complete(taskId,map);
        return "审批是否通过: ";
    }

    /**
     * @title
     * @description 显示当前流程进展
     * @author jiangyongtao
     * @updateTime 2022/3/22 15:13
     */
    @RequestMapping(value = "getReqProcess", method = RequestMethod.POST)
    public void getMap(HttpServletResponse httpServletResponse, @RequestParam String processId) throws IOException {

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //如果流程走完直接返回
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例Id,查询当前正在执行的对象list,返回流程实例对象
        String instanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(instanceId).list();
        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }


}
