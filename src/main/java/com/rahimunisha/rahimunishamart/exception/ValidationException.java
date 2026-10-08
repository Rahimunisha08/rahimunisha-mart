package com.rahimunisha.rahimunishamart.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ValidationException: Captures field-level validation errors
 * satisfying Section 14 / Week 5 requirement.
 */
public class ValidationException extends AppException {
    private static final long serialVersionUID = 1L;

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? new HashMap<>(fieldErrors) : Collections.emptyMap();
    }

    public ValidationException(String field, String errorMessage) {
        super("Validation failed on field: " + field);
        Map<String, String> map = new HashMap<>();
        map.put(field, errorMessage);
        this.fieldErrors = map;
    }

    public Map<String, String> getFieldErrors() {
        return Collections.unmodifiableMap(fieldErrors);
    }
}
