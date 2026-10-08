package com.rahimunisha.rahimunishamart.dto;

import java.io.Serializable;

public class RegisterRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private String fullName;
    private String role; // BUYER or SELLER only (Admin is seed-only)
    private String phone;
    private String address;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String email, String password, String fullName, String role, String phone, String address) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
