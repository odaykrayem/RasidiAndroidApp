package com.example.RasidiAndroid.Models;

public class BillModel {
    private int bill_id;
    private String bill_type;
    private int value;
    private boolean isPaid;
    private String payment_date;

    public BillModel(int bill_id, String bill_type, int value, String payment_date, boolean isPaid) {
        this.bill_id = bill_id;
        this.bill_type = bill_type;
        this.value = value;
        this.isPaid = isPaid;
        this.payment_date = payment_date;
    }

    public int getBill_id() {
        return bill_id;
    }

    public String getBill_type() {
        return bill_type;
    }

    public int getValue() {
        return value;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }
}
