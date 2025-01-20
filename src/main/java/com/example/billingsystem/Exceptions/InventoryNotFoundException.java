package com.example.billingsystem.Exceptions;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(){
        super("Inventory not found");
    }
}
