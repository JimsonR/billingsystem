package com.example.billingsystem.service;

import com.example.billingsystem.Exceptions.ProductNotFoundException;
import com.example.billingsystem.entity.Product;
import com.example.billingsystem.model.ProductAndQuantity;
import com.example.billingsystem.model.ProductModel;
import com.example.billingsystem.model.ProductsList;
import com.example.billingsystem.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public String createProduct(ProductModel productModel){

       if (productModel.id != null){
           System.out.println(productModel);
           Product product = productRepository.findById(productModel.id).get();
           product.setName(productModel.name);
           product.setCategory(productModel.category);
           product.setDescription(productModel.description);
           product.setUpdateAt(LocalDateTime.now());

           productRepository.save(product);
           return "product updated";
       }
        if (productRepository.findByNameAndCategory(productModel.name,productModel.category).isEmpty()) {
            Product product = new Product();
            product.setName(productModel.name);
            product.setCategory(productModel.category);
            product.setDescription(productModel.description);
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdateAt(LocalDateTime.now());
            productRepository.save(product);
            return "product created";
        }
        return "Product already exists";
    }

    public Product getProduct(long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
    }

    public List<Product> getAllProducts(int pgNo, int pgSize){

        return productRepository.findAll(PageRequest.of(pgNo,pgSize)).getContent();
    }

    public String deleteProduct(long id){
         productRepository.deleteById(id);
         return "Product deleted";
    }
//public ProductsList test(){
//       ProductsList productList = ProductsList.builder().productId(2).active(true).createdAt("dasd").updateAt("asd").category("dsad").description("asdasd").name("sadasd").build();
//return productList;
//    }

    public List<Product> searchProducts(String searchTerm,int pgNo , int pgSize ){
        Pageable page = PageRequest.of(pgNo,pgSize);
        Page<Product> products = productRepository.searchByNameAndCategory(searchTerm,page);
        return products.getContent();
    }

public Map<ProductsList , Long> getProductsById(long[] ids){


    int count = 0;
    Map<Long,Integer> productCounts = new HashMap<>();
    Map<ProductsList , Long> productQuantity = new HashMap<>();
    Set<Long> seen = new HashSet<>();
        for(long id: ids){

            Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException());

            ProductsList item = ProductsList.builder().
                    productId(product.getProductId()).
                    name(product.getName()).
                    description(product.getDescription())
                    .category(product.getCategory())
                    .createdAt(product.getCreatedAt().toString())
                    .updateAt(product.getUpdateAt().toString())
                    .active(product.getIsActive()).build();

            if(!seen.add(id)){
                productQuantity.put(item,productQuantity.get(item)+1);
            }
            else{
                productQuantity.put(item, 1L);
            }

        }

        return productQuantity;
}

 public List<ProductAndQuantity> productAndQuantityList(long[] ids){
     List<ProductAndQuantity> list = new ArrayList<>();
        for(Map.Entry<ProductsList, Long> prods: getProductsById(ids).entrySet()){

            list.add(ProductAndQuantity.builder()
                    .product(prods.getKey())
                    .Quantity(prods.getValue()).price(invent(prods.getKey().getProductId())).build());
        }
        return list;
 }

}
