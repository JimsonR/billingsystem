package com.example.billingsystem.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
@Override
    public void beforeJob(JobExecution jobExecution){
    System.out.println("Job Started: "+ jobExecution.getJobInstance());
}

@Override
    public void afterJob(JobExecution jobExecution){
    if (jobExecution.getStatus().isUnsuccessful()){
        System.out.println("Job Failed: "+ jobExecution.getExitStatus());
    }else {
        System.out.println("Job completed Successfully");
    }
}
}
