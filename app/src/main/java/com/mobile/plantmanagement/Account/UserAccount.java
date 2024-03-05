package com.mobile.plantmanagement.Account;

public class UserAccount {
    private String name;
    private String phoneNumber;
    private String email;
    private String street;
    private String apartment;
    private String houseNumber;

    public UserAccount() {
        this.name = "";
        this.phoneNumber = "";
        this.email = "";
        this.street = "";
        this.apartment = "";
        this.houseNumber = "";
    }

    public UserAccount(String name, String phoneNumber, String email, String street, String apartment, String houseNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
        this.apartment = apartment;
        this.houseNumber = houseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
