package com.example.billingsystem.Exceptions;

public class DuplicationException extends RuntimeException{
    DuplicationException(){
        super("Duplicate product ID");
    }
}
