package com.betsanddice.user.exception;

public class BadUuidException extends Exception{
    public BadUuidException(String message){
        super(message);
    }
    public BadUuidException(){}
}

