package com.example.billingsystem.config;

import com.example.billingsystem.batch.JobCompletionNotificationListener;
import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.entity.Product;
import com.example.billingsystem.entity.Product;
import com.example.billingsystem.repository.ProductRepository;
import com.example.billingsystem.repository.ProductRepository;
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
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
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
import org.springframework.validation.BindException;
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
    private ProductRepository productRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository repository;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    public BatchConfig(JobRepository jobRepository , PlatformTransactionManager transactionManager){
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Product> reader(@Value("#{jobParameters['productFile']}") String productFilePath) throws IOException {
      if (productFilePath == null){
          throw new IllegalArgumentException("Product file is required");
      }
      Path path = Paths.get(productFilePath);
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new FileSystemResource(path.toFile()))
                .delimited()
//                .names("name","category","price", "availableQuantity","isActive")

                .names("productId", "name", "description", "category")

                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(Product.class);
                }})
                .build();

    }

    @Bean
    public ItemProcessor<Product, Product> productProcessor(){
        return product ->{
//            if (Product.getPrice().compareTo(BigDecimal.ZERO) <= 0 ||
//                    (Product.getAvailableQuantity() != null && Product.getAvailableQuantity() < 0)){
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new IllegalArgumentException("Product name is required: " + product);
//                throw new IllegalArgumentException("Invalid product data: "+ Product);

            }
            return product;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Product> productWriter(){
        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
//                .sql("INSERT INTO product_catalog (name , category , description, price , available_quantity, is_active, created_at, updated_at)"+
//                        "VALUES (:name, :category, :description, :price, :availableQuantity, :isActive , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                .sql("INSERT INTO product (product_id, name, description, category, created_at, updated_at) " +
                        "VALUES (:productId, :name, :description, :category, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                .beanMapped()
                .build();


    }

    @Bean
    @StepScope
    public FlatFileItemReader<Inventory> inventoryReader(@Value("#{jobParameters['inventoryFile']}") String inventoryFilePath){
        if (inventoryFilePath == null){
            throw new IllegalArgumentException("Inventory file is required");
        }
        Path path = Paths.get(inventoryFilePath);
        return new FlatFileItemReaderBuilder<Inventory>()
                .name("inventoryItemReader")
                .resource(new FileSystemResource(path.toFile()))
                .delimited()
                .names("inventoryId","productId", "category","price","availableQuantity")
                .linesToSkip(1)
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
//                    setTargetType(Inventory.class);
//                }})
                .fieldSetMapper(new FieldSetMapper<Inventory>(){
                    @Override
                    public Inventory mapFieldSet(FieldSet fieldSet) throws BindException {
                        Inventory inventory = new Inventory();
                        inventory.setInventoryId(fieldSet.readLong("inventoryId"));
                        inventory.setCategory(fieldSet.readString("category"));
                        inventory.setPrice(fieldSet.readBigDecimal("price"));
                        inventory.setAvailableQuantity(fieldSet.readInt("availableQuantity"));
                        Long productId = fieldSet.readLong("productId");
                        Product product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product not found for ID:" + inventory.getProductId()));
                        inventory.setProductId(product);
                        return inventory;
                    }
                })
                .build();

    }
    @Bean
    public ItemProcessor<Inventory,Inventory> inventoryProcessor(){
        return inventory ->{
            if(inventory.getPrice().compareTo(BigDecimal.ZERO) <= 0 ||
                    (inventory.getAvailableQuantity() != null && inventory.getAvailableQuantity() < 0)){
                throw new IllegalArgumentException("Invalid inventory data: "+ inventory);
            }



            return inventory;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Inventory> inventoryWriter(){
        return new JdbcBatchItemWriterBuilder<Inventory>()
                .dataSource(dataSource)
                .sql("INSERT INTO inventory (inventory_id, product_id, category, price, available_quantity,created_at, updated_at) "+
                        "VALUES (:inventoryId , :productId.productId, :category , :price, :availableQuantity,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step productStep(ItemReader<Product> reader,
                     ItemProcessor<Product, Product> processor,
                     ItemWriter<Product> writer){
        return new StepBuilder("ProductStep", jobRepository)
                .<Product , Product>chunk(10,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();


    }
@Bean
public Step inventoryStep(ItemReader<Inventory> inventoryReader,
                          ItemProcessor<Inventory,Inventory> inventoryProcessor,
                          ItemWriter<Inventory> inventoryWriter){
        return new StepBuilder("InventoryStep", jobRepository)
                .<Inventory, Inventory>chunk(10,transactionManager)
                .reader(inventoryReader)
                .processor(inventoryProcessor)
                .writer(inventoryWriter)
                .build();
}


    @Bean
    public Job importProductJob(JobCompletionNotificationListener listener ,Step productStep){
        return new JobBuilder("importProductJob", jobRepository)
                .start(productStep)

                .listener(listener)
                .build();
    }

    @Bean
    public Job importInventoryJob(JobCompletionNotificationListener listener , Step inventoryStep){
        return new JobBuilder("importInventoryJob", jobRepository)
                .start(inventoryStep)
                .listener(listener)
                .build();
    }


}
