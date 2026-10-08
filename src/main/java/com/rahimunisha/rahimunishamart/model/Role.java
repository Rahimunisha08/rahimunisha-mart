package com.rahimunisha.rahimunishamart.model;

public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String value) {
        if (value == null) {
            return BUYER;
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
