package com.flowable.process.service;

import org.flowable.task.api.Task;

import java.util.List;

public interface MyService {

    void startProcess();

    List<Task> getTasks(String assignee);

}
