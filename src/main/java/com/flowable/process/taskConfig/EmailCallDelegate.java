package com.flowable.process.taskConfig;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailCallDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("EmailCallDelegate ===> 回调其他服务中");
    }
}
