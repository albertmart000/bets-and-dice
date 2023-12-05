package com.betsanddice.tutorial.exception;

public class GameTutorialAlreadyExistException extends RuntimeException {

    public GameTutorialAlreadyExistException(String message) {
        super(message);
    }
}
