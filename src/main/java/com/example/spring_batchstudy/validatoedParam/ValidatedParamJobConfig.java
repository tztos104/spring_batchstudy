package com.example.spring_batchstudy.validatoedParam;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
public class ValidatedParamJobConfig {

    @Bean
    public Job ValidatedParamJob(JobRepository jobRepository, Step ValidatedParamStep1) {
        return new JobBuilder("validatedParamJob", jobRepository)
                .validator(new FileValidator())
                .start(ValidatedParamStep1)

                .build();
    }
    @Bean
    public Step ValidatedParamStep1(JobRepository jobRepository, Tasklet ValidatedParamTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("validatedParamStep1", jobRepository)
                .tasklet(ValidatedParamTasklet, platformTransactionManager).build();
    }

    @JobScope
    public Tasklet ValidatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return (contribution, chunkContext) -> {
            log.info(fileName);
            log.info("hello-ValidatedParam");
            return RepeatStatus.FINISHED; // 종료 상태 반환
        };
    } }
// 스프링 배치 4.xx 버전일때
// @Configuration
// @EnableBatchProcessing
//public class HelloWorldJobConfig  {


// 
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job ValidatedParamJob(){
//        return jobBuilderFactory.get("hello")
//                .incrementer(new RunIdIncrementer())
//                .start(ValidatedParamStep())
//                .build();
//        }
//    @Bean
//    @JobScope
//    public Step ValidatedParamStep() {
//        return stepBuilderFactory.get("ValidatedParamStep")
//                .tasklet(ValidatedParamTasklet())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public Tasklet ValidatedParamTasklet() {
//        return new Tasklet() {
//            @Override
//            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                System.out.println("hello");
//                return RepeatStatus.FINISHED;
//            }
//        };
//    }





