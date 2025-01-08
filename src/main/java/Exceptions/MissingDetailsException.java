package Exceptions;

public class MissingDetailsException extends RuntimeException{
    public MissingDetailsException(String message){
        super(message+ " required");
    }
}
