package com.example.billingsystem.controller;

import com.example.billingsystem.Exceptions.MissingDetailsException;
import com.example.billingsystem.model.InventoryModel;
import com.example.billingsystem.Exceptions.MissingDetailsException;
import com.example.billingsystem.model.ProductModel;
import com.example.billingsystem.service.InventoryService;
import com.example.billingsystem.service.ProductService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;


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

    @PostMapping("/create")
    public ResponseEntity<String> addProduct(@RequestBody ProductModel productModel){
        StringBuilder validationError = new StringBuilder();
        if(productModel.name == null || productModel.name.isEmpty()){
            validationError.append("Product Name ,");
        }
        if (productModel.category == null || productModel.category.isEmpty()){
            validationError.append("Product Category ,");
        }
        if(validationError.length()> 0){
            throw new MissingDetailsException(validationError.toString().trim());
        }
        return ResponseEntity.ok(productService.createProduct(productModel));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductModel productModel){
        return ResponseEntity.ok(productService.createProduct(productModel));

    }

    @GetMapping("/allproducts")
    public ResponseEntity<?> allProducts(@RequestParam("pgNo")int pgNo, @RequestParam("pgSize") int pgSize){

        return ResponseEntity.ok(productService.getAllProducts(pgNo,pgSize));
    }

    @GetMapping("/allproducts/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }


//    @DeleteMapping ("/delete/{id}")
//    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
//        return ResponseEntity.ok(productService.deleteProduct(id));
//    }

    @GetMapping("/getinventory")
    public ResponseEntity<?> getInventory(@RequestParam ("id") long id){
        return ResponseEntity.ok(inventoryService.getInventory(id));
    }

@PostMapping("/setinventory")
    public ResponseEntity<String> setInventory(@RequestBody InventoryModel inventoryModel){
        return ResponseEntity.ok(inventoryService.createUpdateInventory(inventoryModel));
}

//@PutMapping("/updateinventory")
//    public ResponseEntity<String> putInventory(@RequestBody InventoryModel inventoryModel){
//        return ResponseEntity.ok(inventoryService.createUpdateInventory(inventoryModel));
//}

@DeleteMapping("/deleteinventory")
    public ResponseEntity<String> deleteInventory(@RequestParam ("id") long id){
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
}// request param ante /deleteinventory?id=1 ala vastadhi , path variable ante /deleteinventory/1

    @GetMapping("/prodinv")
    public ResponseEntity<?> getInvByProd(@RequestParam long id){
        return ResponseEntity.ok(inventoryService.findByProdId(id));
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/allproducts/search")
    public ResponseEntity<?> searchProducts(@RequestParam("pgNo") int pgNo , @RequestParam("pgSize") int pgSize,@RequestParam("term")String term){
        return ResponseEntity.ok(productService.searchProducts(term, pgNo, pgSize));
    }

}
