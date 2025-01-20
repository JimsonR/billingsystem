package com.example.billingsystem.Exceptions;

import org.aspectj.weaver.ast.Or;

public class OrderNotFoundException extends RuntimeException{
   public OrderNotFoundException(){
        super("Order not found");
    }
}
