package com.example.spring_batchstudy.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String START_MESSAGE = "{} Job is starting";
    private static String END_MESSAGE = "{} Job is finished (status:{})";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(START_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if(jobExecution.getStatus()== BatchStatus.FAILED){
            log.info("실패");
        }
    }
}
