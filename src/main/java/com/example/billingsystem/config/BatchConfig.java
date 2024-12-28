package com.example.billingsystem.config;

import com.example.billingsystem.batch.JobCompletionNotificationListener;
import com.example.billingsystem.entity.ProductCatalog;
import com.example.billingsystem.repository.ProductCatalogRepository;
import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

//    @Autowired
//    private JobBuilder jobBuilder;
//
//
//    @Autowired
//    private StepBuilder stepBuilder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductCatalogRepository repository;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    public BatchConfig(JobRepository jobRepository , PlatformTransactionManager transactionManager){
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ProductCatalog> reader(@Value("#{jobParameters['file']}") String filePath) throws IOException {
      if (filePath == null){
          throw new IllegalArgumentException("file is required");
      }
      Path path = Paths.get(filePath);
        return new FlatFileItemReaderBuilder<ProductCatalog>()
                .name("productItemReader")
                .resource(new FileSystemResource(path.toFile()))
                .delimited()
                .names("name","category","price", "availableQuantity","isActive")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(ProductCatalog.class);
                }})
                .build();

    }

    @Bean
    public ItemProcessor<ProductCatalog, ProductCatalog> processor(){
        return productCatalog ->{
            if (productCatalog.getPrice().compareTo(BigDecimal.ZERO) <= 0 ||
                    (productCatalog.getAvailableQuantity() != null && productCatalog.getAvailableQuantity() < 0)){
                throw new IllegalArgumentException("Invalid product data: "+ productCatalog);

            }
            return productCatalog;
        };
    }

    @Bean
    public JdbcBatchItemWriter<ProductCatalog> writer(){
        return new JdbcBatchItemWriterBuilder<ProductCatalog>()
                .dataSource(dataSource)
                .sql("INSERT INTO product_catalog (name , category , description, price , available_quantity, is_active, created_at, updated_at)"+
                        "VALUES (:name, :category, :description, :price, :availableQuantity, :isActive , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                .beanMapped()
                .build();


    }
    @Bean
    public Step step(ItemReader<ProductCatalog> reader,
                     ItemProcessor<ProductCatalog, ProductCatalog> processor,
                     ItemWriter<ProductCatalog> writer){
        return new StepBuilder("productCatalogStep", jobRepository)
                .<ProductCatalog , ProductCatalog>chunk(10,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();


    }

    @Bean
    public Job importProductJob(JobCompletionNotificationListener listener ,Step step){
        return new JobBuilder("importProductJob", jobRepository)
                .start(step)
                .listener(listener)
                .build();
    }
}
