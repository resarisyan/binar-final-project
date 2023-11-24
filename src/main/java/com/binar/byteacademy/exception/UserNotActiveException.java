package com.binar.byteacademy.exception;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message){
        super(message);
    }
}
