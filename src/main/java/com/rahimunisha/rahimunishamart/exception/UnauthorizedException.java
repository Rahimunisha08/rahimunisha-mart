package com.rahimunisha.rahimunishamart.exception;

public class UnauthorizedException extends AppException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }
}
