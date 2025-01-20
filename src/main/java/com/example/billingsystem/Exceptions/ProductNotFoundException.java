package com.example.billingsystem.Exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(){
        super("Product Not Found Exception");
    }


}
