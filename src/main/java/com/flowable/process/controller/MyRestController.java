package com.flowable.process.controller;


import com.flowable.process.entity.MyEntity;
import com.flowable.process.service.MyService;
import liquibase.pro.packaged.A;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "addReq", method = RequestMethod.POST)
    public String addYourReq(@RequestParam String name
            , @RequestParam String reqDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("studentName", name);
        map.put("reqInfo",reqDetails);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(HEAD,map);
        return "已经提交成功 : 该流程ID:" + processInstance.getId();
    }

    @RequestMapping(value = "getReqMap", method = RequestMethod.POST)
    public void getMap(@RequestParam String name
            , @RequestParam String reqDetails) {

    }


}
