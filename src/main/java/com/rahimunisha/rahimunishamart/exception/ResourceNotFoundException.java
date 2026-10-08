package com.rahimunisha.rahimunishamart.exception;

public class ResourceNotFoundException extends AppException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
