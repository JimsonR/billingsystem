package com.example.billingsystem.Exceptions;

public class CustomerNotFoundException extends  RuntimeException{
   public CustomerNotFoundException(){
        super("Customer not found");
    }
}
