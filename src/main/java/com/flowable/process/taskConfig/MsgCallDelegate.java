package com.flowable.process.taskConfig;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class MsgCallDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("MsgCallDelegate ===> 回调其他服务中");
    }
}
