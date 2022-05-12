package com.example.RasidiAndroid.Models;

public class UserModel {
    private  int user_id;

    private  String firstName;
    private  String middleName;
    private  String lastName;
    private  String mobileNumber;
    private  String phoneNumber;
    private  String nationalNumber;
    private  String address;
    private  String password;

    private  int balance;

    public UserModel(int user_id,
                     String firstName,
                     String middleName,
                     String lastName,
                     String mobileNumber,
                     String phoneNumber,
                     String nationalNumber,
                     String address,
                     String password,
                     int balance) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.phoneNumber = phoneNumber;
        this.nationalNumber = nationalNumber;
        this.address = address;
        this.password = password;
        this.balance = balance;
    }

    public UserModel(String firstName,
                     String middleName,
                     String lastName,
                     String mobileNumber,
                     String phoneNumber,
                     String nationalNumber,
                     String address,
                     String password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.phoneNumber = phoneNumber;
        this.nationalNumber = nationalNumber;
        this.address = address;
        this.password = password;

    }

    public UserModel() {
    }


    public int getUser_id() {
        return user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
