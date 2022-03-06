package com.veracode.storypointer.exception;

public class UnAuthorizedException extends RuntimeException{

    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
