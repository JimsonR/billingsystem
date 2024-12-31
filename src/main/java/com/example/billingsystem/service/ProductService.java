package com.example.billingsystem.service;

import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.ProductModel;
import com.example.billingsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public String createProduct(ProductModel productModel){
       if (productModel.id != null){
           Product product = productRepository.findById(productModel.id).get();
           product.setName(productModel.name);
           product.setCategory(productModel.category);
           product.setDescription(productModel.description);
           product.setUpdateAt(LocalDateTime.now());

           productRepository.save(product);
           return "product updated";
       }

        Product product = new Product();
        product.setName(productModel.name);
        product.setCategory(productModel.category);
        product.setDescription(productModel.description);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());
        productRepository.save(product);
        return "product created";
    }

    public Product getProduct(long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public String deleteProduct(long id){
         productRepository.deleteById(id);
         return "Product deleted";
    }


}
