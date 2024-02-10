package com.example.spring_batchstudy.job;


import com.example.spring_batchstudy.model.AccountRepository;
import com.example.spring_batchstudy.model.Accounts;
import com.example.spring_batchstudy.model.OrderRepository;
import com.example.spring_batchstudy.model.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;

import org.springframework.batch.core.repository.JobRepository;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;


    @Bean
    public Job simpleJob(JobRepository jobRepository, Step dateStep1) {
        return new JobBuilder("simpleJob2", jobRepository)
                .start(dateStep1)
                .build();
    }

    @Bean
    public Step dateStep1(JobRepository jobRepository, Tasklet dateTasklet, PlatformTransactionManager platformTransactionManager) {
        log.info("여기와1");
        return new StepBuilder("dateStep1", jobRepository)
                .<Orders, Accounts>chunk(5, platformTransactionManager)
                .reader(dataReader())
//                .writer(new ItemWriter<Orders>() {
//                    @Override
//                    public void write(Chunk<? extends Orders> chunk) throws Exception {
//                        chunk.forEach(System.out::println);
//                    }
//                } )
                .processor(dataProcessor())
                .writer(dataWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Orders, Accounts> dataProcessor() {
        log.info("여기와2");
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                log.info("여기와");
                return new Accounts(item);
            }
        };
    }

    public RepositoryItemWriter<Accounts> dataWriter() {
        log.info("여기와3");
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public RepositoryItemReader<Orders> dataReader() {
        log.info("여기와4");
        return new RepositoryItemReaderBuilder<Orders>()
                .name("orders")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

/* 심플한 스템
    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }
    @Bean
    public Tasklet testTasklet(){
        return (contribution, chunkContext) -> {
            log.info("hello");
            return RepeatStatus.FINISHED; // 종료 상태 반환
        };
    } }
*/

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
//    public Job hello(){
//        return jobBuilderFactory.get("hello")
//                .incrementer(new RunIdIncrementer())
//                .start(helloStep())
//                .build();
//        }
//    @Bean
//    @JobScope
//    public Step helloStep() {
//        return stepBuilderFactory.get("helloStep")
//                .tasklet(helloTasklet())
//                .build();
//    }
//
    @Bean
    @StepScope
    public Tasklet testTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("hello");
                return RepeatStatus.FINISHED;
            }
        };
    }
}





