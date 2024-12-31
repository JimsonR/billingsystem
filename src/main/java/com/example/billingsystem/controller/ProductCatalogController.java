package com.example.billingsystem.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("products")
public class ProductCatalogController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importProductJob;

    @Autowired
    private Job importInventoryJob;


//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
//        try{
//
//            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
//            jobParametersBuilder.addString("filename", file.getOriginalFilename());
//            jobParametersBuilder.addLong("timestamp",System.currentTimeMillis());
//
////            InputStreamResource resource = new InputStreamResource(file.getInputStream());
//            Path tempFile = Files.createTempFile("uploaded-file-", ".csv");
//            file.transferTo(tempFile.toFile());
//
//            jobParametersBuilder.addString("file",tempFile.toString());
//
//            jobLauncher.run(importProductJob, jobParametersBuilder.toJobParameters());
//
//            return ResponseEntity.ok("FIle uploaded and processing started");
//        }catch (Exception e){
//            return ResponseEntity.status(500).body("error processing file: "+ e.getMessage());
//        }
//    }

    private ResponseEntity<String> processCSV(MultipartFile file, Job job , String parametersKey) {
        try {

            Path tempFile = Files.createTempFile("uploaded-file-", ".csv");
            file.transferTo(tempFile.toFile());


            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addString(parametersKey, tempFile.toAbsolutePath().toString());
            jobParametersBuilder.addLong("timestamp", System.currentTimeMillis());
//            jobParametersBuilder.addString("file", tempFile.toString());


            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
            return ResponseEntity.ok("file successfully processed" + file.getOriginalFilename());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process file:" + file.getOriginalFilename() + " - " + e.getMessage());

        }
    }

    @PostMapping("upload/product")
    public ResponseEntity<String> uploadProductCSV(@RequestParam("file") MultipartFile file){
        return processCSV(file, importProductJob, "productFile");

    }

    @PostMapping("upload/inventory")
    public ResponseEntity<String> uploadInventoryCSV(@RequestParam("file") MultipartFile file){
        return processCSV(file,importInventoryJob,"inventoryFile");
    }



}
