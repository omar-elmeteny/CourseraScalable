package com.guctechie.datainsertions.exceptions;

public class ApplicatioException extends Exception{
    public ApplicatioException(String message) {
        super(message);
    }

    public ApplicatioException(String message, Throwable cause) {
        super(message, cause);
    }
}
